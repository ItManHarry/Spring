package com.doosan.sb.schedule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	@Bean
	public SimpleTriggerFactoryBean getSimpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
		SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
		factory.setJobDetail(jobDetailFactoryBean.getObject());
		//设置间隔时间(单位统一为毫秒)
		factory.setRepeatInterval(5000);
		//设置循环次数
		factory.setRepeatCount(4);
		return factory;		
	}
	//创建schedule对象
	@Bean
	public SchedulerFactoryBean getSchedulerFactoryBean(SimpleTriggerFactoryBean triggerFactoryBean){
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setTriggers(triggerFactoryBean.getObject());
		return factory;
	}	
}