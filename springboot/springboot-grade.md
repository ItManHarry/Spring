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