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
| 1 | 秒 | 0 ~ 59 | , - * / |
| 2 | 分 | 0 ~ 59 | , - * / |
| 3 | 时 | 0 ~ 23 | , - * / |
| 4 | 日 | 1 ~ 31 | , - * / L W C |
| 5 | 月 | 0 ~ 12 | , - * / |
| 6 | 星期 | 1 ~ 7 | , - * /  L C #|
| 7 | 年(可选) | 1970 ~ 2099 | , - * / |

	特殊字符说明如下：

- 星号(*):可用于所有的字段中，表示对应时间域的每一个时刻，例如：*在分钟字段时，表示"每分钟"

- 问号(?):该字符只用于日期和星期两个字段，它通常指定为"无意义的值"，相当于占位符

- 横杠(-):表示的是范围，如在小时字段中"10-12",表示从10点到12点，即：10,11，12

- 逗号(,):表示的是枚举值，如在星期字段中使用"MON,WED,FRI",则表示星期一、星期三、星期五

- 斜杠(/):x/y表示一个等步长序列，x表示起始值，y表示增量步长。如在秒钟字段使用0/15,则表示0,15,30,45秒执行，而分钟5/15，则表示5,20,35,50，也可使用\*\/y,等同于0\/y

- L:只用在日期和星期两个字段中，表示Last的意思，在日期字段表示一个月的最后一天，在星期字段表示星期六

- W:改字符只出现在日期字段中，是对前导日期的修饰，表示离该日期最近的工作日，如15W表示离该月15号最近的工作日

- LW组合：在日期字段中表示当月最后一个工作日

- 井号(#):该字符只在星期字段中使用，表示当月的某个工作日。如6#3表示当月的第三个星期五，而4#5表示第五个星期三，如果当月没有第五个星期三，忽略不触发

- C:该字符只在日期和星期字段中使用，代表"Calendar"的意思。它的意思是计划所关联的日期，如果日期没有被关联，则相当于日历中的日期。例如：5C在日期字段中就相当于日历5日以后的第一天，1C在星期字段中相当于星期日后的第一天

## 方式二：集成quartz框架

- 导入quartz包(pom.xml)

```xml
	<dependency>
		<groupId>org.quartz-scheduler</groupId>
		<artifactId>quartz</artifactId>
		<version>2.2.1</version>
	</dependency>
```

- 编写job类，实现Job接口

```java
	package com.doosan.sb.schedule;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import org.quartz.Job;
	import org.quartz.JobExecutionContext;
	import org.quartz.JobExecutionException;

	public class QuartzJobForPrint implements Job {

		@Override
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			// TODO Auto-generated method stub
			System.out.println("Quartz job has been executed..." + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
	}
```

- 编写任务调度代码

```java
	package com.doosan.sb.schedule;
	import org.quartz.JobBuilder;
	import org.quartz.JobDetail;
	import org.quartz.Scheduler;
	import org.quartz.SimpleScheduleBuilder;
	import org.quartz.Trigger;
	import org.quartz.TriggerBuilder;
	import org.quartz.impl.StdSchedulerFactory;

	public class QuartzMain {
		
		public static void main(String[] args) throws Exception{
			//创建job对象
			JobDetail job = JobBuilder.newJob(QuartzJobForPrint.class).build();
			//创建trigger
			/**
			 * 1.简单trigger : 简单重复
			 * 2.cron trigger : 按照cron表达式重复
			 */
			Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5)).build();
			//创建schedule对象
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(job, trigger);
			//启动定时任务
			scheduler.start();
		}
	}
```

- 实现SpringBoot整合quartz

	1.编写配置类
	
```java
	package com.doosan.sb.schedule;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
	import org.springframework.scheduling.quartz.JobDetailFactoryBean;
	import org.springframework.scheduling.quartz.SchedulerFactoryBean;
	import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
	@Configuration
	public class QuartzConfig {
		
		//创建job对象
		@Bean
		public  JobDetailFactoryBean getJobDetailFactoryBean(){
			JobDetailFactoryBean factory =  new JobDetailFactoryBean();
			factory.setJobClass(QuartzJobForPrint.class);
			return factory;
		}
		//创建trigger
	//	@Bean
	//	public SimpleTriggerFactoryBean getSimpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
	//		SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
	//		factory.setJobDetail(jobDetailFactoryBean.getObject());
	//		//设置间隔时间(单位统一为毫秒)
	//		factory.setRepeatInterval(5000);
	//		//设置循环次数
	//		factory.setRepeatCount(4);
	//		return factory;		
	//	}
		//创建trigger
		@Bean
		public CronTriggerFactoryBean getCronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
			CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
			factory.setJobDetail(jobDetailFactoryBean.getObject());
			factory.setCronExpression("10 * 9 * * ?");
			return factory;		
		}
		//创建schedule对象
		@Bean
		public SchedulerFactoryBean getSchedulerFactoryBean(CronTriggerFactoryBean triggerFactoryBean, QuartzAdaptableJobFactory jobFactory){
			SchedulerFactoryBean factory = new SchedulerFactoryBean();
			//关联trigger
			factory.setTriggers(triggerFactoryBean.getObject());	
			//重写JobFactory
			factory.setJobFactory(jobFactory);
			return factory;
		}	
	}
```

- Job对象无法注入Spring容器对象的问题

	解决方法：重写AdaptableJobFactory类，代码如下：
	
```java
	package com.doosan.sb.schedule;
	import org.quartz.spi.TriggerFiredBundle;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
	import org.springframework.scheduling.quartz.AdaptableJobFactory;
	import org.springframework.stereotype.Component;
	@Component("jobFactory")
	public class QuartzAdaptableJobFactory extends AdaptableJobFactory {
		@Autowired
		private AutowireCapableBeanFactory factory;

		@Override
		protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
			//创建job对象
			Object jobInstance = super.createJobInstance(bundle);
			//放入Spring容器
			factory.autowireBean(jobInstance);
			return jobInstance;
		}
	}
```