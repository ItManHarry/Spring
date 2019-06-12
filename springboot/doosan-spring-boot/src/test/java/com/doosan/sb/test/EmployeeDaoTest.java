package com.doosan.sb.test;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.dao.employee.EmployeeCrudRepository;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class EmployeeDaoTest {
	
	//测试保存
	@Test
	public void testSave(){
		Tb_Employee employee = new Tb_Employee();
		employee.setAddress("ShanDong KKK");
		employee.setAge(22);
		employee.setGender("F");
		employee.setName("KKK");
		employee.setTelphone("13236748987");
		employeeCrudRepository.save(employee);
	}
	//测试修改
	@Test
	public void testModify(){
		Tb_Employee employee = new Tb_Employee();
		//执行更新也是调用接口的save方法,此处设置ID,接口即可自动识别是更新还是新增
		employee.setTid(10);
		employee.setAddress("ShanDong JJJJ");
		employee.setAge(25);
		employee.setGender("F");
		employee.setName("JJJJ");
		employee.setTelphone("13589790908");
		employeeCrudRepository.save(employee);
	}
	//测试查询
	@Test
	public void testQuery(){
		long sum = employeeCrudRepository.count();
		System.out.println("Recorde size is : " + sum);
		List<Tb_Employee> all = (List<Tb_Employee>)employeeCrudRepository.findAll();
		for(Tb_Employee e : all){
			System.out.println("Employee name is : " + e.getName());
			System.out.println("Employee age is : " + e.getAge());
			System.out.println("Employee address is : " + e.getAddress());
			System.out.println("-------------------------------------------");
		}
	}
	//测试查询单条数据
	@Test
	public void testQueryOne(){
		Tb_Employee employee = employeeCrudRepository.findOne(10);
		System.out.println("Employee has been found, employee name is : " + employee.getName());
	}
	//测试删除
	@Test
	public void testDelete(){
		employeeCrudRepository.delete(12);
		System.out.println("The item has been deleted...");
	}
	@Test
	public void testFindByName(){
		List<Tb_Employee> employees = employeeCrudRepository.findByName("Harry");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
		System.out.println("---------------Now query using @Query:HQL---------------");
		employees = employeeCrudRepository.queryByNameHQL("Tom");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
		System.out.println("---------------Now query using @Query:SQL---------------");
		employees = employeeCrudRepository.queryByNameSQL("Cheng");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
	}
	@Test
	public void testFindByNameAndGender(){
		List<Tb_Employee> employees = employeeCrudRepository.findByNameAndGender("Harry", "F");
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
		List<Tb_Employee> employees = employeeCrudRepository.findByTelphoneLike("%158%");
		if(employees.size() != 0)
			for(Tb_Employee e : employees){
				System.out.println("Employee address : " + e.getAddress());
			}
		else
			System.out.println("No data finded...");
	}
	
	@Autowired
	private EmployeeCrudRepository employeeCrudRepository;
}