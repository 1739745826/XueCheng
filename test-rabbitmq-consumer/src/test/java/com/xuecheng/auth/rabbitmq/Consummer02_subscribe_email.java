package com.xuecheng.auth.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 标题：发布订阅工作模式
 * 作者：何处是归程
 * 时间：2020/2/28 - 15:47
 */
public class Consummer02_subscribe_email {
	private static final String QUEUE_INFOM_EMAIL = "queue_inform_email";
	private static final String EXCHANFE_FANOUT_INFORM = "exchange_fanout_inform";

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
		try {
			connection = connectionFactory.newConnection();
			// 创建会话通道，生产者和mq服务所有通信都在channel通道中完成
			Channel channel = connection.createChannel();
			// 监听队列
			channel.queueDeclare(QUEUE_INFOM_EMAIL, true, false, false, null);

			// 声明交换机
			channel.exchangeDeclare(EXCHANFE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
			// 交换机和队列绑定
			channel.queueBind(QUEUE_INFOM_EMAIL, EXCHANFE_FANOUT_INFORM, "");

			// 实现消费方法
			DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
				// 当接收到消息后此方法被调用
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					// 交换机
					String exchange = envelope.getExchange();
					// 消息ID
					long deliveryTag = envelope.getDeliveryTag();
					// 消息内容
					String message = new String(body, "utf-8");
					System.out.println("receive message");
				}
			};
			channel.basicConsume(QUEUE_INFOM_EMAIL, true, defaultConsumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
