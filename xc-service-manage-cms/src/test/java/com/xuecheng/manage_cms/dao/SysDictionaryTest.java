package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 14:25
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysDictionaryTest {
	@Autowired
	SysDicthinaryRespository sysDicthinaryRespository;

	@Test
	public void test (){
		SysDictionary byType = sysDicthinaryRespository.findBydType("200");
		System.out.println(byType);
	}
}
