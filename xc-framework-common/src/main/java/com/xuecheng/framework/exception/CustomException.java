package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 标题：自定义异常
 * 作者：何处是归程
 * 时间：2020/2/27 - 8:57
 */
public class CustomException extends RuntimeException {
	ResultCode resultCode;

	public CustomException(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public ResultCode getResultCode() {
		return resultCode;
	}
}
