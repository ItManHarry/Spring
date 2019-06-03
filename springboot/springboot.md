# SpringBoot maven工程创建

## 新建maven工程

- eclipse创建新maven工程,pom配置文件：
	
```xml
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	  <properties>
		<java.version>1.8</java.version>
	  </properties>
	  <parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.7.RELEASE</version>
	  </parent>
	  <groupId>doosan.web.sb</groupId>
	  <artifactId>doosan-spring-boot</artifactId>
	  <version>0.0.1-SNAPSHOT</version>
	  <name>SpringBoot</name>
	  <description>Doosan SpringBoot Project</description>
	  <dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	  </dependencies>
	</project>
```
- 编写controller

```java
	package com.doosan.sb.start;
	import java.util.HashMap;
	import java.util.Map;
	import org.springframework.stereotype.Controller;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.ResponseBody;
	import org.springframework.web.bind.annotation.RestController;
	//@Controller
	@RestController //此注解代替 @Controller + @ResponseBody
	@RequestMapping("/starter")
	public class HelloController {

		@RequestMapping("/hello")
		//@ResponseBody
		public Map<String, Object> helle(){
			result.put("name", "Harry");
			result.put("age", 36);
			return result;
		}
		
		private Map<String, Object> result = new HashMap<String, Object>();
	}
```

- 编写启动器

```java
	package com.doosan.sb;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.web.servlet.ServletComponentScan;
	/**
	 * web工程启动器
	 */
	@SpringBootApplication
	@ServletComponentScan	//SpringBoot扫描@WebServlet注解对应的类
	public class ApplicationStarter {
		
		public static void main(String[] args){
			SpringApplication.run(ApplicationStarter.class, args);
		}
		
	}
```


## Spring Boot整合Servlet, Filter, Listener

- 方式一:启动器中增加@ServletComponentScan注解，用以扫描Servlet、Filter、Listener类

	- Spring Boot整合Filter	

```java
	package com.doosan.sb.servlet;
	import java.io.IOException;
	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	/**
	 * SpringBoot使用Servlet
	 */
	@WebServlet(name="userservlet",urlPatterns="/servlet/HelloServlet")	//声明该类为Servlet程序
	/**
	 * 等同于web.xml配置
	 *<servlet>
	 * 	<servlet-name>userservlet</servlet-name>
	 *	<servlet-class>com.doosan.sb.servlet.ServletUsage</servlet-class>
	 *</servlet>
	 *<servlet-mapping>
	 *	<servlet-name>userservlet</servlet-name>
	 *	<url-pattern>/servlet/HelloServlet</url-pattern>
	 *</servlet-mapping>
	 */
	public class ServletUsage extends HttpServlet{

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {			
			System.out.println("SpringBoot use the servlet successfully...");
		}
		
		private static final long serialVersionUID = 2083902449743154468L;
	}
```	
	
	- Spring Boot整合Filter

	使用@WebFilter注解编写Filter类，代码示例：
	
```java
	package com.doosan.sb.filter;
	import java.io.IOException;
	import javax.servlet.Filter;
	import javax.servlet.FilterChain;
	import javax.servlet.FilterConfig;
	import javax.servlet.ServletException;
	import javax.servlet.ServletRequest;
	import javax.servlet.ServletResponse;
	import javax.servlet.annotation.WebFilter;
	@WebFilter(filterName="helloFilter", urlPatterns="/servlet") //urlPatterns拦截地址
	public class FilterUsage implements Filter {

		@Override
		public void destroy() {
			// TODO Auto-generated method stub

		}

		@Override
		public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
				throws IOException, ServletException {
			System.out.println("Executed the code before the method...");
			arg2.doFilter(arg0, arg1);
			System.out.println("Executed the code after the method...");
		}

		@Override
		public void init(FilterConfig arg0) throws ServletException {
			// TODO Auto-generated method stub

		}

	}
```
	- Spring Boot整合Listener

	使用@WebListener注解编写Listener类，代码示例：
	
```java
	package com.doosan.sb.listener;
	import javax.servlet.ServletContextEvent;
	import javax.servlet.ServletContextListener;
	import javax.servlet.annotation.WebListener;
	@WebListener
	public class ListenerUsage implements ServletContextListener {

		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			System.out.println("ServletContext object destroyed...");
		}

		@Override
		public void contextInitialized(ServletContextEvent arg0) {
			System.out.println("ServletContext object created...");
		}
	}
```

- 方式二:启动器中增加@Bean注解的方法加载对应的Servlet、Filter、Listener类

```java
	package com.doosan.sb;
	import javax.servlet.ServletRegistration;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.web.servlet.FilterRegistrationBean;
	import org.springframework.boot.web.servlet.ServletComponentScan;
	import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
	import org.springframework.boot.web.servlet.ServletRegistrationBean;
	import org.springframework.context.annotation.Bean;
	import com.doosan.sb.filter.FilterUsage;
	import com.doosan.sb.listener.ListenerUsage;
	import com.doosan.sb.servlet.ServletUsage;
	/**
	 * web工程启动器
	 */
	@SpringBootApplication
	//@ServletComponentScan	//SpringBoot扫描@WebServlet注解对应的类
	public class ApplicationStarter {
		
		public static void main(String[] args){
			SpringApplication.run(ApplicationStarter.class, args);
		}
		/**
		 * 注册Servlet	
		 * @return
		 */
		@Bean
		public ServletRegistrationBean getServletRegistrationBean(){
			ServletRegistrationBean bean = new ServletRegistrationBean(new ServletUsage());
			bean.addUrlMappings("/servlet/HelloServlet");
			return bean;
		}
		/**
		 * 注册Filter	
		 * @return
		 */
		@Bean
		public FilterRegistrationBean getFilterRegistrationBean(){
			FilterRegistrationBean bean = new FilterRegistrationBean(new FilterUsage());
			bean.addUrlPatterns("/servlet/HelloServlet");
			return bean;
		}
		/**
		 * 注册Listener	
		 * @return
		 */
		@Bean
		public ServletListenerRegistrationBean<ListenerUsage> getServletListenerRegistrationBean(){
			ServletListenerRegistrationBean<ListenerUsage> bean = new ServletListenerRegistrationBean<ListenerUsage>(new ListenerUsage());
			return bean;
		}
	}
```

## SpringBoot使用静态文件

- 在resource下创建"static"文件夹，这下面的所有的资源即可通过地址栏直接进行访问了。

## SpringBoot文件上传

- 上传代码

```java
	package com.doosan.sb.controller.upload
	import org.springframework.stereotype.Controller
	import org.springframework.web.bind.annotation.RequestMapping
	import org.springframework.web.bind.annotation.RequestParam
	import org.springframework.web.bind.annotation.RestController
	import org.springframework.web.multipart.MultipartFile
	@RestController
	@RequestMapping("/file")
	class UploadController {
		@RequestMapping("/doUpload")
		//@RequestParam中的参数对应前端的file的name属性,也可不使用此注解，直接将form中的input的name属性改为"file"
		def upload(@RequestParam("attach")MultipartFile file){
			println "File Name : " + file.getOriginalFilename()
			println "File type : " + file.getContentType()
			//上传路径
			file.transferTo(new File("c:/" + file.getOriginalFilename()))
			result.put("result", "success")
			return result
		}	
		
		def result = [:]
	}
```

- 修改文件上传大小限制

	1.在resource 下新增一配置文件，application.properties
	
	2.设置属性:
		spring.http.multipart.maxFileSize=100MB		//单个文件大小设置
		spring.http.multipart.maxRequestSize=200MB	//单个请求总文件大小
		
## SpringBoot视图层(Freemarker,jsp,thymeleaf)

- Freemarker

	1.pom.xml文件中导入Freemarker依赖包
	
```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-freemarker</artifactId>
	</dependency>
```

	2.编写controller
	
```java
	package com.doosan.sb.controller.view
	import org.springframework.stereotype.Controller
	import org.springframework.ui.Model
	import org.springframework.web.bind.annotation.RequestMapping
	import com.doosan.sb.beans.Users
	@Controller
	class FreemarkerController {
		@RequestMapping("/freemarker/list")
		def toList(Model model){
			def list = []
			list << new Users("name":"Harry", "age":20)
			list << new Users("name":"Tom", "age":23)
			list << new Users("name":"Jack", "age":22)
			//跳转到list.ftl页面
			model.addAttribute("list", list)
			return "view/freemarker/list"
		}
	}
```

	3.建立list.ftl模板页面，在resource目录下创建templates目录存放所有的模板文件，再创建子目录/view/freemarker
	
	4.编写freemarker模板文件(list.ftl)
	
```
	<html>
		<title>Freemarker to list the Users</title>
		<meta charset = "utf-8">
		<body>
			<h3>Users List</h3>
			<table>
				<tr>
					<th>Name</th>
					<th>Age</th>
				</tr>
				<#list list as users>
					<tr>
						<td>${users.name}</td>
						<td>${users.age}</td>
					</tr>
				</#list>
			</table>
		</body>
	</html>
```

- Jsp

	1.pom.xml文件中导入jsp依赖包
	
```xml
	<dependency>
		<groupId>javax.servlet</groupId>
		<artifactId>jstl</artifactId>
	</dependency>
	<dependency>
		<groupId>org.apache.tomcat.embed</groupId>
		<artifactId>tomcat-embed-jasper</artifactId>
		<scope>provided</scope>
	</dependency>
```	

	2.在application.properties文件增加视图解析配置
	
```
	spring.mvc.view.prefix=/WEB-INF/jsp/
	spring.mvc.view.suffix=.jsp
```
	
	3.编写controller
	
```java
	package com.doosan.sb.controller.view
	import org.springframework.stereotype.Controller
	import org.springframework.ui.Model
	import org.springframework.web.bind.annotation.RequestMapping
	import com.doosan.sb.beans.Users
	@Controller
	class JspController {
		@RequestMapping("/jsp/list")
		def toList(Map map){
			def list = []
			list << new Users("name":"Harry", "age":20)
			list << new Users("name":"Tom", "age":23)
			list << new Users("name":"Jack", "age":22)
			//跳转到list.jsp页面
			map.put("list", list)
			return "users/list"
		}
	}
```

	4.在src/main下新增webapp文件夹，在webapp下新增WEB-INF,在WEB-INF新增jsp文件夹，编写jsp文件即可
	
- Thymeleaf

	1.pom.xml文件中导入Freemarker依赖包
	
```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-thymeleaf</artifactId>
	</dependency>
```

	2.编写controller
	
```java
	package com.doosan.sb.controller.view
	import org.springframework.stereotype.Controller
	import org.springframework.ui.Model
	import org.springframework.web.bind.annotation.RequestMapping
	import com.doosan.sb.beans.Users
	@Controller
	class ThymeleafController {
		@RequestMapping("/thymeleaf/list")
		def toList(Map map){
			def list = []
			list << new Users("name":"Harry", "age":20)
			list << new Users("name":"Tom", "age":23)
			list << new Users("name":"Jack", "age":22)
			//跳转到list.jsp页面
			map.put("list", list)
			map.put("message", "Hello Thymeleaf")
			//跳转至list.html文件
			return "thymeleaf/list"
		}
	}
```

	3.在"resource/templates/thymeleaf"下新增list.html文件
	
```html
	<!DOCTYPE html>
	<html lang="en">
		<head>
			<title>Thymeleaf Page</title>
		</head>
		<body>
			<h1 th:text = "${message}"></h1>
		</body>
	</html>
```

	注：报某个标签元素为未终止，原因是Thymeleaf3版本以下有这个校验，解决方法将thymeleaf升级到3.0以上，修改pom.xml文件：

```
	<properties>
		<java.version>1.8</java.version>
		<thymeleaf.version>3.0.2.RELEASE</thymeleaf.version>
		<thymeleaf-layout-dialect.version>2.0.4</thymeleaf-layout-dialect.version>
	</properties>
```	

	4.thymeleaf引入css&js文件
	
	
```html
	<!DOCTYPE html>
	<html lang="en">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
			<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
			<title>Thymeleaf Page</title>
			<link rel = "stylesheet" th:href="@{/css/bootstrap.min.css}" href = "../../../static/css/bootstrap.min.css" media="screen"/>
			<script type = "text/javascript" th:src="@{/js/bootstrap.min.js}" src = "../../../static/js/bootstrap.min.js" charset="UTF-8"></script>
			<script type = "text/javascript" th:src="@{/js/jquery-1.11.3.min.js}" src = "../../../static/js/jquery-1.11.3.min.js" charset="UTF-8"></script>
			<script type = "text/javascript" th:src="@{/js/vue.js}" src = "../../../static/js/vue.js" charset="UTF-8"></script>
		</head>
		<body>
			<div class="container-fluid" id = "contentdiv"> 
				<h1 th:text = "${message}"></h1>
				<hr/>
				<div class = "row">
					<div class = "col-sm-6 col-sm-offset-6 text-right">
						<div class="input-group">
							<input type="text" name = "no" id = "no" placeholder = "决裁单号......" class="form-control input-sm" v-model = "searchParam"/>
							<span class="input-group-btn">
								<button class="btn btn-default btn-sm" type="button" id = "search" @click = "doSearch"><i class = "glyphicon glyphicon-search"></i>&nbsp;查找</button>
							</span>
						</div>
					</div> 
				</div>
			</div>
			<script>
				var vm = new Vue({
					el:"#contentdiv",
					data:{
						searchParam:""					
					},
					methods:{
						doSearch:function(){
							alert(this.searchParam)
						}
					}
				})
			</script>
		</body>
	</html>
```


## Thymeleaf语法

- 变量输出

```html
	<h1 th:text = "${name}"></h1>
```	

- 条件判断

	1.th:if
	
```html
	<div class = "row">
		<div class = "col-sm-12" th:if = "${gender} == 'M'">
			<h3>The gender is Male</h3>
		</div> 
		<div class = "col-sm-12" th:if = "${gender} == 'F'">
			<h3>The gender is Female</h3>
		</div> 
	</div>
```
	2.th:switch
	
```html
	<div class = "row">
		<div class = "col-sm-12" th:switch = "${grade}">
			<h3 th:case = "1">The first grade</h3>
			<h3 th:case = "2">The second grade</h3>
			<h3 th:case = "3">The third grade</h3>
			<h3 th:case = "4">The forth grade</h3>
		</div> 
	</div>
```

- 迭代循环

	th:each
	
```html
	<div class = "col-sm-12">
		<table>
			<tr>
				<th>Name</th>
				<th>Age</th>
			</tr>
			<tr th:each = "user : ${list}">
				<td th:text = "${user.name}"></td>
				<td th:text = "${user.age}"></td>
			</tr>
		</table>
	</div> 
```

- 域对象

```java
	package com.doosan.sb.controller.view
	import javax.servlet.http.HttpServletRequest
	import org.springframework.stereotype.Controller
	import org.springframework.ui.Model
	import org.springframework.web.bind.annotation.RequestMapping
	import com.doosan.sb.beans.Users
	@Controller
	class ThymeleafController {
		@RequestMapping("/thymeleaf/list")
		def toList(HttpServletRequest request, Map map){
			def list = []
			list << new Users("name":"Harry", "age":20)
			list << new Users("name":"Tom", "age":23)
			list << new Users("name":"Jack", "age":22)
			//跳转到list.jsp页面
			map.put("list", list)					//对应前端th:each
			map.put("message", "Hello Thymeleaf")	//对应前端th:text
			map.put("gender", "M")					//对应前端th:if
			map.put("grade", "3")					//对应前端th:switch...th:case...
			//request域数据
			request.setAttribute("requestdata", "Request data.")
			//session域数据
			request.getSession().setAttribute("sessiondata", "Sesssion data.")
			//application域数据
			request.getSession().getServletContext().setAttribute("applicationdata", "Application data.")
			//跳转至list.html文件
			return "view/thymeleaf/list"
		}
	}
```

```html
	<div class = "row">
		<div class = "col-sm-12">
			<h3 th:text = "${#httpServletRequest.getAttribute('requestdata')}">The first grade</h3>
			<h3 th:text = "${session.sessiondata}">The second grade</h3>
			<h3 th:text = "${application.applicationdata}">The third grade</h3>
		</div> 
	</div>
```

- 链接语法

```html
	<a th:href = "@{~/demo1}"></a>
	<a th:href = "@{~/demo1(id=1,name=Harry)}"></a>
```

## SpringBoot整合mybatis

- pom.xml增加数据库链接、mybatis启动器

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
    <!-- 集成mybatis启动器 -->
	<dependency>
		<groupId>org.mybatis.spring.boot</groupId>
		<artifactId>mybatis-spring-boot-starter</artifactId>
		<version>1.1.1</version>
	</dependency>
```

- 配置mysql数据源，配置application.properties文件

```
	spring.datasource.driverClassName=com.mysql.jdbc.Driver
	spring.datasource.url=jdbc:mysql://localhost:3306/fsdb
	spring.datasource.username=fs
	spring.datasource.password=fileShare2017
	spring.datasource.type=com.alibaba.druid.pool.DruidDataSource	//连接池类型
	mybatis.type-aliases-package=com.doosan.sb.dao.domain			//实体表扫描包
```