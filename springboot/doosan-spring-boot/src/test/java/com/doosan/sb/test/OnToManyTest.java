package com.doosan.sb.test;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.department.DepartmentDao;
import com.doosan.sb.dao.domain.Tb_Department;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.dao.employee.EmployeeDao;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class OnToManyTest {

	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private DepartmentDao departmentDao;
	
	@Test
	public void testSave(){
		//save department
		Tb_Department department = new Tb_Department();
		department.setName("Department One");		
		//create a employee
		Tb_Employee employee = new Tb_Employee();
		employee.setName("XiaoHong");		
		//join department and employee
		employee.setDepartment(department);
		department.getEmployees().add(employee);
		employeeDao.save(employee);
	}
	
	@Test
	public void testQuery(){
		Tb_Employee employee = employeeDao.findOne(15);
		Tb_Department department = employee.getDepartment();
		System.out.println("Employee name is : " + employee.getName() + " , and department name is : " + department.getName());
	}
	
	@Test
	public void testSaveDepartment(){
//		Tb_Department department = new Tb_Department();
//		department.setName("Department Three");
//		departmentDao.save(department);
		Tb_Employee employee = employeeDao.findOne(11);
		employee.setName("EM-I");
//		employee.setDepartment(departmentDao.findOne(1));
		employee.setAddress("ShanDong I");
		employee.setAge(26);
		employee.setGender("F");
		employee.setTelphone("13767568909");
		employeeDao.save(employee);
	}
	@Test
	public void testQueryDepartment(){
		Tb_Department department = departmentDao.findOne(1);
		Iterator<Tb_Employee> it = department.getEmployees().iterator();
		while(it.hasNext()){
			Tb_Employee employee = it.next();
			System.out.println("Employee Id : " + employee.getTid() + ", and name is : " + employee.getName());
		}
	}
	@Test
	public void testSaveDepartmentEmployees(){
		Tb_Department department = new Tb_Department();
		department.setName("Department Seven");
//		Set<Tb_Employee> employees = new HashSet<Tb_Employee>();
//		employees.add(employeeDao.findOne(1));
//		employees.add(employeeDao.findOne(7));
//		employees.add(employeeDao.findOne(16));
//		department.setEmployees(employees);
		departmentDao.save(department);
		Tb_Employee employee = employeeDao.findOne(1);
		employee.setDepartment(department);
		employeeDao.save(employee);
	}
} 