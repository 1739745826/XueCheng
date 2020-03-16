package com.xuecheng.manage_course.controller;

import com.xuecheng.api.config.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 16:59
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

	@Autowired
	CourseService courseService;

	// 查询课程计划
	@Override
	@GetMapping("/teachplan/list/{courseId}")
	public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
		return courseService.selectList(courseId);
	}

	// 添加课程计划
	@Override
	@PostMapping("/teachplan/add")
	public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
		return courseService.addTeachplan(teachplan);
	}

	// 分页查询课程
	@PreAuthorize("hasAuthority('course_find_list')")
	@Override
	@GetMapping("/myCourse/{page}/{size}")
	public QueryResponseResult findCourseListPage(
			@PathVariable("page") Integer page,
			@PathVariable("size") Integer size,
			CourseListRequest courseListRequest) {
		// 获取当前用户信息
		XcOauth2Util xcOauth2Util = new XcOauth2Util();
		XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
		// 当前用户所谓单位的ＩＤ
		String company_id = userJwt.getCompanyId();
		return courseService.findCourseListPage(company_id, page, size, courseListRequest);
	}

	// 添加课程
	@PreAuthorize("hasAuthority('course_add_course')")
	@Override
	@PostMapping("/coursebase/add")
	public ResponseResult saveCourseList(@RequestBody CourseBase courseBase) {
		return courseService.saveCourseListPage(courseBase);
	}

	// 根据ID查询课程
	// @PreAuthorize("hasAuthority('course_get_course')")
	@Override
	@GetMapping("/coursebase/get/{courseId}")
	public CourseBase findByCourseId(@PathVariable("courseId") String courseId) {
		return courseService.findByCourseId(courseId);
	}

	// 修改课程
	@Override
	@PutMapping("/coursebase/update/{courseId}")
	public ResponseResult updateCourse(@PathVariable("courseId") String courseId, @RequestBody CourseBase courseBase) {
		return courseService.updateCourse(courseId, courseBase);
	}

	// 获取课程营销信息
	@Override
	@GetMapping("/courseMarker/get/{courseId}")
	public CourseMarket fingCourseMarketById(@PathVariable("courseId") String courseId) {
		return courseService.fingCourseMarketById(courseId);
	}

	// 修改课程营销信息
	@Override
	@PutMapping("/courseMarker/update/{courseId}")
	public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseId, @RequestBody CourseMarket courseMarket) {
		return courseService.updateCourseMarket(courseId, courseMarket);
	}

	// 添加课程与图片的关联信息
	@Override
	@PostMapping("/coursepic/add")
	public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
		return courseService.addCoursePic(courseId, pic);
	}

	// 查询课程图片
	@PreAuthorize("hasAuthority('course_get_coursepic')")
	@Override
	@GetMapping("/coursepic/list/{courseId}")
	public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
		return courseService.findCoursePic(courseId);
	}

	// 删除课程图片
	@Override
	@DeleteMapping("coursepic/delete")
	public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
		return courseService.deleteCoursePic(courseId);
	}

	// 课程视图查询
	@Override
	@GetMapping("/courseview/{id}")
	public CourseView courseView(@PathVariable("id") String id) {
		return courseService.getCourseView(id);
	}

	// 课程预览
	@Override
	@PostMapping("/preview/{id}")
	public CoursePublishResult preview(@PathVariable("id") String id) {
		return courseService.preview(id);
	}

	// 课程发布
	@Override
	@PostMapping("/publish/{id}")
	public CoursePublishResult publish(@PathVariable("id") String id) {
		return courseService.publish(id);
	}

	// 保存课程计划与媒资文件的关联
	@Override
	@PostMapping("/savemedia")
	public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
		return courseService.savemedia(teachplanMedia);
	}
}
