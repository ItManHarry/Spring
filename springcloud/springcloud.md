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