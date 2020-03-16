package com.xuecheng.auth.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;


/**
 * 标题：消费者
 * 作者：何处是归程
 * 时间：2020/2/28 - 14:08
 */
public class Consumer01 {
	private static final String QUEUE = "HelloWorld";

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
			channel.queueDeclare(QUEUE, true, false, false, null);

			// 实现消费方法
			DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
				// 当接收到消息后此方法被调用

				/**
				 *
				 * @param consumerTag 消费者标签，用来标识消费者，在监听队列的时候设置
				 * @param envelope 信封 通过envelope可以获取很多属性
				 * @param properties 消息属性
				 * @param body 消息内容
				 * @throws IOException
				 */
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
			// String basicConsume(String queue, boolean autoAck, Consumer callback) ;
			/**
			 * 参数列表：
			 * 1. queue：队列名称
			 * 2. autoAck：自动回复，当消费者接收到消息后要告诉mq消息已接收，如果将此参数设置为true表示会自动回复mq，如果设置为false 就要通过编程实现回复
			 * 3. callback：消费方法，当消费者接收到消息要执行的方法
			 */
			channel.basicConsume(QUEUE, true, defaultConsumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
