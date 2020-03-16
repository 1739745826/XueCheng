package com.xuecheng.auth;

import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/13 - 12:16
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {
	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	RestTemplate restTemplate;

	@Test
	public void testClient() {
		// 从Eureka中获取认证服务的地址
		ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
		// 此地址就是http://ip:port
		URI uri = serviceInstance.getUri();
		// 令牌申请地址http://localhost:40400/auth/oauth/token
		String authUrl = uri + "/auth/oauth/token";
		System.out.println(authUrl);
		// 定义header
		LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
		String httpBasic = getHttpBasic("XcWebApp", "XcWebApp");
		header.add("Authorization", httpBasic);
		// 定义body
		LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("username", "itcast");
		body.add("password", "123");
		HttpEntity<MultivaluedMap<String, String>> httpEntity = new HttpEntity(body, header);
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
		System.out.println(bodyMap);
	}

	private String getHttpBasic(String clientId, String clientSecret) {
		String string = clientId + ":" + clientSecret;
		// 将串进行base64编码
		byte[] encode = Base64Utils.encode(string.getBytes());
		return "Basic " + new String(encode);
	}
}
