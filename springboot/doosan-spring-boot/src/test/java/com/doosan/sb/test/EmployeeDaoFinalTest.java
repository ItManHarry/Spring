package com.doosan.sb.test;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.dao.employee.EmployeeDao;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class EmployeeDaoFinalTest {
	@Autowired
	private EmployeeDao employeeDao;
	
	//单条件查询
	@Test
	public void testQuery(){
		//Predicate:该对象用于封装条件
		Specification<Tb_Employee> spec = new Specification<Tb_Employee>(){
			/**
			 * Root<Tb_Employee>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *  
			 */
			public Predicate toPredicate(Root<Tb_Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				/**
				 * 第一个参数:查询的属性(需要用root进行查询)
				 * 第二参数:条件值
				 * 以下等同于"from Tb_Employee where name = ?"
				 */
				Predicate predicate = builder.equal(root.get("name"), "Harry");
				return predicate;
			}
		};	
		
		List<Tb_Employee> list = employeeDao.findAll(spec);
		for(Tb_Employee e : list){
			System.out.println("Employee name : " + e.getName() + " age : " + e.getAge());
		}
	}
	//多条件查询
	@Test
	public void testMultiQuery(){
		//Predicate:该对象用于封装条件
		Specification<Tb_Employee> spec = new Specification<Tb_Employee>(){
			/**
			 * Root<Tb_Employee>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *  
			 */
			public Predicate toPredicate(Root<Tb_Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// where name = ? and gender = ?
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("name"), "Harry"));
				predicates.add(builder.equal(root.get("gender"), "M"));
				Predicate[] predicateArray = new Predicate[predicates.size()];
				return builder.and(predicates.toArray(predicateArray));
			}
		};	
		
		List<Tb_Employee> list = employeeDao.findAll(spec);
		for(Tb_Employee e : list){
			System.out.println("Employee name : " + e.getName() + " age : " + e.getAge());
		}
	}
	//单条件模糊查询
	@Test
	public void testLikeQuery(){
		//Predicate:该对象用于封装条件
		Specification<Tb_Employee> spec = new Specification<Tb_Employee>(){
			/**
			 * Root<Tb_Employee>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *  
			 */
			public Predicate toPredicate(Root<Tb_Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				/**
				 * 第一个参数:查询的属性(需要用root进行查询)
				 * 第二参数:条件值
				 * 以下等同于"from Tb_Employee where name like ?"
				 */
				Predicate predicate = builder.like(root.get("name"), "%g%");
				return predicate;
			}
		};	
		
		List<Tb_Employee> list = employeeDao.findAll(spec);
		for(Tb_Employee e : list){
			System.out.println("Employee name : " + e.getName() + " age : " + e.getAge());
		}
	}
}