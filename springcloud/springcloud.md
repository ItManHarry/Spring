# 微服务快速入门

## Docker启动MySQL

- Linux下安装Docker

- 在Docker下运行MySQL服务

```
	docker run -di --name=mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 centos/mysql-57-centos7
```

## RPC和HTTP远程调用方式对比

- RPC ： Remote Produce Call远程过程调用。自定义数据格式，基于原生的TCP通讯，速度快，效率高。早期的webservice及dubbo就是RPC典型应用

- HTTP : HTTP其实是一种网络传输协议，基于TCP，规定了数据传输的格式。现在客户端浏览器和服务端通讯基本都是采用HTTP协议。也可以用以远程服务调用，确定就是消息封装臃肿。

- 目前热门的Rest风格，就可以通过http协议来实现。

	SpringCloud基于http协议实现。
	
## RestTemplate实现远程调用

- Spring提供了一个RestTemplate模板工具类，对基于http的客户端进行了封装，并且实现了对象与json的序列化和反序列化，非常方便


- 初始化RestTemplate

```java
	@bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
```

- 使用RestTemplate远程调用微服务

```java
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value= "/order", method = RequestMethod.GET)
	public User Order(){
		User user = restTemplate.getForObject("http://localhost:9001/user/1", User.class);
		return user;
	}
```

# SpringCloud

## Eureka

- Eureka是Netflix出品的用于服务注册和发现的工具，SpringCloud集成了Eureka，提供了开箱即用的api，有对应的Eureka服务端和客户端。

- Eureka负责管理、记录服务提供者的信息。服务调用者无需自己寻找服务，而是把需求告诉Eureka，Eureka会反馈符合要求的服务。

- 服务器提供和Eureka之间通过“心跳”机制进行监控，如果某个服务出现问题，Eureka会自动剔除对应得服务。

- 概括：Eureka实现了服务的自动注册、发现、状态监控。

## 搭建Eureka Server

- 创建一个新的模块

- 导入依赖

	- 父工程导入SpringCloud依赖
	
```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>
		<parent>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-parent</artifactId>
			<version>2.1.8.RELEASE</version>
			<relativePath/> <!-- lookup parent from repository -->
		</parent>
		<groupId>com.doosan.ms</groupId>
		<artifactId>dooms</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<packaging>pom</packaging>
		<name>dooms</name>
		<description>Demo project for Spring Boot</description>
		<properties>
			<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
			<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
			<java.version>1.8</java.version>
		</properties>
		<dependencies>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-web</artifactId>
			</dependency>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-devtools</artifactId>
				<scope>runtime</scope>
				<optional>true</optional>
			</dependency>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-test</artifactId>
				<scope>test</scope>
			</dependency>
		</dependencies>
		<!-- 定义SpringCloud版本 -->
		<repositories>
			<repository>
				<id>spring-snapshots</id>
				<name>Spring Snapshots</name>
				<url>https://repo.spring.io/snapshot</url>
				<snapshots>
					<enabled>true</enabled>
				</snapshots>
			</repository>
			<repository>
				<id>spring-milestones</id>
				<name>Spring Milestones</name>
				<url>https://repo.spring.io/milestone</url>
				<snapshots>
					<enabled>false</enabled>
				</snapshots>
			</repository>
		</repositories>
		<pluginRepositories>
			<pluginRepository>
				<id>spring-snapshots</id>
				<name>Spring Snapshots</name>
				<url>https://repo.spring.io/snapshot</url>
				<snapshots>
					<enabled>true</enabled>
				</snapshots>
			</pluginRepository>
			<pluginRepository>
				<id>spring-milestones</id>
				<name>Spring Milestones</name>
				<url>https://repo.spring.io/milestone</url>
				<snapshots>
					<enabled>false</enabled>
				</snapshots>
			</pluginRepository>
		</pluginRepositories>
		<!-- 锁定SpringCloud版本 -->
		<dependencyManagement>
			<dependencies>
				<dependency>
					<groupId>org.springframework.cloud</groupId>
					<artifactId>spring-cloud-dependencies</artifactId>
					<version>Greenwich.SR2</version>
					<type>pom</type>
					<scope>import</scope>
				</dependency>
			</dependencies>
		</dependencyManagement>
		<build>
			<plugins>
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
				</plugin>
			</plugins>
		</build>
		<modules>
			<module>ms_user</module>
			<module>ms_movie</module>
			<module>eureka-server</module>
		</modules>
	</project>
```
	
	- 子工程里导入Eureka Server的依赖
	
```xml
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	  <parent>
		<groupId>com.doosan.ms</groupId>
		<artifactId>dooms</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	  </parent>
	  <artifactId>eureka-server</artifactId>
	  <name>Eureka</name>
	  <description>Eureka Server</description>
	  <!-- Eureka服务端 -->
	  <dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
	  </dependencies>
	</project>
```
	
- 编写application.properties配置Eureka

```
	server.port=8888
	spring.application.name=eureka-server
	eureka.client.fetch-registry=false
	eureka.client.register-with-eureka=false
	#register url
	eureka.client.service-url.defaultZone=http://127.0.0.1:${server.port}/eureka
```

- 编写启动类 ： 添加@EnableEurekaServer注解，启动Eureka服务。

```java
	package com.doosan.ms.eureka;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
	@SpringBootApplication
	@EnableEurekaServer //开启Eureka服务端自动配置
	public class EurekaServerApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(EurekaServerApplication.class, args);
		}
		
	}
```

- 启动子工程，访问网址：http://127.0.0.1:8888

## 搭建Eureka Client

- 对应工程里导入Eureka Client依赖

```xml
	<!-- 导入Eureka客户端 -->
	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  	</dependency>
```

- 在application.properties文件中配置Eureka Server

```
	server.port=9001
	spring.application.name=microservice-user
	spring.datasource.url=jdbc:mysql://10.40.123.215:3306/msdb?characterEncoding=UTF8
	spring.datasource.driver=com.mysql.jdbc.Driver
	spring.datasource.username=root
	spring.datasource.password=root2019
	spring.jpa.show-sql=true
	spring.jpa.generate-ddl=true
	spring.jpa.database=mysql
	eureka.client.fetch-registry=true
	eureka.client.register-with-eureka=true
	eureka.client.service-url.defaultZone=http://127.0.0.1:8888/eureka
	eureka.instance.prefer-ip-address=true 
```

- 启动类增加@EnableEurekaClient注解启动客户端

- 使用Eureka获取微服务

```java
	package com.doosan.ms.movie.controller;
	import java.util.List;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.cloud.client.ServiceInstance;
	import org.springframework.cloud.client.discovery.DiscoveryClient;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;
	import org.springframework.web.client.RestTemplate;
	import com.doosan.ms.movie.pojo.User;
	@RequestMapping("/movie")
	@RestController
	public class MovieController {
		
		@Autowired	
		private RestTemplate restTemplate;
		@Autowired
		private DiscoveryClient discoveryClient;
		/**
		 * restTemplate way
		 * @return
		 */
		@PostMapping("/order")
		public String order() {
			//模拟当前用户
			Integer id = 1;
			//远程调用用户微服务
			User user = restTemplate.getForObject("http://localhost:9001/user/find/"+id, User.class);
			
			//调用用户微服，获取用户具体信息
			System.out.println(user.getName() + " is buying the movie tickets...");
			
			return "Sale successfully";
		}
		
		/**
		 * Eureka way
		 * @return
		 */
		@PostMapping("/sale")
		public String sale() {
			//模拟当前用户
			Integer id = 1;
			//获取Eureka中的用户微服务 - 根据名称获取
			List<ServiceInstance> instances = discoveryClient.getInstances("microservice-user");
			//如果没有负载均衡，只能获取一个服务器
			ServiceInstance instance = instances.get(0);
			User user = restTemplate.getForObject("http://"+instance.getHost()+":"+instance.getPort()+"/user/find/"+id, User.class);
			//调用用户微服，获取用户具体信息
			System.out.println(user.getName() + " is buying the movie tickets...(Use eureka to get the serivce infomation)");
			return "Sale successfully";
		}
	}
```

## 搭建高可用Eureka Server

- Eureka服务端application.properties配置更改 - 服务端

	- Server1

```
	server.port=8888
	spring.application.name=eureka-server-1
	eureka.client.fetch-registry=true						#单机版为false
	eureka.client.register-with-eureka=true		#单机版位false
	#register url
	eureka.client.service-url.defaultZone=http://127.0.0.1:9999/eureka #把本服务注册到其他服务中
```

	- Server2

```
	server.port=9999
	spring.application.name=eureka-server-2
	eureka.client.fetch-registry=true						#单机版为false
	eureka.client.register-with-eureka=true		#单机版位false
	#register url
	eureka.client.service-url.defaultZone=http://127.0.0.1:8888/eureka #把本服务注册到其他服务中
```

- Eureka服务端application.properties配置更改 - 客户端

```
	eureka.client.fetch-registry=true
	eureka.client.register-with-eureka=true
	eureka.client.service-url.defaultZone=http://10.40.123.215:8888/eureka,http://10.40.123.215:9999/eureka	#配置多个Eureka服务端
	eureka.instance.prefer-ip-address=true
````

- 启动Eureka及各个微服务。


## Eureka配置详解

- 服务提供方

	1. 服务注册：
		
		eureka.client.register-with-eureka=true
	
	2. 服务续约：在注册服务完成后，服务提供者会定时想EurekaServer发起Rest请求，告诉EurekaServer：“我还活着”。这个我们称之为服务续约（renew）心跳，续约参数：
	
		eureka.instance.lease-expiration-duration-in-seconds=30	#服务失效时间，默认90秒
		eureka.instance.lease-renewal-interval-in-seconds=10			#续约间隔时间，默认30秒
		
- 服务调用方

	1. 获取服务注册信息：
		
		eureka.client.fetch-registry=true
		
	2. 默认30秒重新获取并更新注册信息。
	
		eureka.client.registry-fetch-interval-seconds=5
		
## Eureka Server失效剔除与自我保护

- 失效剔除

	默认情况下，Eureka Server每隔60秒对失效的服务（超过90秒未续约的服务）进行剔除，以下参数可以修改剔除时间：
	
	eureka.server.eviction-interval-timer-in-ms		#修改扫描失效服务间隔时间（单位是毫秒）
	eureka.server.enable-self-preservation:false		#取消自我保护（默认是true）
	
- 自我保护

	Eureka会统计15分钟心跳失败的服务实例的比例是否超过了15%。在生产环境下，因为网络延时原因，心跳失败实例的比例很可能超标，但是此时就把服务剔除并不妥当，
	因为服务可能没有宕机。Eureka就会把当前实例的注册信息保护起来，不予剔除。生产环境下非常有效，保证了大多数服务依然可用。
	
	
## Spring Cloud 服务调用与负载均衡

- Ribbon组件

	Ribbon是Netflix发布的负载均衡器，它有助于控制HTTP和TCP客户端的行为。为Ribbon配置服务提供者地址列表后，Ribbon就可以基于某种负载均衡算法，自动地帮助服务消费者去请求。
	Ribbon默认为我们提供了很多的负载均衡算法，例如轮询、随机等。我们也可以实现自定义的负载均衡算法。

- 方式一：RestTemplate + Ribbon

- 示例代码

```java
	/**
	 * Eureka way-使用Ribbon负载均衡
	 * @return
	 */
	//注入负载均衡客户端
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@PostMapping("/buy")
	public String buy() {
		//模拟当前用户
		Integer id = 1;
		//使用Ribbon选择合适的服务实例
		ServiceInstance instance = loadBalancerClient.choose("microservice-user");
		User user = restTemplate.getForObject("http://"+instance.getHost()+":"+instance.getPort()+"/user/find/"+id, User.class);
		//使用简化版-前提是启动类在RrestTemplate初始化的时候增加@LoadBalanced注解
		//User user = restTemplate.getForObject("http://microservice-user/user/find/"+id, User.class);
		//调用用户微服，获取用户具体信息
		System.out.println(user.getName() + " is buying the movie tickets...(Use eureka to get the serivce infomation)");
		return "Sale successfully";
	}
	
	
	package com.doosan.ms.movie;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.client.loadbalancer.LoadBalanced;
	import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
	import org.springframework.context.annotation.Bean;
	import org.springframework.web.client.RestTemplate;
	@SpringBootApplication
	@EnableEurekaClient
	public class MicroServiceMovieApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(MicroServiceMovieApplication.class, args);
		}
		
		/**
		 * 初始化RestTemplate
		 * @return
		 */
		@Bean
		@LoadBalanced
		public RestTemplate restTemplate(){
			return new RestTemplate();
		}
	}
```
- 修改负载均衡算法,修改配置文件application.properties

```
	serviceID(对应的服务名).ribbon.NFLocalBalanceRuleClassName=com.netflix.loadbalancer.RandomRule
```

- 方式二：OpenFeign + 内置Ribbon（推荐使用，简单易用）

- 导入OpenFeign包

```xml
	<!-- 导入OpenFeign -->
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-openfeign</artifactId>
  	</dependency>
```
- 编写代理接口(代理商接口使用@FeignClient指明调用的服务名)

```java
	package com.doosan.ms.movie.ofs;
	import org.springframework.cloud.openfeign.FeignClient;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import com.doosan.ms.movie.pojo.User;
	/**
	 * 注意事项
	 * 1.使用@FeignClient注解
	 * 2.检查@GetMapping的路径是否完整
	 * 3.@PathVariable的value一定不能省略
	 * @author 20112004
	 *
	 */
	@FeignClient("microservice-user")
	public interface UserServiceInf {
		
		@GetMapping("/user/find/{id}")
		public User getUserById(@PathVariable("id") Integer id) ;
		@GetMapping("/hr/biz/department/getAll")
		def getAll(@RequestParam("order") String order, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("name") String name)
		@GetMapping("/hr/biz/department/total")
		def total()
	}
```

- 开启OpenFeign客户端

```java
	package com.doosan.ms.movie;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.client.loadbalancer.LoadBalanced;
	import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
	import org.springframework.cloud.openfeign.EnableFeignClients;
	import org.springframework.context.annotation.Bean;
	import org.springframework.web.client.RestTemplate;
	@SpringBootApplication
	@EnableEurekaClient
	@EnableFeignClients //开启OpenFeign客户端
	public class MicroServiceMovieApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(MicroServiceMovieApplication.class, args);
		}
		
		/**
		 * 初始化RestTemplate
		 * @return
		 */
		@Bean
		@LoadBalanced
		public RestTemplate restTemplate(){
			return new RestTemplate();
		}
	}
```

- 调用代码

```java
	@Autowired
	private UserServiceInf usi;
	@PostMapping("/get")
	public String getTicket() {
		Integer id = 1;
		User user = usi.getUserById(id);
		//调用用户微服，获取用户具体信息
		System.out.println(user.getName() + " is buying the movie tickets...(Use OpenFeign to get the serivce infomation)");
		return "Get ticket successfully";
	}
```

## 熔断器

- 雪崩效应 

	在微服务架构中通常会有多个服务调用，基础服务的故障可能会导致级联故障，进而造成整个系统不可用的情况，这种现象被称之为雪崩效应。服务雪崩效应是一种因“服务提供者”的不
	可用导致“服务消费者”不可用，并将不可用逐渐放大的过程。
	
- 熔断器 - Hystrix

	Hystrix是Netflix开源的一个延迟和容错库，用于隔离访问远程服务、第三方库、防止出现级联失败（雪崩效应），放于调用方使用。
	
- Ribbon整合Hystrix
	
	- 在微服务调用方导入hystrix依赖
	
```xml
	<!-- 导入Hystrix -->
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-starter-netflix-hystrix</artifactId>
  	</dependency>
```

	
	- 使用@HystrixCommand声明fallback方法(声明位置：服务调用方法)
	
```java
	@PostMapping("/buy")
	@HystrixCommand(fallbackMethod =  "fallback")
	public String buy() {
		//模拟当前用户
		Integer id = 1;
		//使用Ribbon选择合适的服务实例
//		ServiceInstance instance = loadBalancerClient.choose("microservice-user");
//		User user = restTemplate.getForObject("http://"+instance.getHost()+":"+instance.getPort()+"/user/find/"+id, User.class);
		//使用简化版(地址栏直接使用微服名称)-前提是启动类在RrestTemplate初始化的时候增加@LoadBalanced注解
		User user = restTemplate.getForObject("http://microservice-user/user/find/"+id, User.class);
		//调用用户微服，获取用户具体信息
		System.out.println(user.getName() + " is buying the movie tickets...(Use eureka to get the serivce infomation)");
		return "Sale successfully";
	}
```
	
	- 编写fallback方法逻辑
	
```java
	/**
	 * 熔断器回滚方法
	 */
	public String fallback() {
		return "Service can not be used!";
	}
```
	
	- 在启动类添加@EnableHystrix注解
	
```java
	package com.doosan.ms.movie;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.client.loadbalancer.LoadBalanced;
	import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
	import org.springframework.cloud.netflix.hystrix.EnableHystrix;
	import org.springframework.cloud.openfeign.EnableFeignClients;
	import org.springframework.context.annotation.Bean;
	import org.springframework.web.client.RestTemplate;
	@SpringBootApplication
	@EnableEurekaClient
	@EnableFeignClients
	@EnableHystrix
	public class MicroServiceMovieApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(MicroServiceMovieApplication.class, args);
		}
		
		/**
		 * 初始化RestTemplate
		 * @return
		 */
		@Bean
		@LoadBalanced
		public RestTemplate restTemplate(){
			return new RestTemplate();
		}
	}
```
	
	


- OpenFeign整合Hystrix

	- 打开OpenFeign的Hystrix开关，修改application.properties配置文件
	
```
	feign.hystrix.enabled=true
```
	
	- 编写fallback处理类(实现熔断器接口)
	
```java
	package com.doosan.ms.movie.ofs.hystrix;
	import org.springframework.stereotype.Component;
	import com.doosan.ms.movie.ofs.UserServiceInf;
	import com.doosan.ms.movie.pojo.User;
	/**
	 * Feign熔断器类
	 * @author 20112004
	 *
	 */
	@Component
	public class UserServiceFallback implements UserServiceInf{

		@Override
		public User getUserById(Integer id) {
			System.out.println("UserService Hystrix...");
			return null;
		}

	}
```
	
	- 在FeignClient接口中指定对应的实现类
	
```java
	package com.doosan.ms.movie.ofs;
	import org.springframework.cloud.openfeign.FeignClient;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import com.doosan.ms.movie.ofs.hystrix.UserServiceFallback;
	import com.doosan.ms.movie.pojo.User;
	/**
	 * 注意事项
	 * 1.使用@FeignClient注解
	 * 2.检查@GetMapping的路径是否完整
	 * 3.@PathVariable的value一定不能省略
	 * @author 20112004
	 *
	 */
	@FeignClient(value = "microservice-user", fallback = UserServiceFallback.class)
	public interface UserServiceInf {
		
		@GetMapping("/user/find/{id}")
		public User getUserById(@PathVariable("id") Integer id) ;
	}
```
	
- 搭建Hystrix监控服务

	- 搭建Hystrix Dashboard工程(新建microservice)
	
		1. 导入Hystrix Dashboard的依赖
		
```xml
	<!-- 导入Hystrix -->
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
  	</dependency>
  	<!-- 导入Hystrix Dashboard -->
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
  	</dependency>
  	<!-- 导入web依赖 -->
  	<dependency>
  		<groupId>org.springframework.boot</groupId>
  		<artifactId>spring-boot-starter-actuator</artifactId>
  	</dependency>
```
		
		2. 启动类添加@EnableHystrixDashboard注解
		
```java
	package com.doosan.ms.hystrix;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
	@SpringBootApplication
	@EnableHystrixDashboard
	public class HystrixServerApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(HystrixServerApplication.class, args);
		}
		
	}
````

	- 启动工程，访问URL：
	
	http://localhost:1111/hystrix		

- 使用Hystrix监控服务消费者情况

	- 在服务消费方的启动类，添加监听方法的Servlet
	
```java
	package com.doosan.ms.movie;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.web.servlet.ServletRegistrationBean;
	import org.springframework.cloud.client.loadbalancer.LoadBalanced;
	import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
	import org.springframework.cloud.netflix.hystrix.EnableHystrix;
	import org.springframework.cloud.openfeign.EnableFeignClients;
	import org.springframework.context.annotation.Bean;
	import org.springframework.web.client.RestTemplate;

	import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
	@SpringBootApplication
	@EnableEurekaClient
	@EnableFeignClients
	@EnableHystrix
	public class MicroServiceMovieApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(MicroServiceMovieApplication.class, args);
		}
		
		/**
		 * 初始化RestTemplate
		 * @return
		 */
		@Bean
		@LoadBalanced
		public RestTemplate restTemplate(){
			return new RestTemplate();
		}
		
		@Bean
		public ServletRegistrationBean getServlet() {
			HystrixMetricsStreamServlet servlet = new HystrixMetricsStreamServlet();
			@SuppressWarnings("unchecked")
			ServletRegistrationBean registration = new ServletRegistrationBean(servlet);
			registration.setLoadOnStartup(1);
			registration.addUrlMappings("/hystrix.stream");
			registration.setName("HystrixMetricsStreamServlet");
			return registration;
		}
	}
```

	- 在Dashboard网址中输入以下监控地址：
	
		http://localhost:9002/hystrix.stream
		
## SpringCloud 网关

- 为什么需要网关（API Gateway）

	在微服务架构中，后端服务往往不直接开放给调用端，而是通过一个API网关根据请求的URL路由到相应的服务。
	
	1. 权限校验
	
	2. 负载均衡
	
- SpringCloud Zuul

	SpringCloud Zuul路由是微服务架构不可缺少的一部分，提供动态路由、监控、弹性、安全等边缘服务。
	
	Zuul的核心是一系列的过滤器，可以完成以下功能：
	
	1. 身份认证与安全
	
	2. 审查与监控
	
	3. 动态路由
	
	4. 压力测试
	
	5. 负责分配
	
	6. 静态响应处理
	
	7. 多区域弹性
	
	SpringCloud对Zuul进行了整合与增强。目前，Zuul默认使用的HTTP客户端是Apache HTTP Client，也可以使用RestClient或者OKHTTP3.0.
	如果要使用RestClient，可以设置：ribbon.restclient.enabled=true;
	如果要使用OKHTTP3.0，可以设置：ribbon.okhttp.enabled=true;
	
- 配置Zuul网关动态路由

	1. 创建独立的网关微服务模块：api-gateway
	
	2. 导入Zuul和Eureka的依赖(网关本身也要注册到Eureka) - pom.xml
	
```xml
	<!-- 导入Eureka客户端 -->
	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  	</dependency>
  	<!-- 导入Zuul -->
	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
  	</dependency>
```
	
	3. 启动类添加@EnableZuulProxy注解
	
```java
	package com.doosan.ms.gateway;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
	import springfox.documentation.swagger2.annotations.EnableSwagger2;
	@SpringBootApplication
	@EnableZuulProxy	//启用Zuul网关代理功能
	@EnableSwagger2		//开启Swagger2功能
	public class MicroServiceGatewayApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(MicroServiceGatewayApplication.class, args);
		}
	}
```
	
	4. 配置Zuul路由规则（application.properties）

```
	server.port=2222
	spring.application.name=microservice-gateway
	eureka.client.fetch-registry=true
	eureka.client.register-with-eureka=true
	eureka.client.service-url.defaultZone=http://10.40.123.215:8888/eureka,http://10.40.123.215:9999/eureka
	eureka.instance.prefer-ip-address=true
	#注：如果转发的路径(path)和转发的服务名称(serivceId)是一致的话，可以省略zuul的路由配置
	zuul.routes.microservice-user.path=/microservice-user
	zuul.routes.microservice-user.serviceId=microservice-user
	zuul.routes.microservice-movie.path=/microservice-movie
	zuul.routes.microservice-movie.serviceId=microservice-movie
```

注意：zuul网关工程启动报错“The bean 'proxyRequestHelper', defined in class path resource [org/springframework/cloud/netflix/zuul/ZuulProxyAutoConfiguration$NoActuatorConfiguration.class], could not be registered. A bean with that name has already been defined in class path resource”，
原因是springboot和springcloud的版本不符导致，springboot&springcloud版本对应参照：https://www.cnblogs.com/zhuwenjoyce/p/10261079.html

- Zuul网关负载均衡： Zuul负载均衡默认使用的也是Ribbon，默认策略也是轮询

- Zuul过滤器 - 方法说明

	1. filterType ： 该函数需要返回一个字符串来代表过滤器的类型，而这个类型就是在HTTP请求过程中定义的各个阶段。在Zuul中默认定义了四个不同的类型，具体如下：
	
		1.1. pre： 可以在请求被路由之前调用
		
		1.2. route：在路由请求时候调用
		
		1.3. post： 在routing和error过滤器之后被调用
		
		1.4. error： 处理请求发生错误是调用
		
	2. filterOrder：通过int值来定义过滤器执行的顺序，值越小优先级越高
	
	3. shouldFilter： 返回一个boolean类型来判断该过滤器是否要执行，可以通过该方法来指定过滤器的有效范围
	
	4. run： 过滤器的具体逻辑。我们可以实现自定义的过滤逻辑，来确定是否要拦截当前的请求，不对其进行后续的路由，或是在请求路由返回结果之后，对处理结果做一些加工。
	
- zuul自定义异常处理

	1. 配置Zuul默认的异常处理器失效(配置application.properties)
	
```
	zuul.SendErrorFilter.error.disable=true
```

	2. 编写自定义异常处理类型
	
		2.1. 处理结果result
```java
	package com.doosan.ms.gateway.filters.result;
	import java.io.Serializable;

	public class FilterResult implements Serializable {

		private static final long serialVersionUID = -7196773410439546025L;
		private boolean flag;
		private String message;
		
		public FilterResult() {}
		
		public FilterResult(boolean flag, String message) {
			super();
			this.flag = flag;
			this.message = message;
		}
		
		public boolean isFlag() {
			return flag;
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}	
````
	
	2.2. 处理过滤器
	
```java
	package com.doosan.ms.gateway.filters;
	import javax.servlet.http.HttpServletResponse;
	import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
	import org.springframework.stereotype.Component;
	import com.doosan.ms.gateway.filters.result.FilterResult;
	import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import com.netflix.zuul.ZuulFilter;
	import com.netflix.zuul.context.RequestContext;
	import com.netflix.zuul.exception.ZuulException;
	@Component
	public class SystemErrorFilter extends ZuulFilter {

		@Override
		public boolean shouldFilter() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Object run() throws ZuulException {
			System.out.println("Do the error exception...");
			//捕获异常信息
			RequestContext context = RequestContext.getCurrentContext();
			HttpServletResponse response = context.getResponse();
			ZuulException exception = (ZuulException)context.get("throwable");
			FilterResult result = new FilterResult(false, exception.getMessage());
			ObjectMapper om = new ObjectMapper();
			try {
				String json = om.writeValueAsString(result);
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return null;
		}

		@Override
		public String filterType() {
			// TODO Auto-generated method stub
			return FilterConstants.ERROR_TYPE;
		}

		@Override
		public int filterOrder() {
			// TODO Auto-generated method stub
			return 1;
		}
	}
```

- zuul整合Swagger2

	1. 导入swagger2依赖
	
```xml
	<!-- 导入Swagger2 -->
  	<dependency>
  		<groupId>io.springfox</groupId>
  		<artifactId>springfox-swagger2</artifactId>
  		<version>2.8.0</version>	
  	</dependency>
  	<dependency>
  		<groupId>io.springfox</groupId>
  		<artifactId>springfox-swagger-ui</artifactId>
  		<version>2.8.0</version>	
  	</dependency> 
```

	2. 编写组件（和启动类在同一路径下）
	
```java
	package com.doosan.ms.gateway;
	import java.util.ArrayList;
	import java.util.List;
	import org.springframework.cloud.netflix.zuul.filters.Route;
	import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
	import org.springframework.context.annotation.Primary;
	import org.springframework.stereotype.Component;
	import springfox.documentation.swagger.web.SwaggerResource;
	import springfox.documentation.swagger.web.SwaggerResourcesProvider;
	@Component
	@Primary
	public class DocumentationConfig implements SwaggerResourcesProvider{
		
		private final RouteLocator routeLocator;
		
		public DocumentationConfig(RouteLocator routeLocator) {
			this.routeLocator = routeLocator;
		}

		@Override
		public List<SwaggerResource> get() {
			List<SwaggerResource> resouces = new ArrayList<SwaggerResource>();
			List<Route> routes = routeLocator.getRoutes();
			routes.forEach(route -> {
				resouces.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "2.0"));
			});
			return null;
		}
		
		private SwaggerResource swaggerResource(String name, String location, String version) {
			SwaggerResource swaggerResource = new SwaggerResource();
			swaggerResource.setName(name);
			swaggerResource.setLocation(location);
			swaggerResource.setSwaggerVersion(version);
			return swaggerResource;
		}
	}
```

	3. 启动类开启swagger功能
	
```java
	package com.doosan.ms.gateway;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
	import springfox.documentation.swagger2.annotations.EnableSwagger2;
	@SpringBootApplication
	@EnableZuulProxy	//启用Zuul网关代理功能
	@EnableSwagger2		//开启Swagger2功能
	public class MicroServiceGatewayApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(MicroServiceGatewayApplication.class, args);
		}
	}

```

	4. 登录网址验证：
	
		http://127.0.0.1:2222/swagger-ui.html
		
## SpringCloudConfig配置中心

- 构筑config微服务

	1. 引入config依赖(pom.xml)
	
```
	<!-- 导入Configuration服务端 -->
	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-config-server</artifactId>
  	</dependency>
```

	2. 配置连接属性application.properties
	
```
	server.port=1001
	spring.application.name=microservice-config
	spring.cloud.config.server.git.uri=https://github.com/ItManHarry/config.git
```

	3. 编写启动类
	
```java
	package com.doosan.ms.config;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.cloud.config.server.EnableConfigServer;
	@SpringBootApplication
	@EnableConfigServer
	public class MicroServiceConfigApplication {
		
		public static void main(String[] args) {
			SpringApplication.run(MicroServiceConfigApplication.class, args);
		}
	}
```

- config客户端
	
	1. 引入config客户端依赖
	
```xml
	<!-- 导入config客户端 -->
	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-config</artifactId>
  	</dependency>
```

	2. 在resources下删除application.properties文件，新增bootstrap.properties文件，文件配置内容如下：
	
```
	spring.cloud.config.uri=http://127.0.0.1:1001	#springcloud config服务端地址
	spring.cloud.config.name=user								#配置文件前缀		
	spring.cloud.config.profile=dev								#配置文件后缀
	spring.cloud.config.label=master							#仓库分支名称
```

- 更改为svn

	1. 服务端：
	
		1.1. 导入svn客户端依赖
		
```xml
	<dependency>
  		<groupId>org.tmatesofte.svnkit</groupId>
  		<artifactId>svnkit</artifactId>
		<version>1.8.10</version>
  	</dependency>
```
	
		1.2. 修改配置文件application.properties：
	
```
	server.port=1001
	spring.application.name=microservice-config
	#spring.cloud.config.server.git.uri=https://github.com/ItManHarry/config.git
	spring.profiles.active=subversion						#启用SVN
	spring.cloud.config.server.svn.uri=https://10.40.128.191/svn/doosan-ms/	#SVN地址
	spring.cloud.config.server.svn.name=doosanms													#SVN账号
	spring.cloud.config.server.svn.password=doosanms											#SVN密码
	spring.cloud.config.server.svn.default-label=trunk												#SVN路径
```

	2. 客户端，修改配置文件bootstrap.properties
	
```
	spring.cloud.config.uri=http://127.0.0.1:1001
	spring.cloud.config.name=movie
	spring.cloud.config.profile=dev
	spring.cloud.config.label=trunk
```

- 搭建高可用的配置中心框架

	1. config要注册到Eureka
	
	2. 各个微服务启用自动发现配置功能(bootstrap.properties)

```
	spring.cloud.config.discovery.enabled=true
	spring.cloud.config.discovery.service-id=microservice-config
```

- Spring Cloud Bus消息服务总线

	1. 安装RabbitMQ
	
	2. 配置端导入RabiitMQ服务端
	
		2.1. 导入RabbitMQ依赖
		
```xml
	<!-- 导入SpringConfigBus依赖 -->
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-bus</artifactId>
  	</dependency>
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-stream-binder-rabbit</artifactId>
  	</dependency>
```

		2.2. 配置RabbitMQ(application.properties)
		
```
	spring.rabbitmq.host=127.0.0.1
	management.endpointes.web.exposure.include=bus-refresh
```

	3. 各个配置读取端配置RabbitMQ客户端
	
		31. 导入RabbitMQ依赖
		
```xml
	<!-- 导入SpringCloudBus客户端 -->
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-bus</artifactId>
  	</dependency>
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-stream-binder-rabbit</artifactId>
  	</dependency>
  	<dependency>
  		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
  	</dependency>
```

		3.2. 在svn配置文件中增加配置RabbitMQ
		
```
	spring.rabbitmq.host=127.0.0.1
```

## SpringCloud分布式链路跟踪 - （sleuth + Zipkin）

1. 微服中导入sleuth依赖

```xml
	<!-- 导入sleuth -->
  	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-sleuth</artifactId>
  	</dependency>
```

2. 搭建Zipkin服务器

- 新建一子工程 （ddic-zipkin-server）

- 导入zipkin依赖

```xml
	<dependency>
  		<groupId>io.zipkin.java</groupId>
  		<artifactId>zipkin-server</artifactId>
  		<version>2.9.4</version>
  		<exclusions>
  			<exclusion>
  				<groupId>org.apache.logging.log4j</groupId>
  				<artifactId>log4j-slf4j-impl</artifactId>
  			</exclusion>
  		</exclusions>
  	</dependency>
  	<dependency>
  		<groupId>io.zipkin.java</groupId>
  		<artifactId>zipkin-autoconfigure-ui</artifactId>
  		<version>2.9.4</version>
  	</dependency>
```

- 编写配置文件

```
	server.port=1003
	spring.application.name=ddic-zipkin-server
	management.metrics.web.server.auto-time-requests=false
```

- 编写启动类

```groovy
	package com.doosan.biz.ddic.zipkin
	import org.springframework.context.ConfigurableApplicationContext
	import zipkin.server.internal.EnableZipkinServer
	import org.springframework.boot.SpringApplication
	import org.springframework.boot.autoconfigure.SpringBootApplication
	@SpringBootApplication
	@EnableZipkinServer
	class DdicZipkinServerApplication {
		static void main(String[] args) {
			ConfigurableApplicationContext context = SpringApplication.run(DdicZipkinServerApplication, args)
		}
	}
```

3. 微服注册到Zipkin

- 导入zipkin依赖

```xml
	<dependency>
  		<groupId>org.springframework.cloud</groupId>
  		<artifactId>spring-cloud-starter-zipkin</artifactId>
  	</dependency>
```

- 配置zipkin服务端信息(application.properties)

```
	spring.zipkin.base-url=http://127.0.0.1:1003
	spring.zipkin.sender.type=web
	spring.sleuth.sampler.probability=1
```
