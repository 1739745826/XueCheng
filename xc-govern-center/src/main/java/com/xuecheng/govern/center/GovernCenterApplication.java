package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/3 - 13:45
 */
// 标识此工程是一个EurekaServer工程
@EnableEurekaServer
@SpringBootApplication
public class GovernCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(GovernCenterApplication.class, args);
	}
}
