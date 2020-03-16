package com.xuecheng.api.config.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 8:54
 */
@Api(value = "用户中心", description = "用户中心管理")
public interface UcenterControllerApi {
	@ApiOperation("根据用户账号查询用户信息")
	public XcUserExt getUserext(String username);
}
