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