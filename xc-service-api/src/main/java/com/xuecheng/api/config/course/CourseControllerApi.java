package com.xuecheng.api.config.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.Response;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 15:58
 */
@Api(value = "课程管理接口", description = "课程管理接口，提供课程的增、删、改、查")
public interface CourseControllerApi {
	@ApiOperation("课程计划查询")
	public TeachplanNode findTeachplanList(String courseId);

	@ApiOperation("课程计划添加")
	public ResponseResult addTeachplan(Teachplan teachplan);

	@ApiOperation("我的课程分页查询")
	public QueryResponseResult findCourseListPage(Integer page, Integer size, CourseListRequest courseListRequest);

	@ApiOperation("添加课程")
	public ResponseResult saveCourseList(CourseBase courseBase);

	@ApiOperation("按照课程ID查询课程基本信息")
	public CourseBase findByCourseId(String courseId);

	@ApiOperation("修改课程基本信息")
	public ResponseResult updateCourse(String courseId, CourseBase courseBase);

	@ApiOperation("获取课程营销信息")
	public CourseMarket fingCourseMarketById(String courseId);

	@ApiOperation("修改课程营销信息")
	public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket);

	@ApiOperation("添加图片与课程的关联信息")
	public ResponseResult addCoursePic(String courseId, String pic);

	@ApiOperation("查询课程图片")
	public CoursePic findCoursePic(String courseId);

	@ApiOperation("根据课程ID删除图片")
	public ResponseResult deleteCoursePic(String courseId);

	@ApiOperation("课程视图查询")
	public CourseView courseView(String id);

	@ApiOperation("课程预览")
	public CoursePublishResult preview(String id);

	@ApiOperation("课程发布")
	public CoursePublishResult publish(String id);

	@ApiOperation("保存课程计划与媒资文件的关联")
	public ResponseResult savemedia(TeachplanMedia teachplanMedia);
}