package com.xuecheng.auth.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 标题：生产者
 * 作者：何处是归程
 * 时间：2020/2/28 - 13:37
 */
public class Producer01 {
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
		Channel channel = null;
		try {
			connection = connectionFactory.newConnection();
			// 创建会话通道，生产者和mq服务所有通信都在channel通道中完成
			channel = connection.createChannel();
			// 声明队列
			// Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) ;
			/**
			 * 参数明细：
			 * 1. queue：队列名称
			 * 2. durable：是否持久化，如果持久化，mq重启后队列还在
			 * 3. exclusive：是否独占连接， 队列只允许在该连接中访问，如果连接关闭队列自动删除， 可用于临时队列
			 * 4. autoDelete： 自动删除， 队列不再使用时自动删除此队列，如果将此参数exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
			 * 5. arguments：参数，可以设置一个队列的扩展参数，比如：可设置存活事件
			 */
			channel.queueDeclare(QUEUE, true, false, false, null);
			// 发送消息：
			// void basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body);
			/**
			 * 参数明细：
			 * 1. exchange：交换机，如果不指定，将使用mq的默认交换机
			 * 2. routingKey：路由key，交换机根据路由key来将消息转发到指定的消息队列，如果使用默认交换机，routingKey要设置为队列名称
			 * 3. props：消息的属性
			 * 4. body：消息内容
			 */
			channel.basicPublish("", QUEUE, null, "Hello Wrold 曹璐".getBytes());
			System.out.println("send to mq");
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
