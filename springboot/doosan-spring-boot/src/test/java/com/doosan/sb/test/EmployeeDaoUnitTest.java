package com.doosan.sb.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.dao.employee.EmployeRepository;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class EmployeeDaoUnitTest {
	
	@Test
	public void testSave(){
		Tb_Employee employee = new Tb_Employee();
		employee.setAddress("Test Employee address");
		employee.setAge(36);
		employee.setGender("M");
		employee.setName("Harry");
		employee.setTelphone("13780924007");
		employeRepository.save(employee);
	}
	
	@Autowired
	private EmployeRepository employeRepository;
}