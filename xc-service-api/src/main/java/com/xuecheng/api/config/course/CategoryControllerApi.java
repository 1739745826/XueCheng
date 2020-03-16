package com.xuecheng.api.config.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 10:44
 */
@Api(value = "课程分类管理", description = "课程分类管理，提供课程分类的查询")
public interface CategoryControllerApi {
	CategoryNode findList();
}
