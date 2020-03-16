package com.xuecheng.auth.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 标题：通配符模式
 * 作者：何处是归程
 * 时间：2020/2/28 - 16:24
 */
public class Producer04_topics {
	private static final String QUEUE_INFOM_EMAIL = "queue_inform_email";
	private static final String QUEUE_INFOM_SMS = "queue_inform_sms";
	private static final String EXCHANFE_TOPICS_INFORM = "exchange_topics_inform";
	private static final String ROUTINGKEY_EMAIL = "inform.#.email.#"; // inform.email
	private static final String ROUTINGKEY_SMS = "inform.#.sms.#";

	public static void main(String[] args) {
		// 通过连接工厂创建新的连接和mq建立连接
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("127.0.0.1");
		connectionFactory.setPort(5672); // 端口
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		// 设置虚拟机 一个mq可以设置多个虚拟机 每个虚拟机就相当于一个独立的mq
		connectionFactory.setVirtualHost("/");
		// 建立新连接
		Connection connection = null;
		Channel channel = null;
		try {
			connection = connectionFactory.newConnection();
			// 创建会话通道，生产者和mq服务所有通信都在channel通道中完成
			channel = connection.createChannel();
			// 声明队列
			channel.queueDeclare(QUEUE_INFOM_EMAIL, true, false, false, null);
			channel.queueDeclare(QUEUE_INFOM_SMS, true, false, false, null);
			// 声明交换机
			channel.exchangeDeclare(EXCHANFE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
			// 交换机和队列绑定
			channel.queueBind(QUEUE_INFOM_EMAIL, EXCHANFE_TOPICS_INFORM, ROUTINGKEY_EMAIL);
			channel.queueBind(QUEUE_INFOM_SMS, EXCHANFE_TOPICS_INFORM, ROUTINGKEY_SMS);
			// 发送消息：
			for (int i = 0; i < 5; i++) {
				// 发送消息的时候要指定routingKey
				channel.basicPublish(EXCHANFE_TOPICS_INFORM, "inform.email", null, "路由模式".getBytes());
				System.out.println("send to mq email");
			}
			for (int i = 0; i < 5; i++) {
				// 发送消息的时候要指定routingKey
				channel.basicPublish(EXCHANFE_TOPICS_INFORM, "inform.sms", null, "路由模式".getBytes());
				System.out.println("send to mq sms");
			}
			for (int i = 0; i < 5; i++) {
				// 发送消息的时候要指定routingKey
				channel.basicPublish(EXCHANFE_TOPICS_INFORM, "inform.email.sms", null, "路由模式".getBytes());
				System.out.println("send to mq sms");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
