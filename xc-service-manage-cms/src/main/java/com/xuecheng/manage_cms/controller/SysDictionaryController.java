package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.config.cms.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDicthinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 14:34
 */
@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDicthinaryControllerApi {
	@Autowired
	SysDicthinaryService sysDicthinaryService;

	@Override
	@GetMapping("/get/{dType}")
	public SysDictionary findByType(@PathVariable("dType") String type) {
		return sysDicthinaryService.findByType(type);
	}
}
