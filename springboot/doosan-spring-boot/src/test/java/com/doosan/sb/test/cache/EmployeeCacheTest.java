package com.doosan.sb.test.cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.service.employee.EmployeeService;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class EmployeeCacheTest {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Test
	public void testFindOne(){
		//第一次查询
		System.out.println(employeeService.findById(1));
		//第二次查询
		//缓存启用后,查询数据库只执行一次，第二次查询就是走的缓存,可从后台打印的HQL看出
		System.out.println(employeeService.findById(1));
	}
	@Test
	public void testFindByPage(){
		Pageable pageable = new PageRequest(0, 5);
		//第一次查询 - 查询数据库
		System.out.println(employeeService.findByPage(pageable).getTotalElements());
		//第二次查询 - 不走数据库查询,走缓存
		System.out.println(employeeService.findByPage(pageable).getTotalElements());
		//换页查询 - 查询数据库,之所以重新走数据库查询,主要是@Cacheable的key属性起了作用
		pageable = new PageRequest(1, 5);
		System.out.println(employeeService.findByPage(pageable).getTotalElements());
	}
	@Test
	public void testFindAll(){
		//第一次查询
		System.out.println(employeeService.findAll().size());
		//新增一个雇员
		Tb_Employee employee = new Tb_Employee();
		employee.setAddress("For cache test2");
		employee.setAge(25);
		employee.setGender("M");
		employee.setName("ForCache2");
		employee.setTelphone("13652645268");
		//如果在sava方法上不加@CacheEvict注解的话,二次查询的数据还是查的之前的缓存,造成数据不准确
		employeeService.save(employee);
		//第二次查询
		System.out.println(employeeService.findAll().size());
	}
}