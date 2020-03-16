package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/10 - 19:27
 */
public interface TeacherplanMediaRepostiory extends JpaRepository<TeachplanMedia, String> {
	// 根据ID查询猎豹
	List<TeachplanMedia> findByCourseId(String courseId);
}
