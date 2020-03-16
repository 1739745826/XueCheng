package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/16 - 14:03
 */
public interface XcLearningCourseRequestory extends JpaRepository<XcLearningCourse, String> {
	/**
	 * @功能: 根据用户ID和课程ID查询
	 * @作者: 高志红
	*/
	XcLearningCourse findByUserIdAndCourseId(String userId, String courseId);
}
