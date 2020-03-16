package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/13 - 11:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Test
	public void testRedis() {
		// 设置key
		String key = "user_token:9734b68f-cf5e-456f-9bd6-df578c711390";
		// 设置value
		Map<String, String> value = new HashMap<>();
		value.put("jwt", "123456");
		value.put("refresh_token", "654321");
		String jsonValue = JSON.toJSONString(value);
		// 存储数据
		stringRedisTemplate.boundValueOps(key).set(jsonValue, 60, TimeUnit.SECONDS);
		// 校验key是否存在 如果不存在则返回 -2
		Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
		System.out.println(expire);
		// 获取数据
		String var = stringRedisTemplate.opsForValue().get(key);
		System.out.println(var);
	}
}
