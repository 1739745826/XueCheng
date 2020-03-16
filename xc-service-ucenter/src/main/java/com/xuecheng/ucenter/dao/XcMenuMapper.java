package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/15 - 10:56
 */
@Mapper
@Component
public interface XcMenuMapper {
	/**
	 * @功能: 根据用户id查询用户的权限
	 * @作者: 高志红
	 */
	public List<XcMenu> selectPermissionByUserId(String userId);
}
