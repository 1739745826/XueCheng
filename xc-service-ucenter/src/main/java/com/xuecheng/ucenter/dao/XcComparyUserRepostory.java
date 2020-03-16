package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 9:01
 */
public interface XcComparyUserRepostory extends JpaRepository<XcCompanyUser, String> {
	/**
	 * @功能: 根据用户ID查询用户所属的公司的id
	 * @作者: 高志红
	*/
	XcCompanyUser findByUserId(String userId);
}
