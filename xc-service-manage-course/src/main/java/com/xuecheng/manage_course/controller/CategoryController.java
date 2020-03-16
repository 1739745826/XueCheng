package com.xuecheng.manage_course.controller;

import com.xuecheng.api.config.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 10:59
 */
@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {
	@Autowired
	CategoryService categoryService;

	@Override
	@GetMapping("/list")
	public CategoryNode findList() {
		return categoryService.findList();
	}
}
