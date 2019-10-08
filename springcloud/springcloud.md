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
					<version>Finchley.M9</version>
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

- 搭建Hystrix监控服务

- 使用Hystrix监控服务消费者情况