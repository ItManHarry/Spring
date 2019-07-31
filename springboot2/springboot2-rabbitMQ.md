# Spring Boot2

## RebbitMQ安装

- Linux下安装

	1.准备
	
		1.1 yum install gcc glibc-devel make ncurses-devel openssl-devel xmlto
		1.2 yum intall perl -y
	
	2.
	
## SpringBoot集成RabbitMQ

- pom.xml文件引入RabbitMQ

```xml
	<!-- 启用RabbitMQ -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-amqp</artifactId>
	</dependency>
```

- 配置RabbitMQ(application.properties)

```
	#configure RabbitMQ
	spring.rabbitmq.host=localhost
	spring.rabbitmq.port=5672
	spring.rabbitmq.username=admin
	spring.rabbitmq.password=admin123
	spring.rabbitmq.virtual-host=/admin_host
```

- 编写配置类

```java
	package com.ch.sys.biz.system.configuration.rabbitmq;
	import org.springframework.amqp.core.Binding;
	import org.springframework.amqp.core.BindingBuilder;
	import org.springframework.amqp.core.FanoutExchange;
	import org.springframework.amqp.core.Queue;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	@Configuration
	public class RabbitMQFanoutConfig {
		
		//邮箱队列
		public static final String QUEUE_EMAIL = "fanout.queue.email";
		//短信队列
		public static final String QUEUE_SMS = "fanout.queue.sms";
		//交换机名称
		private final String EXCHANGE_NAME = "fanout.exchange";
		//定义邮件队列
		@Bean
		public Queue fanoutEmailQueue() {
			return new Queue(QUEUE_EMAIL);
		}
		//定义短信队列
		@Bean
		public Queue fanoutSmsQueue() {
			return new Queue(QUEUE_SMS);
		}
		//定义交换机
		@Bean
		public FanoutExchange fanoutExchange() {
			return new FanoutExchange(EXCHANGE_NAME);
		}
		//绑定队列和交换机
		@Bean
		public Binding bindingExchangeEmail() {
			return BindingBuilder.bind(fanoutEmailQueue()).to(fanoutExchange());
		}
		@Bean
		public Binding bindingExchangeSms() {
			return BindingBuilder.bind(fanoutSmsQueue()).to(fanoutExchange());
		}
	}
```

- 编写生成类

```java
	package com.ch.sys.biz.system.queue.producer;
	import org.springframework.amqp.rabbit.core.RabbitTemplate;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Component;
	@Component
	public class FanoutProducer {
		
		@Autowired
		private RabbitTemplate rabbitTemplate;
		
		public void sendRabbitMQ(String queue, String message) {
			System.out.println("Queue name is : " + queue + ", message is : " + message);
			rabbitTemplate.convertAndSend(queue, message);
		}
	}															
```

- 编写Controller

```groovy
	package com.ch.sys.biz.controller.system.rabbitmq
	import com.ch.sys.biz.system.configuration.rabbitmq.RabbitMQFanoutConfig
	import com.ch.sys.biz.system.queue.producer.FanoutProducer
	import org.springframework.beans.factory.annotation.Autowired
	import org.springframework.web.bind.annotation.GetMapping
	import org.springframework.web.bind.annotation.RequestMapping
	import org.springframework.web.bind.annotation.RestController
	@RestController
	@RequestMapping("/rabbit")
	class RabbitMQController {
		
		@Autowired
		FanoutProducer fanoutProducer;
		
		@GetMapping("/queue/email")
		def emailQueue() {
			def message = "This is the email queue. now : " + new Date()
			fanoutProducer.sendRabbitMQ(RabbitMQFanoutConfig.QUEUE_EMAIL, message)
			return "success"
		}
		
		@GetMapping("/queue/sms")
		def smsQueue() {
			def message = "This is the sms queue. now : " + new Date()
			fanoutProducer.sendRabbitMQ(RabbitMQFanoutConfig.QUEUE_SMS, message)
			return "success"
		}
	}
```

- 编写消费者类

```java
	package com.ch.sys.biz.system.queue.consumer;
	import org.springframework.amqp.rabbit.annotation.RabbitHandler;
	import org.springframework.amqp.rabbit.annotation.RabbitListener;
	import org.springframework.stereotype.Component;
	import com.ch.sys.biz.system.configuration.rabbitmq.RabbitMQFanoutConfig;
	@Component
	@RabbitListener(queues = RabbitMQFanoutConfig.QUEUE_EMAIL)
	public class EmailQueueConsumer {

		@RabbitHandler
		public void doEmailConsumer(String message) {
			System.out.println("==============>Email Queue Has Been Consumed. Message is : " + message);
		}
	}

	package com.ch.sys.biz.system.queue.consumer;
	import org.springframework.amqp.rabbit.annotation.RabbitHandler;
	import org.springframework.amqp.rabbit.annotation.RabbitListener;
	import org.springframework.stereotype.Component;
	import com.ch.sys.biz.system.configuration.rabbitmq.RabbitMQFanoutConfig;
	@Component
	@RabbitListener(queues = RabbitMQFanoutConfig.QUEUE_SMS)
	public class SmsQueueConsumer {

		@RabbitHandler
		public void doSmsConsumer(String message) {
			System.out.println("==============>SMS Queue Has Been Consumed. Message is : " + message);
		}
	}
```