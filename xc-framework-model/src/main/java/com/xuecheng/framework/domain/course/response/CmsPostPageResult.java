package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/4 - 18:51
 */
@Data
@NoArgsConstructor
@ToString
public class CmsPostPageResult extends ResponseResult {
	String pageUrl;

	public CmsPostPageResult(ResultCode resultCode, String pageUrl) {
		super(resultCode);
		this.pageUrl = pageUrl;
	}
}
