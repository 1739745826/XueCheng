package com.xuecheng.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/27 - 11:20
 */
@SpringBootApplication
public class FreemarkerApplication {
	public static void main(String[] args) {
		SpringApplication.run(FreemarkerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
	}
}
