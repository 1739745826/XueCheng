package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.CourseLearningService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/16 - 14:59
 */
@Component
public class ChooseCourseTask {
	@Autowired
	CourseLearningService courseLearningService;
	@Autowired
	RabbitTemplate rabbitTemplate;

	@RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
	public void receiveChoosecourseTask(XcTask xcTask) {
		// 取到消息的内容
		String requestBody = xcTask.getRequestBody();
		Map map = JSON.parseObject(requestBody, Map.class);
		String userId = (String) map.get("userId");
		String courseId = (String) map.get("courseId");
		String valid = (String) map.get("valid");
		Date startTime = null;
		Date endTime = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY‐MM‐dd HH:mm:ss");
		try {
			if (map.get("startTime") != null) {
				startTime = dateFormat.parse((String) map.get("startTime"));
			}
			if (map.get("endTime") != null) {
				endTime = dateFormat.parse((String) map.get("endTime"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 添加选课
		ResponseResult responseResult = courseLearningService.addCourse(userId, courseId, valid, startTime, endTime, xcTask);
		if (responseResult.isSuccess()) {
			// 添加选课成功，要向mq发送添加选课成功的消息
			rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE, RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY, xcTask);
		}
	}
}
