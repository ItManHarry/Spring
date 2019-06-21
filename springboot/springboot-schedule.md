# Spring Boot定时任务

## 方式一：@scheduled注解

- 编辑pom.xml，导入spring-context-support包

```xml
	<!-- spring自带任务调度 -->
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context-support</artifactId>
	</dependency>
```

- 编写调度类

```java
	package com.doosan.sb.schedule;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Component;
	/**
	 * 定时任务
	 */
	@Component
	public class SystemScheduleForPrint {

		@Scheduled(cron="0/3 * * * * ?")
		public void run(){
			System.out.println("Execute the schedule task : ("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+")");
		}
	}
```

- 启动类启动任务调度:@EnableScheduling

```java
	package com.doosan.sb;
	import javax.servlet.ServletRegistration;
	import org.mybatis.spring.annotation.MapperScan;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.web.servlet.FilterRegistrationBean;
	import org.springframework.boot.web.servlet.ServletComponentScan;
	import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
	import org.springframework.boot.web.servlet.ServletRegistrationBean;
	import org.springframework.cache.annotation.EnableCaching;
	import org.springframework.context.annotation.Bean;
	import org.springframework.scheduling.annotation.EnableScheduling;
	import com.doosan.sb.filter.FilterUsage;
	import com.doosan.sb.listener.ListenerUsage;
	import com.doosan.sb.servlet.ServletUsage;
	/**
	 * web工程启动器
	 */
	@SpringBootApplication
	//@ServletComponentScan	//SpringBoot扫描@WebServlet注解对应的类
	@MapperScan("com.doosan.sb.dao")	//MyBtis扫描
	@EnableCaching						//启用缓存
	@EnableScheduling					//启动定时任务
	public class ApplicationStarter {
		
		public static void main(String[] args){
			SpringApplication.run(ApplicationStarter.class, args);
		}
		/**
		 * 注册Servlet	
		 * @return
		 */
		@Bean
		public ServletRegistrationBean getServletRegistrationBean(){
			ServletRegistrationBean bean = new ServletRegistrationBean(new ServletUsage());
			bean.addUrlMappings("/servlet/HelloServlet");
			return bean;
		}
		/**
		 * 注册Filter	
		 * @return
		 */
		@Bean
		public FilterRegistrationBean getFilterRegistrationBean(){
			FilterRegistrationBean bean = new FilterRegistrationBean(new FilterUsage());
			bean.addUrlPatterns("/servlet/HelloServlet");
			return bean;
		}
		/**
		 * 注册Listener	
		 * @return
		 */
		@Bean
		public ServletListenerRegistrationBean<ListenerUsage> getServletListenerRegistrationBean(){
			ServletListenerRegistrationBean<ListenerUsage> bean = new ServletListenerRegistrationBean<ListenerUsage>(new ListenerUsage());
			return bean;
		}
	}
```

- @Scheduled(cron="0/3 * * * * ?")之cron属性详解

	cron是一个字符串，分为6个或7个值（中间使用空格分隔）: 从左到右依次：秒、分、时、日、月、星期、年（可选，一般不填）
	
| 位置 | 时间域名 | 允许值 | 允许的特殊字符 |
| --- | --- | --- | --- |
| 1 | 秒 | 0~59 | , - * / |

## 方式二：集成quartz框架