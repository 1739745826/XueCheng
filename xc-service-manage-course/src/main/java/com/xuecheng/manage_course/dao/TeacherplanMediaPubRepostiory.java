package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/10 - 19:27
 */
public interface TeacherplanMediaPubRepostiory extends JpaRepository<TeachplanMediaPub, String> {
	// 根据课程id删除
	long deleteByCourseId(String courseId);
}
