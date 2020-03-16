package com.xuecheng.auth.controller;

import com.alibaba.fastjson.JSON;
import com.xuecheng.api.config.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/13 - 16:25
 */
@RestController
public class AuthController implements AuthControllerApi {
	@Autowired
	AuthService authService;
	@Value("${auth.clientId}")
	String clientId;
	@Value("${auth.clientSecret}")
	String clientSecret;
	@Value("${auth.cookieDomain}")
	String cookieDomain;
	@Value("${auth.cookieMaxAge}")
	int cookieMaxAge;


	// 登录
	@Override
	@PostMapping("/userlogin")
	public LoginResult login(LoginRequest loginRequest) {
		if (loginRequest == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		if (StringUtils.isEmpty(loginRequest.getUsername())) {
			ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
		}
		if (StringUtils.isEmpty(loginRequest.getPassword())) {
			ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
		}
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		// 申请令牌
		AuthToken authToken = authService.login(username, password, clientId, clientSecret);
		String access_token = authToken.getAccess_token();
		// 将令牌存到cookie
		saveCookie(access_token);
		return new LoginResult(CommonCode.SUCCESS, access_token);
	}

	//将令牌存储到cookie
	private void saveCookie(String access_token) {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		//HttpServletResponse response,String domain,String path, String name, String value, int maxAge,boolean httpOnly
		CookieUtil.addCookie(response, cookieDomain, "/", "uid", access_token, cookieMaxAge, false);
	}

	/**
	 * @功能: 查询用户的JWT令牌
	 * @作者: 高志红
	 */
	@Override
	@GetMapping("/userjwt")
	public JwtResult userJwt() {
		// 取出cookie中的用户身份令牌
		String uid = getTokenFormCookie();
		if (uid == null) {
			return new JwtResult(CommonCode.FAIL, null);
		}
		// 拿身份令牌从redis中查询jwt令牌
		AuthToken userToken = authService.getUserToken(uid);
		if (userToken != null) {
			String jwt_token = userToken.getJwt_token();
			// 将jwt令牌返回给用户
			return new JwtResult(CommonCode.SUCCESS, jwt_token);
		}
		return null;
	}

	/**
	 * @功能: 取出cookie中的身份令牌
	 * @作者: 高志红
	 */
	private String getTokenFormCookie() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, String> uidMap = CookieUtil.readCookie(request, "uid");
		if (uidMap != null && uidMap.get("uid") != null) {
			String uid = uidMap.get("uid");
			return uid;
		}
		return null;
	}

	// 退出
	@Override
	@PostMapping("/userlogout")
	public ResponseResult logout() {
		// 取出cookie中的用户身份令牌
		String uid = getTokenFormCookie();
		// 删除redis中的token
		authService.delToken(uid);
		// 清除cookie
		delCookie(uid);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	//将令牌存储到cookie
	private void delCookie(String access_token) {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		//HttpServletResponse response,String domain,String path, String name, String value, int maxAge,boolean httpOnly
		CookieUtil.addCookie(response, cookieDomain, "/", "uid", access_token, 0, false);
	}
}
