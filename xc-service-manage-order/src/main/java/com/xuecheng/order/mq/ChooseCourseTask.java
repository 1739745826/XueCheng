package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/16 - 9:31
 */
@Component
public class ChooseCourseTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

	@Autowired
	TaskService taskService;

	/**
	 * @功能: 定时发送选课任务
	 * @作者: 高志红
	 */
	// @Scheduled(cron = "0 0/1 * * * *")
	@Scheduled(cron = "0/10 * * * * *")
	public void sendChoosecourseTask() {
		// 得到一分钟之前的时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		Date time = calendar.getTime();
		List<XcTask> xcTaskList = taskService.findXcTaskList(time, 10);
		System.out.println(xcTaskList);
		// 调用service发布消息，将添加选课的任务发给mq
		for (XcTask xcTask : xcTaskList) {
			if (taskService.getTask(xcTask.getId(), xcTask.getVersion()) > 0) {
				String mqExchange = xcTask.getMqExchange();
				String mqRoutingkey = xcTask.getMqRoutingkey();
				taskService.publish(xcTask, mqExchange, mqRoutingkey);
			}
		}
	}

	/**
	 * @功能: 接收选课响应结果
	 * @作者: 高志红
	*/
	@RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
	public void receiveFinishChoosecourseTask(XcTask xcTask) {
		if (xcTask != null && !StringUtils.isEmpty(xcTask.getId())){
			taskService.finishTask(xcTask.getId());
		}
	}

	/**
	 * @功能: 定义任务调度策略
	 * @作者: 高志红
	 */
	/*@Scheduled(cron = "0/3 * * * * *") // 每隔三秒执行
	public void task1() {
		LOGGER.error("======== 测试1开始 ========");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOGGER.error("======== 测试1结束 ========");
	}*/
}
