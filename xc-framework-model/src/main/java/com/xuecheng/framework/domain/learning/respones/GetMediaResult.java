package com.xuecheng.framework.domain.learning.respones;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/12 - 9:16
 */
@Data
@NoArgsConstructor
@ToString
public class GetMediaResult extends ResponseResult {
	// 视频播放地址
	String fileUrl;

	public GetMediaResult(ResultCode resultCode, String fileUrl) {
		super(resultCode);
		this.fileUrl = fileUrl;
	}
}
