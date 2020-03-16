package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 18:17
 */
// @Component
public class LoginFilterTest extends ZuulFilter {
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
		HttpServletResponse response = requestContext.getResponse();
		// 得到Authorization头
		String header = request.getHeader("Authorization");
		if (StringUtils.isEmpty(header)) {
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
			return null;
		}
		return null;
	}
}
