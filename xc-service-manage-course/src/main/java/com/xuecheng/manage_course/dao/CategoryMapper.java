package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 10:46
 */
@Mapper
@Component
public interface CategoryMapper {
	CategoryNode findList();
}
