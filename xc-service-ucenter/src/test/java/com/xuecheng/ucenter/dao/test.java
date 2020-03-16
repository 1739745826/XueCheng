package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/15 - 11:40
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {
	@Autowired
	XcMenuMapper xcMenuMapper;

	@Test
	public void test (){
		List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId("49");
		System.out.println(xcMenus);
	}
}
