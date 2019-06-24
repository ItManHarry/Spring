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

	@Scheduled(cron="0 0/5 * * * ?")
	public void run(){
		System.out.println("Execute the schedule task : ("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+")");
	}
}