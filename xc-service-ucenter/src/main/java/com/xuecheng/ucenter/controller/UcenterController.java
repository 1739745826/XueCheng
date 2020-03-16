package com.xuecheng.ucenter.controller;

import com.xuecheng.api.config.ucenter.UcenterControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 8:57
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {
	@Autowired
	UserService userService;

	/**
	 * @功能: 根据用户账号查询用户信息
	 * @作者: 高志红
	 */
	@Override
	@GetMapping("/getuserext")
	public XcUserExt getUserext(@RequestParam("username") String username) {
		return userService.getUserExt(username);
	}
}
