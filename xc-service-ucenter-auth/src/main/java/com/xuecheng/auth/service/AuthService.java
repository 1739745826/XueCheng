package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/13 - 16:27
 */
@Service
public class AuthService {
	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	StringRedisTemplate redisTemplate;
	@Value("${auth.tokenValiditySeconds}")
	int tokenValiditySeconds;

	// 用户认证申请令牌
	public AuthToken login(String username, String password, String clientId, String clientSecret) {
		// 请求spring security申请令牌
		AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
		if (authToken == null) {
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
		}
		// 将令牌存储到redis
		// 用户身份令牌
		String access_token = authToken.getAccess_token();
		// 存储到redis中的内容
		String jsonStr = JSON.toJSONString(authToken);
		boolean falg = this.saveToken(access_token, jsonStr, tokenValiditySeconds);
		if (!falg) {
			ExceptionCast.cast(AuthCode.AUTH_SAVE_APPLYTOKEN_FAIL);
		}
		return authToken;
	}

	// 申请令牌
	private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
		// 请求spring secuity送去令牌
		// 从Eureka中获取认证服务的地址
		ServiceInstance serviceInstance = loadBalancerClient.choose("XC-SERVICE-UCENTER-AUTH");
		// 此地址就是http://ip:port
		URI uri = serviceInstance.getUri();
		// 令牌申请地址http://localhost:40400/auth/oauth/token
		String authUrl = uri + "/auth/oauth/token";
		System.out.println(authUrl);
		// 定义header
		LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
		String httpBasic = getHttpBasic(clientId, clientSecret);
		header.add("Authorization", httpBasic);
		// 定义body
		LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("username", username);
		body.add("password", password);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(body, header);
		// 设置restTemplate远程调用的时候，对400，401不让报错，正常返回数据
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
					super.handleError(response);
				}
			}
		});
		ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
		// 申请令牌信息
		Map bodyMap = exchange.getBody();
		if (bodyMap == null ||
				bodyMap.get("access_token") == null ||
				bodyMap.get("refresh_token") == null ||
				bodyMap.get("jti") == null) {
			// 解析Spring security返回的错误信息
			if (bodyMap != null && bodyMap.get("error_description") != null) {
				String error_description = (String) bodyMap.get("error_description");
				if (error_description.indexOf("UserDetailsService returned null") >= 0) {
					ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
				} else if (error_description.indexOf("坏的凭证") >= 0) {
					ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
				}
			}
			return null;
		}
		AuthToken authToken = new AuthToken();
		authToken.setAccess_token((String) bodyMap.get("jti")); // jwt令牌
		authToken.setRefresh_token((String) bodyMap.get("refresh_token")); // 刷新令牌
		authToken.setJwt_token((String) bodyMap.get("access_token")); // 用户身份令牌
		return authToken;
	}

	// 获取HttpBasic串
	private String getHttpBasic(String clientId, String clientSecret) {
		String string = clientId + ":" + clientSecret;
		// 将串进行base64编码
		byte[] encode = Base64Utils.encode(string.getBytes());
		return "Basic " + new String(encode);
	}
	// 将令牌存储到redis
	// access_token：用户身份令牌
	// content：AuthToken对象的内容
	// ttl：过期时间
	private boolean saveToken(String access_token, String content, long ttl) {
		String key = "user_token:" + access_token;
		redisTemplate.boundValueOps(key).set(content, ttl, TimeUnit.SECONDS);
		Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
		return expire > 0;
	}

	/**
	 * @功能: 从redis查询令牌
	 * @作者: 高志红
	 */
	public AuthToken getUserToken(String token) {
		String key = "user_token:" + token;
		// 从redis中取到的令牌信息
		String userToken = redisTemplate.opsForValue().get(key);
		// 转成对象
		try {
			AuthToken authToken = JSON.parseObject(userToken, AuthToken.class);
			return authToken;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @功能: 删除redis中的token
	 * @作者: 高志红
	 */
	public boolean delToken(String access_token) {
		String key = "user_token:" + access_token;
		Boolean delete = redisTemplate.delete(key);
		return delete;
	}
}
