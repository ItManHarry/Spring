package com.doosan.sb.test;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.dao.employee.EmployeeJpaRepository;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class EmployeeDaoJpaTest {
	@Autowired
	private EmployeeJpaRepository employeeJpaRepository;
	
	//测试排序
	@Test
	public void testSorting(){
		//封装排序条件的对象
		Sort sort = new Sort(new Order(Direction.DESC, "tid"));
		List<Tb_Employee> employees = (List<Tb_Employee>)employeeJpaRepository.findAll(sort);
		for(Tb_Employee e : employees){
			System.out.println("Employee id is : " + e.getTid());
			System.out.println("Employee name is : " + e.getName());
			System.out.println("Employee age is : " + e.getAge());
			System.out.println("Employee address is : " + e.getAddress());
			System.out.println("-------------------------------------------");
		}
	}
	
	//测试分页
	@Test
	public void testPaging(){
		//封装排序条件的对象
		Sort sort = new Sort(new Order(Direction.DESC, "tid"));
		//分页参数封装：第一个参数：页数，第二个参数：每页条数，第三个：排序(可不用)
		Pageable pageable = new PageRequest(0, 5, sort);
		//Page用于封装查询后的结果
		Page<Tb_Employee> pageData = employeeJpaRepository.findAll(pageable);
		//总记录数
		System.out.println("Total elements : " + pageData.getTotalElements());
		System.out.println("Total pages : " + pageData.getTotalPages());
		//获取结果集
		List<Tb_Employee> employees = pageData.getContent();
		for(Tb_Employee e : employees){
			System.out.println("Employee id is : " + e.getTid());
			System.out.println("Employee name is : " + e.getName());
			System.out.println("Employee age is : " + e.getAge());
			System.out.println("Employee address is : " + e.getAddress());
			System.out.println("-------------------------------------------");
		}
	}
	
	//测试保存
	@Test
	public void testSave(){
		Tb_Employee employee = new Tb_Employee();
		employee.setAddress("ShanDong UUU");
		employee.setAge(23);
		employee.setGender("M");
		employee.setName("UUU");
		employee.setTelphone("15867876753");
		employeeJpaRepository.save(employee);
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
		employeeJpaRepository.save(employee);
	}
	//测试查询
	@Test
	public void testQuery(){
		long sum = employeeJpaRepository.count();
		System.out.println("Recorde size is : " + sum);
		//继承JpaRepository不再需要类型强转
		List<Tb_Employee> all = employeeJpaRepository.findAll();
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
		Tb_Employee employee = employeeJpaRepository.findOne(13);
		System.out.println("Employee has been found, employee name is : " + employee.getName());
	}
	//测试删除
	@Test
	public void testDelete(){
		employeeJpaRepository.delete(12);
		System.out.println("The item has been deleted...");
	}
	@Test
	public void testFindByName(){
		List<Tb_Employee> employees = employeeJpaRepository.findByName("Harry");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
		System.out.println("---------------Now query using @Query:HQL---------------");
		employees = employeeJpaRepository.queryByNameHQL("Tom");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
		System.out.println("---------------Now query using @Query:SQL---------------");
		employees = employeeJpaRepository.queryByNameSQL("Cheng");
		for(Tb_Employee e : employees){
			System.out.println("Employee address : " + e.getAddress());
		}
	}
	@Test
	public void testFindByNameAndGender(){
		List<Tb_Employee> employees = employeeJpaRepository.findByNameAndGender("Harry", "F");
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
		List<Tb_Employee> employees = employeeJpaRepository.findByTelphoneLike("%158%");
		if(employees.size() != 0)
			for(Tb_Employee e : employees){
				System.out.println("Employee address : " + e.getAddress());
			}
		else
			System.out.println("No data finded...");
	}
}