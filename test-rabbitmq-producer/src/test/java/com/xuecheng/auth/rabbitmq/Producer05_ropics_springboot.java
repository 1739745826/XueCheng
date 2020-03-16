package com.xuecheng.auth.rabbitmq;

import com.xuecheng.auth.rabbitmq.config.RabbitmConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/28 - 18:37
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05_ropics_springboot {
	@Autowired
	RabbitTemplate rabbitTemplate;

	@Test
	public void testSendEmail (){
		// void convertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor, CorrelationData correlationData);
		/**
		 * 参数：
		 * 1. exchange：交换机名称
		 * 2. routingKey：路由值
		 * 3. message：消息内容
		 */
	    rabbitTemplate.convertAndSend(RabbitmConfig.EXCHANFE_TOPICS_INFORM, "inform.email", "send email message to user".getBytes());
		System.out.println("已发送");
	}
}
