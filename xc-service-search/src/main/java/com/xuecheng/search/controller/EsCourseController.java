package com.xuecheng.search.controller;

import com.xuecheng.api.config.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/7 - 20:33
 */
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {
	@Autowired
	EsCourseService esCourseService;

	// 课程综合搜索
	@Override
	@GetMapping("/list/{page}/{size}")
	public QueryResponseResult<CoursePub> list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
		return esCourseService.list(page, size, courseSearchParam);
	}

	// 根据课程id查询课程信息
	@Override
	@GetMapping("/getall/{id}")
	public Map<String, CoursePub> getall(@PathVariable("id") String id) {
		return esCourseService.getall(id);
	}

	// 根据课程计划id查询媒资信息
	@Override
	@GetMapping(value = "/getmedia/{teachplanId}")
	public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
		String[] teachplanIds = new String[]{teachplanId};
		QueryResponseResult<TeachplanMediaPub> queryResponseResult = esCourseService.getmedia(teachplanIds);
		QueryResult<TeachplanMediaPub> queryResult = queryResponseResult.getQueryResult();
		if (queryResult != null) {
			List<TeachplanMediaPub> list = queryResult.getList();
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}
}
