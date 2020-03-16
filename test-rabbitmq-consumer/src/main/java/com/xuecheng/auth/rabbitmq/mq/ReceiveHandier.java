package com.xuecheng.auth.rabbitmq.mq;

import com.xuecheng.auth.rabbitmq.config.RabbitmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.channels.Channel;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/28 - 18:54
 */
@Component
public class ReceiveHandier {
	@RabbitListener(queues = {RabbitmConfig.QUEUE_INFOM_EMAIL})
	public void send_email(String mes, Message message, Channel channel) {
		System.out.println("receive message is " + mes);
	}
}
