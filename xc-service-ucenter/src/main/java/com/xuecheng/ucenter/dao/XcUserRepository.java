package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 8:58
 */
public interface XcUserRepository extends JpaRepository<XcUser, String> {
	/**
	 * @功能: 根据账号来查询用户信息
	 * @作者: 高志红
	 */
	XcUser findByUsername(String username);
}
