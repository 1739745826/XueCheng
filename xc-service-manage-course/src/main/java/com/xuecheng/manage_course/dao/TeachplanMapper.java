package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 16:17
 */
@Component
@Mapper
public interface TeachplanMapper {
	// 课程计划查询
	public TeachplanNode selectList(String courseId);
}
