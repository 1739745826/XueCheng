package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.clienttest.CmsPaegClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/3 - 15:33
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	CmsPaegClient cmsPaegClient;

	@Test
	public void test() {
		// 确定要获取的服务名
		String serverId = "XC-SERVICE-MANAGE-CMS";
		// ribbon客户端冲eurekaServer中获取服务列表
		ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://" + serverId + "/cms/page/list/1/2", Map.class);
		Map body = forEntity.getBody();
		System.out.println(body);
	}

	@Test
	public void test2 (){
	    // 发起远程调用
		CmsPage cmaPageById = cmsPaegClient.findCmaPageById("5a795ac7dd573c04508f3a56");
		System.out.println(cmaPageById);
	}
}
