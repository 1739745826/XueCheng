package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 9:22
 */
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient {
	/**
	 * @功能: 根据账号查询用户信息
	 * @作者: 高志红
	*/
	@GetMapping("/ucenter/getuserext")
	public XcUserExt getUserext(@RequestParam("username") String username);
}
