# Spring Boot进阶

## SpringBoot整合Junit进行单元测试

- pom.xml导入Junit包

```xml
	<!-- Junit单元测试 -->
  	<dependency>
  		<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-test</artifactId>
  	</dependency>
```

- 编写测试类(src/test/java)

```java
	package com.doosan.sb.test;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	import com.doosan.sb.dao.domain.SysUser;
	import com.doosan.sb.service.user.SysUserService;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class UserDaoUnitTest {
		
		@Test
		public void testSave(){
			SysUser user = new SysUser();
			user.setBg(3);
			user.setRoleid(3);
			user.setStatus(3);
			user.setTeamid(3);
			user.setUsercd("test001");
			user.setUsernm("Junit001");
			sysUserService.save(user);
		}
		
		@Autowired
		private SysUserService sysUserService;
	}
```

## Springloader热部署

- 方式一：SpringLoader

	1.插件方式使用
	
		1.1.配置pom.xml
	
```xml
	<!-- 安装SpringLoader插件 -->
	<plugin>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-maven-plugin</artifactId>
		<dependencies>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>springloaded</artifactId>
				<version>1.2.5.RELEASE</version>
			</dependency>
		</dependencies>
	</plugin>
```
		1.2.项目启动更改为maven启动方式，右击项目，"run as" -> "maven build",在Goals里输入："spring-boot:run",应用后，运行即可。
	
	总结：
		缺点一：springloader只能实现后台代码的热部署，但是对于前端的变化，无法起到热部署的作用。
		缺点二：程序启动后，后台启动了一个进程，需要手动还杀掉。即在eclipse中点击停止项目后，再次启动会报端口冲突错误。
		
	2.直接使用jar包的方式
	
		2.1.下载springloaded包
		
		2.2.将springloaded加入到工程
		
		2.3.右击项目，选择"Run configuration"，设置"Arguments" -> "VM Arguments"，输入以下参数：
		
			-javaagent:\lib\springloaded-1.2.5.RELEASE.jar-noverify
			
		2.4.配置完成后点击"run",  运行项目		
	
- 方式二：devtools

	1.1.导入devtools
	
```xml
	<!-- 导入devtools包 -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-devtools</artifactId>
		<optional>true</optional>
		<scope>true</scope>
	</dependency>
```

	1.2.启动程序即可，修改后台文件，项目会自动重启
	
## SpringBoot异常处理

- 自定义error页面

	SpringBoot已经默认提供了一套错误机制：把所有的后台错误发送到error请求，然后跳到了对应的error页面。
	自定义error页面：在templates下新建error.html文件即可
	
	注：该方式最为简单，但是不利于具体异常问题的排查

- @ExceptionHandler注解提供方法

	在控制层中编写异常处理代码：
	
```groovy
	@ExceptionHandler(value=[java.lang.ArithmeticException])
	ModelAndView handlerArithmeticException(Exception e){
		ModelAndView mv = new ModelAndView()
		mv.addObject("exception", e.toString())
		mv.setViewName("view/thymeleaf/user/arithmeticError")
		return mv
	}
	@ExceptionHandler(value=[java.lang.NullPointerException])
	ModelAndView handlerNullPointerException(Exception e){
		ModelAndView mv = new ModelAndView()
		mv.addObject("exception", e.toString())
		mv.setViewName("view/thymeleaf/user/nullPointerError")
		return mv
	}
```

	注：该方式可以捕获具体的异常，跳到具体的error页面，但是不是太通用

- @ControllerAdvice + @ExceptionHandler

	1.1.定义全局的Controller来处理异常
	
```groovy
	package com.doosan.sb.controller.exception
	import org.springframework.web.bind.annotation.ControllerAdvice
	import org.springframework.web.bind.annotation.ExceptionHandler
	import org.springframework.web.servlet.ModelAndView
	@ControllerAdvice
	class GlobalExceptionHandler {
		
		@ExceptionHandler(value=[java.lang.ArithmeticException])
		ModelAndView handlerArithmeticException(Exception e){
			ModelAndView mv = new ModelAndView()
			mv.addObject("exception", e.toString())
			mv.setViewName("view/thymeleaf/user/arithmeticError")
			return mv
		}
		@ExceptionHandler(value=[java.lang.NullPointerException])
		ModelAndView handlerNullPointerException(Exception e){
			ModelAndView mv = new ModelAndView()
			mv.addObject("exception", e.toString())
			mv.setViewName("view/thymeleaf/user/nullPointerError")
			return mv
		}
	}
```
	
	注：该方式可以捕获所有同类型的异常，推荐使用


- 配置SimpleMappingExceptionResolver类

	编写Resolver：
	
```groovy
	package com.doosan.sb.config
	import org.springframework.context.annotation.Bean
	import org.springframework.context.annotation.Configuration
	import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
	/**
	 * 配置全局异常页面跳转
	 */
	@Configuration
	class DoosanSimpleMappingExceptionResolver {
		
		@Bean
		SimpleMappingExceptionResolver getSimpleMappingExceptionResolver(){
			SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver()
			Properties mappings = new Properties()
			/**
			 * key:异常类型全名
			 * value:异常跳转页面地址
			 */
			mappings.put("java.lang.ArithmeticException", "view/thymeleaf/exception/arithmeticError")
			mappings.put("java.lang.NullPointerException", "view/thymeleaf/exception/nullPointerError")
			resolver.setExceptionMappings(mappings)
			return resolver
		}	
	}
```

	注：该方式在项目启动时即可加载，是方式三的优化方案
	
	
- 自定义HandlerExceptionResolver类

	编写程序实现HandlerExceptionResolver接口：

```groovy
	package com.doosan.sb.controller.exception
	import javax.servlet.http.HttpServletRequest
	import javax.servlet.http.HttpServletResponse
	import org.springframework.context.annotation.Configuration
	import org.springframework.web.servlet.HandlerExceptionResolver
	import org.springframework.web.servlet.ModelAndView
	@Configuration
	class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

		@Override
		public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
				Exception e) {
			ModelAndView mv = new ModelAndView()
			if(e instanceof ArithmeticException)
				mv.setViewName("view/thymeleaf/exception/arithmeticError")
			if(e instanceof NullPointerException)
				mv.setViewName("view/thymeleaf/exception/nullPointerError")
			mv.addObject("exception", e.toString())
			return mv
		}
	}
```

## SpringBoot表单验证

- 原理：使用的是hibernate-validate注解实现验证

- 编写代码:form表单字段对应的类

```java
	package com.doosan.sb.dao.domain;
	import org.hibernate.validator.constraints.NotBlank;
	public class SysUser {

		public int getTid() {
			return tid;
		}
		public void setTid(int tid) {
			this.tid = tid;
		}
		public int getRoleid() {
			return roleid;
		}
		public void setRoleid(int roleid) {
			this.roleid = roleid;
		}
		public int getTeamid() {
			return teamid;
		}
		public void setTeamid(int teamid) {
			this.teamid = teamid;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public int getBg() {
			return bg;
		}
		public void setBg(int bg) {
			this.bg = bg;
		}
		public String getUsercd() {
			return usercd;
		}
		public void setUsercd(String usercd) {
			this.usercd = usercd;
		}
		public String getUsernm() {
			return usernm;
		}
		public void setUsernm(String usernm) {
			this.usernm = usernm;
		}
		private int tid;
		@NotBlank(message="User code should not be blank!")
		private String usercd;
		@NotBlank(message="User name should not be blank!")
		private String usernm;
		private int roleid;
		private int teamid;
		private int status;
		private int bg;
	}
```

- 编写控制层代码

```groovy
	@RequestMapping("/save")
	def save(@Valid @ModelAttribute("user") SysUser user, BindingResult result, Map map){
		if(result.hasErrors()){
			println "The validation is not passed, user name or code is empty..."
			return add(user, map)
		}
		sysUserService.save(user)
		return "view/thymeleaf/user/success"
	}
```

- 前端页面修改

```html
	<div class = "col-sm-2">
		<input type="text" name = "usercd" id = "usercd" placeholder = "User Name......" class="form-control input-sm"/>
		<span class = "text-danger" th:errors = "${user.usercd}"></span>
	</div>
	<div class = "col-sm-2 text-right">
		<h5>User Code : </h5>
	</div> 
	<div class = "col-sm-2">
		<input type="text" name = "usernm" id = "usernm" placeholder = "User Code......" class="form-control input-sm"/>
		<span class = "text-danger" th:errors = "${user.usernm}"></span>
	</div> 
```

- 常用的验证注解

	1.@NotBlank：是否为空，自动去掉两边的空格
	
	2.@NotEmpty：是否为空，不会自动去掉两边的空格
	
	3.@Length：判断长度
	
	4.@Min：最小值
	
	5.@Max：最大值
	
	6.@Email：判断邮箱格式

## Spring JPA整合

- 配置pom.xml文件，导入JPA启动器及数据库连接驱动和连接池

```xml
	 <!-- 导入mysql -->
    <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <!-- 导入连接池 -->
    <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid</artifactId>
       <version>1.0.9</version>
    </dependency>
    <!-- spring jpa -->
  	<dependency>
  		<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-data-jpa</artifactId>
  	</dependency>
```
 - 配置数据库信息，在resources下配置application.properties文件
 
 ```
	spring.datasource.driverClassName=com.mysql.jdbc.Driver
	spring.datasource.url=jdbc:mysql://localhost:3306/fsdb
	spring.datasource.username=fs
	spring.datasource.password=fileShare2017
	spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
	mybatis.type-aliases-package=com.doosan.sb.dao.domain
	spring.jpa.hibernate.ddl-auto=update
	spring.jpa.show-sql=true
 ```
 
 - 创建实体表
 
 ```java
	package com.doosan.sb.dao.domain;
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.Table;
	/**
	 * 雇员表实体
	 */
	@Entity
	@Table(name="tb_employee")
	public class Tb_Employee {	
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="tid")
		private Integer tid;
		@Column(name="name")
		private String name;
		@Column(name="age")
		private Integer age;
		@Column(name="address")
		private String address;
		@Column(name="gender")
		private String gender;
		@Column(name="telphone")	
		private String telphone;
		
		public Integer getTid() {
			return tid;
		}
		public void setTid(Integer tid) {
			this.tid = tid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getTelphone() {
			return telphone;
		}
		public void setTelphone(String telphone) {
			this.telphone = telphone;
		}
	}
 ```
 
 - 编写DAO接口(继承JpaRepository接口)
 
 ```java
	package com.doosan.sb.dao.employee;
	import org.springframework.data.jpa.repository.JpaRepository;
	import com.doosan.sb.dao.domain.Tb_Employee;
	/**
	 * 参数一：映射的实体
	 * 参数二：实体对应的主键(ID)的类型
	 */
	public interface EmployeRepository extends JpaRepository<Tb_Employee, Integer> {

	}
 ```
 
 - 编程单元测试
 
 ```java
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
 ```

## Spring Data JPA提供的核心接口

- Repository接口使用

	1.提供基于方法名称命名查询
	
	1.1.编写接口：
	
```java
	package com.doosan.sb.dao.employee;
	import java.util.List;
	import org.springframework.data.repository.Repository;
	import com.doosan.sb.dao.domain.Tb_Employee;

	public interface EmployeeQueryRepository extends Repository<Tb_Employee, Integer> {
		//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
		List<Tb_Employee> findByName(String name);
		//根据姓名和性别查询
		List<Tb_Employee> findByNameAndGender(String name, String gender);
		//模糊查询
		List<Tb_Employee> findByTelphoneLike(String telphone);
	}
```

	1.2.编写单元测试

```java
	package com.doosan.sb.test;
	import java.util.List;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
		public void testUpdate(){
			
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
```
	
	2.提供基于@Query查询与修改
	
	编写接口方法：
	
```java
	package com.doosan.sb.dao.employee;
	import java.util.List;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.Repository;
	import com.doosan.sb.dao.domain.Tb_Employee;

	public interface EmployeeQueryRepository extends Repository<Tb_Employee, Integer> {
		//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
		List<Tb_Employee> findByName(String name);
		//根据姓名和性别查询
		List<Tb_Employee> findByNameAndGender(String name, String gender);
		//模糊查询
		List<Tb_Employee> findByTelphoneLike(String telphone);
		
		//使用@Query注解HQL查询
		@Query("from Tb_Employee where name = ?")
		List<Tb_Employee> queryByNameHQL(String name);
		//使用@Query注解SQL查询
		@Query(value="select * from Tb_Employee where name = ?",nativeQuery=true)
		List<Tb_Employee> queryByNameSQL(String name);
		@Query("update Tb_Employee set address = ? where tid = ?")
		@Modifying	//执行更新时,必须增加此注解
		void updateEmployeeById(String address, int id);
	}
```

	2.2.编写单元测试代码
	
```java
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
```

- CrudRepository

	该接口的作用：增删改查的基本操作。
	注：CrudRepository继承于Repository接口
	
	编写接口类:

```java
	package com.doosan.sb.dao.employee;
	import java.util.List;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.CrudRepository;
	import com.doosan.sb.dao.domain.Tb_Employee;

	public interface EmployeeCrudRepository extends CrudRepository<Tb_Employee, Integer> {
		//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
		List<Tb_Employee> findByName(String name);
		//根据姓名和性别查询
		List<Tb_Employee> findByNameAndGender(String name, String gender);
		//模糊查询
		List<Tb_Employee> findByTelphoneLike(String telphone);
		
		//使用@Query注解HQL查询
		@Query("from Tb_Employee where name = ?")
		List<Tb_Employee> queryByNameHQL(String name);
		//使用@Query注解SQL查询
		@Query(value="select * from Tb_Employee where name = ?",nativeQuery=true)
		List<Tb_Employee> queryByNameSQL(String name);
		@Query("update Tb_Employee set address = ? where tid = ?")
		@Modifying	//执行更新时,必须增加此注解
		void updateEmployeeById(String address, int id);
	}

```

	编写单元测试：

```java
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
```

- PagingAndSortingRepository

	PagingAndSortingRepository接口继承于CrudRepository接口
	
	编写接口方法：
	
```java
	package com.doosan.sb.dao.employee;
	import java.util.List;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.PagingAndSortingRepository;

	import com.doosan.sb.dao.domain.Tb_Employee;

	public interface EmployeePageAndSortingRepository extends PagingAndSortingRepository<Tb_Employee, Integer> {
		//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
		List<Tb_Employee> findByName(String name);
		//根据姓名和性别查询
		List<Tb_Employee> findByNameAndGender(String name, String gender);
		//模糊查询
		List<Tb_Employee> findByTelphoneLike(String telphone);
		
		//使用@Query注解HQL查询
		@Query("from Tb_Employee where name = ?")
		List<Tb_Employee> queryByNameHQL(String name);
		//使用@Query注解SQL查询
		@Query(value="select * from Tb_Employee where name = ?",nativeQuery=true)
		List<Tb_Employee> queryByNameSQL(String name);
		@Query("update Tb_Employee set address = ? where tid = ?")
		@Modifying	//执行更新时,必须增加此注解
		void updateEmployeeById(String address, int id);
	}

```

	编写单元测试代码：
	
```java
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
	import com.doosan.sb.dao.employee.EmployeePageAndSortingRepository;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class EmployeeDaoPageAndSortTest {
		@Autowired
		private EmployeePageAndSortingRepository employeePageAndSortingRepository;
		
		//测试排序
		@Test
		public void testSorting(){
			//封装排序条件的对象
			Sort sort = new Sort(new Order(Direction.DESC, "tid"));
			List<Tb_Employee> employees = (List<Tb_Employee>)employeePageAndSortingRepository.findAll(sort);
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
			Page<Tb_Employee> pageData = employeePageAndSortingRepository.findAll(pageable);
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
			employeePageAndSortingRepository.save(employee);
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
			employeePageAndSortingRepository.save(employee);
		}
		//测试查询
		@Test
		public void testQuery(){
			long sum = employeePageAndSortingRepository.count();
			System.out.println("Recorde size is : " + sum);
			List<Tb_Employee> all = (List<Tb_Employee>)employeePageAndSortingRepository.findAll();
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
			Tb_Employee employee = employeePageAndSortingRepository.findOne(13);
			System.out.println("Employee has been found, employee name is : " + employee.getName());
		}
		//测试删除
		@Test
		public void testDelete(){
			employeePageAndSortingRepository.delete(12);
			System.out.println("The item has been deleted...");
		}
		@Test
		public void testFindByName(){
			List<Tb_Employee> employees = employeePageAndSortingRepository.findByName("Harry");
			for(Tb_Employee e : employees){
				System.out.println("Employee address : " + e.getAddress());
			}
			System.out.println("---------------Now query using @Query:HQL---------------");
			employees = employeePageAndSortingRepository.queryByNameHQL("Tom");
			for(Tb_Employee e : employees){
				System.out.println("Employee address : " + e.getAddress());
			}
			System.out.println("---------------Now query using @Query:SQL---------------");
			employees = employeePageAndSortingRepository.queryByNameSQL("Cheng");
			for(Tb_Employee e : employees){
				System.out.println("Employee address : " + e.getAddress());
			}
		}
		@Test
		public void testFindByNameAndGender(){
			List<Tb_Employee> employees = employeePageAndSortingRepository.findByNameAndGender("Harry", "F");
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
			List<Tb_Employee> employees = employeePageAndSortingRepository.findByTelphoneLike("%158%");
			if(employees.size() != 0)
				for(Tb_Employee e : employees){
					System.out.println("Employee address : " + e.getAddress());
				}
			else
				System.out.println("No data finded...");
		}
	}
```

- JpaRepository

	接口继承于PagingAndSortingRepository,适配之前接口的方法:findAll返回结果不再需要进行类型强转了。
	实际开发中，通常继承该接口
	
	编写DAO接口:
	
```java
	package com.doosan.sb.dao.employee;
	import java.util.List;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	import com.doosan.sb.dao.domain.Tb_Employee;

	public interface EmployeeJpaRepository extends JpaRepository<Tb_Employee, Integer> {
		//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
		List<Tb_Employee> findByName(String name);
		//根据姓名和性别查询
		List<Tb_Employee> findByNameAndGender(String name, String gender);
		//模糊查询
		List<Tb_Employee> findByTelphoneLike(String telphone);
		
		//使用@Query注解HQL查询
		@Query("from Tb_Employee where name = ?")
		List<Tb_Employee> queryByNameHQL(String name);
		//使用@Query注解SQL查询
		@Query(value="select * from Tb_Employee where name = ?",nativeQuery=true)
		List<Tb_Employee> queryByNameSQL(String name);
		@Query("update Tb_Employee set address = ? where tid = ?")
		@Modifying	//执行更新时,必须增加此注解
		void updateEmployeeById(String address, int id);
	}
```

	编写单元测试代码：
	
```java
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
```	

- JpaSpecificationExecutor

	作用：用于(组合)条件查询，例如：条件+分页
	该接口是独立接口，顾实际开发中进行多个继承，即：继承JpaRepository + JpaSpecificationExecutor，以实现最强大的功能。

```java
	public interface EmployeeDao extends JpaRepository<Tb_Employee, Integer>, JpaSpecificationExecutor<Tb_Employee> {
		...
	}
```

	编写DAO接口：
	
```java
	package com.doosan.sb.dao.employee;
	import java.util.List;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	import com.doosan.sb.dao.domain.Tb_Employee;

	public interface EmployeeDao extends JpaRepository<Tb_Employee, Integer>, JpaSpecificationExecutor<Tb_Employee> {
		//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
		List<Tb_Employee> findByName(String name);
		//根据姓名和性别查询
		List<Tb_Employee> findByNameAndGender(String name, String gender);
		//模糊查询
		List<Tb_Employee> findByTelphoneLike(String telphone);	
		//使用@Query注解HQL查询
		@Query("from Tb_Employee where name = ?")
		List<Tb_Employee> queryByNameHQL(String name);
		//使用@Query注解SQL查询
		@Query(value="select * from Tb_Employee where name = ?",nativeQuery=true)
		List<Tb_Employee> queryByNameSQL(String name);
		@Query("update Tb_Employee set address = ? where tid = ?")
		@Modifying	//执行更新时,必须增加此注解
		void updateEmployeeById(String address, int id);
	}	
```

	编写单元测试代码：

```java
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
	}
```

- 一对多

	编写实体表 - 雇员表：

```java
	package com.doosan.sb.dao.domain;
	import javax.persistence.CascadeType;
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.JoinColumn;
	import javax.persistence.ManyToOne;
	import javax.persistence.Table;
	/**
	 * 雇员表实体
	 */
	@Entity
	@Table(name="tb_employee")
	public class Tb_Employee {	
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="tid")
		private Integer tid;
		@Column(name="name")
		private String name;
		@Column(name="age")
		private Integer age;
		@Column(name="address")
		private String address;
		@Column(name="gender")
		private String gender;
		@Column(name="telphone")	
		private String telphone;
		//多发关联一方,一个雇员属于一个部门
		//@ManyToOne(cascade=CascadeType.PERSIST)
		@ManyToOne
		//维护外键
		@JoinColumn(name="department")
		private Tb_Department department;
		
		public Integer getTid() {
			return tid;
		}
		public void setTid(Integer tid) {
			this.tid = tid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getTelphone() {
			return telphone;
		}
		public void setTelphone(String telphone) {
			this.telphone = telphone;
		}
		public Tb_Department getDepartment() {
			return department;
		}
		public void setDepartment(Tb_Department department) {
			this.department = department;
		}
	}
```

	编写实体表 - 部门表：
	
```java
	package com.doosan.sb.dao.domain;
	import java.util.HashSet;
	import java.util.Set;
	import javax.persistence.CascadeType;
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.FetchType;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.OneToMany;
	import javax.persistence.Table;
	/**
	 * 部门表实体
	 */
	@Entity
	@Table(name="tb_department")
	public class Tb_Department {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="tid")
		private Integer tid;
		@Column(name="name")
		private String name;
		//一方关联多发,一个部门有多个雇员
		//如果不配置fetch属性时,fetch属性值默认为lazy,执行查询部门获取雇员信息会报异常:
		/**
		 * org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: 
		 * com.doosan.sb.dao.domain.Tb_Department.employees, could not initialize proxy - no Session
		 * ...
		 */
		@OneToMany(mappedBy="department",fetch=FetchType.EAGER)
		private Set<Tb_Employee> employees = new HashSet<Tb_Employee>();
		
		public Integer getTid() {
			return tid;
		}
		public void setTid(Integer tid) {
			this.tid = tid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Set<Tb_Employee> getEmployees() {
			return employees;
		}
		public void setEmployees(Set<Tb_Employee> employees) {
			this.employees = employees;
		}	
	}
```

	编写单元测试代码：
	
```java
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
	public class OneToManyTest {

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
```

- 多对多

	编写实体表 - 用户表
	
```java
	package com.doosan.sb.dao.domain;
	import java.util.HashSet;
	import java.util.Set;
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.FetchType;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.JoinColumn;
	import javax.persistence.JoinTable;
	import javax.persistence.ManyToMany;
	import javax.persistence.Table;
	@Entity
	@Table(name="tb_user")
	public class Tb_User {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="tid")
		private Integer tid;
		@Column(name="name")
		private String name;
		@Column(name="password")
		private String password;
		//多对多,一个用户对应多个角色
		//@ManyToMany(cascade=CascadeType.PERSIST)
		@ManyToMany(fetch=FetchType.EAGER)
		//多对多的实现方式就是中间表方式实现
		@JoinTable(name="t_user_role",joinColumns=@JoinColumn(name="user_id"),inverseJoinColumns=@JoinColumn(name="role_id"))
		private Set<Tb_Role> roles = new HashSet<Tb_Role>();
		public Integer getTid() {
			return tid;
		}
		public void setTid(Integer tid) {
			this.tid = tid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public Set<Tb_Role> getRoles() {
			return roles;
		}
		public void setRoles(Set<Tb_Role> roles) {
			this.roles = roles;
		}
	}

```

	编写实体表 - 角色表
	
```java
	package com.doosan.sb.dao.domain;
	import java.util.HashSet;
	import java.util.Set;
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.FetchType;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.ManyToMany;
	import javax.persistence.Table;
	/**
	 * 角色表实体
	 */
	@Entity
	@Table(name="tb_role")
	public class Tb_Role {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="tid")
		private Integer tid;
		@Column(name="name")
		private String name;
		//多对多,一个角色对应多个用户
		@ManyToMany(mappedBy="roles",fetch=FetchType.EAGER)
		private Set<Tb_User> users = new HashSet<Tb_User>();
		
		public Set<Tb_User> getUsers() {
			return users;
		}
		public void setUsers(Set<Tb_User> users) {
			this.users = users;
		}
		public Integer getTid() {
			return tid;
		}
		public void setTid(Integer tid) {
			this.tid = tid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
```

	编写单元测试代码：
	
```java
	package com.doosan.sb.test;
	import java.util.Iterator;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	import com.doosan.sb.dao.domain.Tb_Role;
	import com.doosan.sb.dao.domain.Tb_User;
	import com.doosan.sb.dao.role.RoleDao;
	import com.doosan.sb.dao.user.UserDao;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class ManyToManyTest {

		@Autowired
		private UserDao userDao;
		@Autowired
		private RoleDao roleDao;
		
		@Test
		public void testSaveUserAndRole(){
			//Create user
			Tb_User user = new Tb_User();
			user.setName("Harry");
			user.setPassword("12345678");
			//Create role
			Tb_Role roleA = new Tb_Role();
			roleA.setName("RoleA");
			Tb_Role roleB = new Tb_Role();
			roleB.setName("RoleB");
			//connect user and role
			user.getRoles().add(roleA);
			user.getRoles().add(roleB);
			roleA.getUsers().add(user);
			roleB.getUsers().add(user);
			//save data
			userDao.save(user);
		}	
		@Test
		public void testSaveUser(){
			Tb_User user = new Tb_User();
			user.setName("UserD");
			user.setPassword("1234ABCD");
	//		user.getRoles().add(roleDao.findOne(1));
	//		user.getRoles().add(roleDao.findOne(2));
			userDao.save(user);
		}
		@Test
		public void testSaveRole(){
			Tb_Role role = new Tb_Role();
			role.setName("RoleD");
			userDao.findOne(5).getRoles().add(role);
			userDao.findOne(6).getRoles().add(role);
			role.getUsers().add(userDao.findOne(5));
			role.getUsers().add(userDao.findOne(6));
			roleDao.save(role);
		}
		@Test
		public void testUpdateUser(){
			Tb_User user = userDao.findOne(5);
	//		user.getRoles().clear();
			user.getRoles().add(roleDao.findOne(3));
			user.getRoles().add(roleDao.findOne(5));
			userDao.save(user);
		}
		@Test
		public void testQuery(){
			Tb_User user = userDao.findOne(5);
			Iterator<Tb_Role> roles = user.getRoles().iterator();
			while(roles.hasNext()){
				Tb_Role role = roles.next();
				System.out.println("User : " + user.getName() + ", role Name : " + role.getName());
			}
			Tb_Role role = roleDao.findOne(3);
			Iterator<Tb_User> users = role.getUsers().iterator();
			while(users.hasNext()){
				Tb_User u = users.next();
				System.out.println("Role : " + role.getName() + ", user name is : " + u.getName());
			}
		}
	} 
```