package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/25 - 15:09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
	@Autowired
	CmsPageRepository cmsPageRepository;

	// 查询全部
	@Test
	public void testFindAll() {
		List<CmsPage> all = cmsPageRepository.findAll();
		System.out.println(all);
	}

	// 分页查询
	@Test
	public void testFindPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<CmsPage> all = cmsPageRepository.findAll(pageable);
		System.out.println(all);
	}

	// 修改
	@Test
	public void testFindUpdate() {
		// 查询对象
		Optional<CmsPage> optional = cmsPageRepository.findById("5abefd525b05aa293098fca6");
		if (optional.isPresent()) {
			CmsPage cmsPage = optional.get();
			// 设置要修改的值
			cmsPage.setPageAliase("曹璐");
			// 修改
			CmsPage save = cmsPageRepository.save(cmsPage);
			System.out.println(save);
		}
	}

	// 根据页面名称查询
	@Test
	public void testfindByPageName() {
		CmsPage cmsPage = cmsPageRepository.findByPageName("index2.html");
		System.out.println(cmsPage);
	}

	// 自定义条件查询测试
	@Test
	public void testFindAllbYexample() {
		Pageable pageable = PageRequest.of(0, 10);
		// 条件值对象
		CmsPage cmsPage = new CmsPage();
		// 站点id
		cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
		// 模板id
		cmsPage.setTemplateId("5a962bf8b00ffc514038fafa");
		// 页面别名
		cmsPage.setPageAliase("轮");
		// 条件匹配器
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
		// ExampleMatcher.GenericPropertyMatchers.contains() 包含匹配
		// ExampleMatcher.GenericPropertyMatchers.startsWith()  前缀匹配
		// 定义Example
		Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
		Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
		List<CmsPage> content = all.getContent();
		System.out.println(content);
	}
}
