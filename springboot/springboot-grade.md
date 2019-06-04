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