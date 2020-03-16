package com.xuecheng.auth.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 标题：配置类
 * 作者：何处是归程
 * 时间：2020/2/28 - 18:15
 */
@Configuration
public class RabbitmConfig {
	public static final String QUEUE_INFOM_EMAIL = "queue_inform_email";
	public static final String QUEUE_INFOM_SMS = "queue_inform_sms";
	public static final String EXCHANFE_TOPICS_INFORM = "exchange_topics_inform";
	public static final String ROUTINGKEY_EMAIL = "inform.#.email.#"; // inform.email
	public static final String ROUTINGKEY_SMS = "inform.#.sms.#";

	// 声明交换机
	@Bean(EXCHANFE_TOPICS_INFORM)
	public Exchange EXCHANFE_TOPICS_INFORM() {
		return ExchangeBuilder.topicExchange(EXCHANFE_TOPICS_INFORM).durable(true).build();
	}


	// 声明队列
	@Bean(QUEUE_INFOM_EMAIL)
	public Queue QUEUE_INFOM_EMAIL() {
		return new Queue(QUEUE_INFOM_EMAIL);
	}

	// 声明队列
	@Bean(QUEUE_INFOM_SMS)
	public Queue QUEUE_INFOM_SMS() {
		return new Queue(QUEUE_INFOM_SMS);
	}

	// 绑定交换机队列
	@Bean
	public Binding BINDING_QUEUE_INFORM_EMAIL(
			@Qualifier(QUEUE_INFOM_EMAIL) Queue queue,
			@Qualifier(EXCHANFE_TOPICS_INFORM) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
	}

	@Bean
	public Binding BINDING_QUEUE_INFORM_SMS(
			@Qualifier(QUEUE_INFOM_SMS) Queue queue,
			@Qualifier(EXCHANFE_TOPICS_INFORM) Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();
	}
}
