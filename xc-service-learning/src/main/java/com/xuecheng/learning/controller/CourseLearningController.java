package com.xuecheng.learning.controller;

import com.xuecheng.api.config.learning.CourseLearningControllerApi;
import com.xuecheng.framework.domain.learning.respones.GetMediaResult;
import com.xuecheng.learning.service.CourseLearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/12 - 9:25
 */
@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {
	@Autowired
	CourseLearningService courseLearningService;

	// 获取课程学习地址
	@Override
	@GetMapping("/getmedia/{courseId}/{teachplanId}")
	public GetMediaResult getMedia(@PathVariable("courseId") String courseId, @PathVariable("teachplanId") String teachplanId) {
		return courseLearningService.getMedia(courseId, teachplanId);
	}
}
