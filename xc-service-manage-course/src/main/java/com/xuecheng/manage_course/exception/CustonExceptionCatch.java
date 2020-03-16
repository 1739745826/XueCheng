package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 标题：课程管理自定义异常类，其中定义异常类型所对应的错误代码
 * 作者：何处是归程
 * 时间：2020/3/15 - 9:33
 */
@ControllerAdvice
public class CustonExceptionCatch extends ExceptionCatch {
	static {
		builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
	}
}
