package com.xuecheng.api.config.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/7 - 20:25
 */
@Api(value = "课程搜索接口", description = "提供搜索")
public interface EsCourseControllerApi {
	// 搜索课程信息
	@ApiOperation("课程综合搜索")
	public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);

	// 根据课程id查询课程信息
	@ApiOperation("根据课程id查询课程信息")
	public Map<String, CoursePub> getall(String id);

	// 根据课程计划id查询媒资信息
	@ApiOperation("根据课程计划id查询媒资信息")
	public TeachplanMediaPub getmedia(String id);
}
