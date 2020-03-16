package com.xuecheng.manage_course.clienttest;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/3 - 16:23
 */
@FeignClient(value = "XC-SERVICE-MANAGE-CMS") // 指定远程调用的服务名
public interface CmsPaegClient {
	// 根据页面ID查询页面信息，　远程调用cms请求数据
	@GetMapping("/cms/page/get/{id}") // 用@GetMapping标识远程调用http方法类型
	public CmsPage findCmaPageById(@PathVariable("id") String id);

	// 预览页面 用于课程预览
	@PostMapping("/cms/page/save")
	public CmsPageResult save(@RequestBody CmsPage cmsPage);

	// 一件发布
	@PostMapping("/cms/page/postPageQuick")
	public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}
