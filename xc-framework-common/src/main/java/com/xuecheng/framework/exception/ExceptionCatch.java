package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 标题：异常捕获类
 * 作者：何处是归程
 * 时间：2020/2/27 - 9:02
 */
@ControllerAdvice // 控制器增强
public class ExceptionCatch {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

	// 定义Map配置异常类型所对应的错误代码
	private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
	// 定义Map的Builder对象，去构建ImmutableMap
	protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

	static {
		builder.put(HttpMessageNotReadableException.class, CommonCode.INVALLD_PARAM);
	}

	// 捕获CustomException此类异常
	@ResponseBody
	@ExceptionHandler(CustomException.class)
	public ResponseResult customException(CustomException customException) {
		// 记录日志
		LOGGER.error("catch exception{}", customException.getMessage());
		ResultCode resultCode = customException.getResultCode();
		return new ResponseResult(resultCode);
	}

	// 捕获不可预知异常
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseResult exception(Exception exception) {
		// 记录日志
		LOGGER.error("catch exception{}", exception.getMessage());
		if (EXCEPTIONS == null) {
			EXCEPTIONS = builder.build(); // EXCEPTIONS构建成功
		}
		// 从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999
		ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
		if (resultCode != null) {
			return new ResponseResult(resultCode);
		} else {
			return new ResponseResult(CommonCode.SERVER_ERROR);
		}
	}
}
