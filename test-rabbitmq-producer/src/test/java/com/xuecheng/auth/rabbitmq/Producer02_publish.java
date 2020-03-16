package com.xuecheng.auth.rabbitmq;

import com.rabbitmq.client.*;

/**
 * 标题：发布订阅工作模式
 * 作者：何处是归程
 * 时间：2020/2/28 - 15:30
 */
public class Producer02_publish {
	private static final String QUEUE_INFOM_EMAIL = "queue_inform_email";
	private static final String QUEUE_INFOM_SMS = "queue_inform_sms";
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
		Channel channel = null;
		try {
			connection = connectionFactory.newConnection();
			// 创建会话通道，生产者和mq服务所有通信都在channel通道中完成
			channel = connection.createChannel();
			// 声明队列
			channel.queueDeclare(QUEUE_INFOM_EMAIL, true, false, false, null);
			channel.queueDeclare(QUEUE_INFOM_SMS, true, false, false, null);
			// 声明交换机
			// AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, BuiltinExchangeType type);
			/**
			 * 参数列表：
			 * 1. exchange：交换机名称
			 * 2. type：交换机类型
			 *      FANOUT：对应发布订阅工作模式
			 *      DIRECT：对应路由工作模式
			 *      TOPIC：对应通配符工作模式
			 *      HEADERS：对应Header转发器工作模式
			 */
			channel.exchangeDeclare(EXCHANFE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
			// 交换机和队列绑定
			// Queue.BindOk queueBind(String queue, String exchange, String routingKey);
			/**
			 * 参数列表：
			 * 1. queue：队列名称
			 * 2. exchange：交换机名称
			 * 3. routingKey：路由key在发布订阅模式中设置为空串, 作用是交换机根据路由key的值将消息转发到指定的队列中在发布订阅模式中为空串
			 */
			channel.queueBind(QUEUE_INFOM_EMAIL, EXCHANFE_FANOUT_INFORM, "");
			channel.queueBind(QUEUE_INFOM_SMS, EXCHANFE_FANOUT_INFORM, "");
			// 发送消息：
			for (int i = 0; i < 5; i++) {
				channel.basicPublish(EXCHANFE_FANOUT_INFORM, "", null, "发布订阅模式".getBytes());
				System.out.println("send to mq");
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
