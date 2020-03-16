package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 标题：封装自定义异常类
 * 作者：何处是归程
 * 时间：2020/2/27 - 9:00
 */
public class ExceptionCast {
	public static void cast(ResultCode resultCode) {
		throw new CustomException(resultCode);
	}
}
