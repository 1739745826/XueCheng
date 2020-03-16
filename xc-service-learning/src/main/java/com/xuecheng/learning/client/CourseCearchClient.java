package com.xuecheng.learning.client;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/12 - 9:30
 */
@FeignClient(value = "XC-SERVICE-SEARCH")
public interface CourseCearchClient {
	// 根据课程计划id查询课程媒资
	@GetMapping("/search/course/getmedia/{teachplanId}")
	public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
