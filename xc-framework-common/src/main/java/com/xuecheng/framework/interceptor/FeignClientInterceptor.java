package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/15 - 17:40
 */
public class FeignClientInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate requestTemplate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request != null) {
			// 取出当前请求的header找到jwt令牌
			Enumeration<String> headerNames = request.getHeaderNames();
			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					String headerName = headerNames.nextElement();
					String headerValue = request.getHeader(headerName);
					// 将jwt令牌向下传递
					requestTemplate.header(headerName, headerValue);
				}
			}
		}
	}
}
