package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcComparyUserRepostory;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/14 - 9:11
 */
@Service
public class UserService {
	@Autowired
	XcUserRepository xcUserRepository;
	@Autowired
	XcComparyUserRepostory xcComparyUserRepostory;
	@Autowired
	XcMenuMapper xcMenuMapper;

	/**
	 * @功能: 根据账号查询xcUser信息
	 * @作者: 高志红
	 */
	public XcUser findXcUserByIsername(String username) {
		return xcUserRepository.findByUsername(username);
	}

	/**
	 * @功能: 根据账号查询用户信息
	 * @作者: 高志红
	 */
	public XcUserExt getUserExt(String username) {
		// 根据账号查询xcUser信息
		XcUser xcUser = this.findXcUserByIsername(username);
		if (xcUser == null) {
			return null;
		}
		// 用户ID
		String userId = xcUser.getId();
		// 查询用户的所有权限
		List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
		// 根据用户ID查询用户所属公司ID
		XcCompanyUser xcCompanyUser = xcComparyUserRepostory.findByUserId(userId);
		// 取到用户公司ID
		String companyId = null;
		if (xcCompanyUser != null) {
			companyId = xcCompanyUser.getCompanyId();
		}
		XcUserExt xcUserExt = new XcUserExt();
		BeanUtils.copyProperties(xcUser, xcUserExt);
		xcUserExt.setCompanyId(companyId);
		// 设置权限
		xcUserExt.setPermissions(xcMenus);
		return xcUserExt;
	}
}
