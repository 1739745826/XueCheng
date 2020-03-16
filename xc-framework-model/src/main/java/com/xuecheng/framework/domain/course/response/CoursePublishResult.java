package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/4 - 15:16
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {
	String previewUrl;

	public CoursePublishResult(ResultCode resultCode, String previewUrl) {
		super(resultCode);
		this.previewUrl = previewUrl;
	}
}
