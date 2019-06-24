package com.doosan.sb.schedule;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzMain {
	
	public static void main(String[] args) throws Exception{
//		//创建job对象
//		JobDetail job = JobBuilder.newJob(QuartzJobForPrint.class).build();
//		//创建trigger
//		/**
//		 * 1.简单trigger : 简单重复
//		 * 2.cron trigger : 按照cron表达式重复
//		 */
//		//Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5)).build();
//		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?")).build();
//		//创建schedule对象
//		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//		scheduler.scheduleJob(job, trigger);
//		//启动定时任务
//		scheduler.start();
	}
}