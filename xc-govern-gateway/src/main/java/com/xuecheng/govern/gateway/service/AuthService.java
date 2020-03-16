package com.xuecheng.govern.gateway.service;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 18:47
 */
@Service
public class AuthService {
	@Autowired
	StringRedisTemplate redisTemplate;

	// 从头取出jwt令牌
	public String getJwtFromHeader(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (StringUtils.isEmpty(header)) {
			return null;
		}
		if (!header.startsWith("Bearer ")) {
			return null;
		}
		String[] jwt = header.split(" ");
		return jwt[1];
	}

	// 从cookie取出token
	public String getTokenFormCookie(HttpServletRequest request) {
		Map<String, String> uidMap = CookieUtil.readCookie(request, "uid");
		if (uidMap != null && uidMap.get("uid") != null) {
			String uid = uidMap.get("uid");
			return uid;
		}
		return null;
	}

	// 判断redis中的令牌是否过期
	public long getExpire(String access_token) {
		// key
		String key = "user_token:" + access_token;
		Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
		return expire;
	}
}
