package com.doosan.sb.test;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.dao.employee.EmployeRepository;
import com.doosan.sb.dao.employee.EmployeeQueryRepository;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class EmployeeDaoUnitTest {
	
	@Test
	public void testSave(){
		Tb_Employee employee = new Tb_Employee();
		employee.setAddress("ShanDong DC");
		employee.setAge(22);
		employee.setGender("M");
		employee.setName("Guoqian");
		employee.setTelphone("13465235623");
		employeRepository.save(employee);
	}
	@Test
	public void testQuery(){
		long sum = employeRepository.count();
		System.out.println("Recorde size is : " + sum);
		List<Tb_Employee> all = employeRepository.findAll();
		for(Tb_Employee e : all){
			System.out.println("Employee name is : " + e.getName());
			System.out.println("Employee age is : " + e.getAge());
			System.out.println("Employee address is : " + e.getAddress());
			System.out.println("-------------------------------------------");
		}
	}
	@Test
	@Transactional	//启用事务操作;@Transactional和@Test一起使用的时候,事务会自动回滚
	@Rollback(false)//禁止事务自动回滚
	public void testUpdate(){
		employeeQueryRepository.updateEmployeeById("changing the address", 5);
	}
	
	@Test
	public void testDelete(){
		Tb_Employee employee = employeRepository.findOne(2);
		employeRepository.delete(employee);
		System.out.println("Employee has been deleted...");
	}
	
	@Test
	public void testFindByName(){
		List<Tb_Employee> employees = employeeQueryRepository.findByName("Harry");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
		System.out.println("---------------Now query using @Query:HQL---------------");
		employees = employeeQueryRepository.queryByNameHQL("Tom");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
		System.out.println("---------------Now query using @Query:SQL---------------");
		employees = employeeQueryRepository.queryByNameSQL("Cheng");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
	}
	@Test
	public void testFindByNameAndGender(){
		List<Tb_Employee> employees = employeeQueryRepository.findByNameAndGender("Harry", "F");
		if(employees.size() != 0)
			for(Tb_Employee e : employees){
				System.out.println("Employee address : " + e.getAddress());
			}
		else
			System.out.println("No data finded...");
	}
	@Test
	/**
	 * 查询需要加上查询关键词
	 * %:匹配多个字符
	 * _:匹配单个字符
	 */
	public void testFindByTelphoneLike(){
		List<Tb_Employee> employees = employeeQueryRepository.findByTelphoneLike("%158%");
		if(employees.size() != 0)
			for(Tb_Employee e : employees){
				System.out.println("Employee address : " + e.getAddress());
			}
		else
			System.out.println("No data finded...");
	}
	
	@Autowired
	private EmployeRepository employeRepository;
	@Autowired
	private EmployeeQueryRepository employeeQueryRepository;
}