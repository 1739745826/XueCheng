package com.xuecheng.api.config.learning;

import com.xuecheng.framework.domain.learning.respones.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/12 - 9:14
 */
@Api(value = "录播课程学习管理", description = "录播课程学习管理")
public interface CourseLearningControllerApi {
	@ApiOperation("获取课程学习地址")
	public GetMediaResult getMedia(String courseId, String teachplanId);
}
