package com.doosan.sb.schedule;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.service.employee.EmployeeService;

public class QuartzJobForPrint implements Job {
	@Autowired
	private EmployeeService employeeService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		Tb_Employee employee = new Tb_Employee();
		employee.setAddress("SD TT");
		employee.setAge(33);
		employee.setGender("F");
		employee.setName("Tomcat");
		employee.setTelphone("15856463746");
		employeeService.save(employee);
		System.out.println("Quartz job has been executed..." + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
}