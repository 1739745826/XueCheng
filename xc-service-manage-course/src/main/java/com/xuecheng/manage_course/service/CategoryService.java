package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 11:02
 */
@Service
public class CategoryService {
	@Autowired
	CategoryMapper categoryMapper;

	// 查询课程分类
	public CategoryNode findList() {
		CategoryNode list = categoryMapper.findList();
		if (list == null) {
			ExceptionCast.cast(CourseCode.COURSE_QUERY_ISNULL);
			return null;
		}
		return list;
	}
}
