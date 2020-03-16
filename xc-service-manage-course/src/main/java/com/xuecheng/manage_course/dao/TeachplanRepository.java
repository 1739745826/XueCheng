package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 17:38
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
	// 根据课程Id肯和parentId查询teachplan
	public List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}
