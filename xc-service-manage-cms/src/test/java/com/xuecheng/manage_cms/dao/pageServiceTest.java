package com.xuecheng.manage_cms.dao;

import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/27 - 20:13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class pageServiceTest {
	@Autowired
	PageService pageService;

	@Test
	public void testGetPateHtml() {
		String pageHtml = pageService.getPageHtml("5e571ee82a371a2ed486dd77");
		System.out.println(pageHtml);
	}

	@Test
	public void test (){
	    pageService.getTemplateByPageId("5e571ee82a371a2ed486dd77");
	}
}
