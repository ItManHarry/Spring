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
	@Controller
	@RequestMapping("/starter")
	public class HelloController {

		@RequestMapping("/hello")
		@ResponseBody
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
	/**
	 * web工程启动器
	 */
	@SpringBootApplication
	public class ApplicationStarter {
		
		public static void main(String[] args){
			SpringApplication.run(ApplicationStarter.class, args);
		}
		
	}
```
