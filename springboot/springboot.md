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
