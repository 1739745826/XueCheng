package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.respones.GetMediaResult;
import com.xuecheng.framework.domain.learning.respones.LearbubgCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.client.CourseCearchClient;
import com.xuecheng.learning.dao.XcLearningCourseRequestory;
import com.xuecheng.learning.dao.XcTaskHisRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;
import java.util.logging.Handler;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/12 - 9:28
 */
@Service
public class CourseLearningService {
	@Autowired
	CourseCearchClient courseCearchClient;
	@Autowired
	XcLearningCourseRequestory xcLearningCourseRequestory;
	@Autowired
	XcTaskHisRepository xcTaskHisRepository;

	// 获取课程学习地址
	public GetMediaResult getMedia(String courseId, String teachplanId) {
		// 远程调用搜索服务查询课程计划对应的课程媒资信息
		TeachplanMediaPub teachplanMediaPub = courseCearchClient.getmedia(teachplanId);
		if (teachplanMediaPub == null || StringUtils.isEmpty(teachplanMediaPub.getMediaUrl())) {
			ExceptionCast.cast(LearbubgCode.LEARNING_GETMEDIA_ERROR);
		}
		return new GetMediaResult(CommonCode.SUCCESS, teachplanMediaPub.getMediaUrl());
	}

	/**
	 * @功能: 添加选课
	 * @作者: 高志红
	 */
	@Transactional
	public ResponseResult addCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask) {
		if (StringUtils.isEmpty(courseId)) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		if (StringUtils.isEmpty(userId)) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		if (xcTask == null || StringUtils.isEmpty(xcTask.getId())) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		XcLearningCourse xcLearningCourse = xcLearningCourseRequestory.findByUserIdAndCourseId(userId, courseId);
		if (xcLearningCourse != null) {
			// 更新选课记录
			// 课程的开始时间
			xcLearningCourse.setStartTime(startTime);
			xcLearningCourse.setEndTime(endTime);
			xcLearningCourse.setStatus("501001");
			xcLearningCourseRequestory.save(xcLearningCourse);
		} else {
			xcLearningCourse = new XcLearningCourse();
			xcLearningCourse.setUserId(userId);
			xcLearningCourse.setCourseId(courseId);
			xcLearningCourse.setValid(valid);
			xcLearningCourse.setStartTime(startTime);
			xcLearningCourse.setEndTime(endTime);
			xcLearningCourse.setStatus("501001");
			xcLearningCourseRequestory.save(xcLearningCourse);
		}
		// 向历史记录表插入记录
		Optional<XcTaskHis> xcTaskHisOptional = xcTaskHisRepository.findById(xcTask.getId());
		if (!xcTaskHisOptional.isPresent()) {
			// 添加历史任务
			XcTaskHis xcTaskHis = new XcTaskHis();
			BeanUtils.copyProperties(xcTask, xcTaskHis);
			xcTaskHisRepository.save(xcTaskHis);
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}
}
