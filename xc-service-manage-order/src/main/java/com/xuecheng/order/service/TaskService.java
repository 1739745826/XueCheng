package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/16 - 10:12
 */
@Service
public class TaskService {
	@Autowired
	XcTaskRepository xcTaskRepository;
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	XcTaskHisRepository xcTaskHisRepository;

	/**
	 * @功能: 查询前n条记录
	 * @作者: 高志红
	 */
	public List<XcTask> findXcTaskList(Date date, int n) {
		// 设置分页参数
		Pageable pageable = new PageRequest(0, n);
		Page<XcTask> all = xcTaskRepository.findByUpdateTimeBefore(pageable, date);
		if (all == null || all.getSize() <= 0) {
			return null;
		}
		return all.getContent();
	}

	/**
	 * @功能: 发布消息
	 * @作者: 高志红
	 */
	@Transactional
	public void publish(XcTask xcTask, String ex, String routingKey) {
		Optional<XcTask> xcTaskOptional = xcTaskRepository.findById(xcTask.getId());
		if (xcTaskOptional.isPresent()) {
			rabbitTemplate.convertAndSend(ex, routingKey, xcTask);
			// 更新任务时间
			XcTask one = xcTaskOptional.get();
			one.setUpdateTime(new Date());
			xcTaskRepository.save(one);
		}
	}

	/**
	 * @功能: 获取任务
	 * @作者: 高志红
	 */
	@Transactional
	public int getTask(String id, int version) {
		// 通过乐观锁的方式更新数据库表，如果结果大于0，说明取到任务
		int count = xcTaskRepository.updateRaskCersion(id, version);
		return count;
	}

	/**
	 * @功能: 完成任务
	 * @作者: 高志红
	 */
	@Transactional
	public void finishTask(String taskId) {
		Optional<XcTask> xcTaskOptional = xcTaskRepository.findById(taskId);
		if (xcTaskOptional.isPresent()) {
			// 当前任务
			XcTask xcTask = xcTaskOptional.get();
			XcTaskHis xcTaskHis = new XcTaskHis();
			BeanUtils.copyProperties(xcTask, xcTaskHis);
			// 保存到历史任务
			xcTaskHisRepository.save(xcTaskHis);
			// 删除当前任务
			xcTaskRepository.delete(xcTask);
		}
	}
}
