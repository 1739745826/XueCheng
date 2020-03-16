package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.config.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/27 - 15:33
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {
	@Autowired
	PageService pageService;

	@Override
	@GetMapping("/getmodel/{id}")
	public CmsConfig getModel(@PathVariable("id") String id) {
		return pageService.getConfigById(id);
	}
}
