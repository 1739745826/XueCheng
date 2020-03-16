package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 18:17
 */
@Component
public class LoginFilter extends ZuulFilter {
	@Autowired
	AuthService authService;

	/**
	 * @功能: 设置过滤器的类型
	 * pre：在路由之前执行
	 * routing：在路由请求时调用
	 * post：在routing和error过滤器之后调用
	 * error：处理请i去发生错误时调用
	 * @作者: 高志红
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/**
	 * @功能: 过滤器的序号，越小越先被执行
	 * @作者: 高志红
	 */
	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * @功能: 判断该过滤器是否需要执行
	 * @作者: 高志红
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * @功能: 过滤的内容
	 * @作者: 高志红
	 */
	@Override
	public Object run() throws ZuulException {
		// 得到request
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		// 取出cookie中的身份令牌
		String cookie = authService.getTokenFormCookie(request);
		if (StringUtils.isEmpty(cookie)) {
			// 拒绝访问
			access_denied();
			return null;
		}
		// 从header中取jwt
		String jwtFromHeader = authService.getJwtFromHeader(request);
		if (StringUtils.isEmpty(jwtFromHeader)) {
			// 拒绝访问
			access_denied();
			return null;
		}
		// 从redis取出jwt的过期时间
		long expire = authService.getExpire(cookie);
		if (expire < 0) {
			// 拒绝访问
			access_denied();
			return null;
		}
		return null;
	}

	/**
	 * @功能: 拒绝访问
	 * @作者: 高志红
	 */
	private void access_denied() {
		// 得到request
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletResponse response = requestContext.getResponse();
		// 拒绝访问
		requestContext.setSendZuulResponse(false);
		// 设置响应代码
		requestContext.setResponseStatusCode(200);
		// 构建响应的信息
		ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
		// 转成json
		String jsonString = JSON.toJSONString(responseResult);
		requestContext.setResponseBody(jsonString);
		// 设置响应
		response.setContentType("application/json;charset=utf-8");
	}
}
