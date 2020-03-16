package com.xuecheng.order;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.dao.XcTaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/16 - 12:41
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskTest {
	@Autowired
	XcTaskRepository repository;

	@Test
	public void test (){
		// 设置分页参数
		Pageable pageable = new PageRequest(0, 100);
		Page<XcTask> byUpdateTimeBefore = repository.findByUpdateTimeBefore(pageable, new Date());
		System.out.println(byUpdateTimeBefore.getContent());
	}
}
