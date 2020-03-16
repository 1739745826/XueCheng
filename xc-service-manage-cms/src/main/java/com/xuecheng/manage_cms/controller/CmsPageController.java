package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.config.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/25 - 14:49
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
	@Autowired
	PageService pageService;

	/**
	 * @功能: 查询
	 * @作者: 高志红
	 */
	@Override
	@GetMapping("/list/{page}/{size}")
	public QueryResponseResult findList(
			@PathVariable("page") Integer page,
			@PathVariable("size") Integer size,
			QueryPageRequest queryPageRequest) {
		return pageService.findList(page, size, queryPageRequest);
	}

	/**
	 * @功能: 添加
	 * @作者: 高志红
	 */
	@Override
	@PostMapping("/add")
	public CmsPageResult add(@RequestBody CmsPage cmsPage) {
		return pageService.add(cmsPage);
	}

	/**
	 * @功能: 根据页面的ID查询页面
	 * @作者: 高志红
	 */
	@GetMapping("/get/{id}")
	@Override
	public CmsPage findById(@PathVariable("id") String id) {
		return pageService.findById(id);
	}

	/**
	 * @功能: 修改页面
	 * @作者: 高志红
	 */
	@Override
	@PutMapping("/edit/{id}")
	public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
		return pageService.edit(id, cmsPage);
	}

	/**
	 * @功能: 删除页面
	 * @作者: 高志红
	 */
	@Override
	@DeleteMapping("/del/{id}")
	public ResponseResult delelte(@PathVariable("id") String id) {
		return pageService.delete(id);
	}

	// 发布页面
	@Override
	@PostMapping("/postPage/{pageId}")
	public ResponseResult post(@PathVariable("pageId") String pageId) {
		return pageService.post(pageId);
	}

	// 保存页面
	@Override
	@PostMapping("/save")
	public ResponseResult save(@RequestBody CmsPage cmsPage) {
		return pageService.save(cmsPage);
	}

	// 发布页面
	@Override
	@PostMapping("/postPageQuick")
	public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
		return pageService.postPageQuick(cmsPage);
	}
}
