package com.xuecheng.api.config.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/13 - 12:04
 */
@Api(value = "用户认证", description = "用户认证接口")
public interface AuthControllerApi {
	@ApiOperation("登录")
	public LoginResult login(LoginRequest loginRequest);

	@ApiOperation("退出")
	public ResponseResult logout();

	@ApiOperation("查询用户的JWT令牌")
	public JwtResult userJwt();
}
