# Spring Boot2

## Webflux框架

	参考：https://blog.csdn.net/mikezzmeric/article/details/88075658
	
- SpringBoot启动webflux框架：
	
```xml
	<!-- 更改为webflux工程 -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-webflux</artifactId>
	</dependency>
```

- 编写实体表类

```java
	package com.doosan.spring.boot2.dao.entity;
	import java.io.Serializable;

	public class User implements Serializable{

		private static final long serialVersionUID = 6266407321527104244L;
		private Integer id;
		private String name;
		private Integer age;
		private String remark;
		
		public User(){}

		public User(Integer id, String name, Integer age, String remark) {
			super();
			this.id = id;
			this.name = name;
			this.age = age;
			this.remark = remark;
		}
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
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
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
	}
```

- 编写dao

```java
	package com.doosan.spring.boot2.dao;
	import java.util.concurrent.ConcurrentHashMap;
	import java.util.concurrent.ConcurrentMap;
	import java.util.concurrent.atomic.AtomicInteger;
	import org.springframework.stereotype.Repository;
	import com.doosan.spring.boot2.dao.entity.User;
	import reactor.core.publisher.Flux;
	import reactor.core.publisher.Mono;
	@Repository
	public class UserDao {

		//模拟数据库
		private final ConcurrentMap<Integer, User> db = new ConcurrentHashMap<>();
		//ID生成器
		private final static AtomicInteger id = new AtomicInteger();
		
		public UserDao(){
			db.put(1, new User(1, "Harry", 25, "Remark1"));
			db.put(2, new User(2, "Jack", 25, "Remark2"));
			db.put(3, new User(3, "Tom", 25, "Remark3"));
			db.put(4, new User(4, "Alex", 25, "Remark4"));
		}
		//保存
		public boolean save(User user){
			Integer uid = id.incrementAndGet();
			user.setId(uid);
			return db.putIfAbsent(uid, user) == null;
		}
		//保存
		public Mono<Void> add(User user){
			Integer uid = id.incrementAndGet();
			user.setId(uid);
			db.putIfAbsent(uid, user);
			return  Mono.empty();
		}
		/**
		 * 返回0或1个元素
		 * @param uid
		 * @return
		 */
		public Mono<User> getUserById(Integer uid){
			if(db.get(uid) == null)
				return Mono.empty();
			else 
				return Mono.just(db.get(uid));
		}
		/**
		 * 返回0或n个元素
		 * @return
		 */
		public Flux<User> findAll(){
			return Flux.fromIterable(db.values());
		}
		/**
		 * 执行删除
		 * @param uid
		 * @return
		 */
		public Mono<Void> delete(Integer uid){
			db.remove(uid);
			return Mono.empty();
		}
	}
```

-编写Handler（等同于service层）

```java
	package com.doosan.spring.boot2.handler;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Component;
	import org.springframework.web.reactive.function.server.ServerRequest;
	import org.springframework.web.reactive.function.server.ServerResponse;
	import com.doosan.spring.boot2.dao.UserDao;
	import com.doosan.spring.boot2.dao.entity.User;
	import reactor.core.publisher.Mono;
	@Component
	public class UserHandler {
		@Autowired
		private UserDao userDao;
		/**
		 * 查询所有的数据
		 * @param request
		 * @return
		 */
		public Mono<ServerResponse> findAll(ServerRequest request){
			return ServerResponse.ok().body(userDao.findAll(), User.class);
		}
		/**
		 * 根据ID查询
		 * @param request
		 * @return
		 */
		public Mono<ServerResponse> get(ServerRequest request){
			return userDao.getUserById(Integer.valueOf(request.pathVariable("id")))
					.flatMap(user -> ServerResponse.ok().syncBody(user))
					.switchIfEmpty(ServerResponse.notFound().build());
		}
		/**
		 * 执行删除
		 * @param request
		 * @return
		 */
		public Mono<ServerResponse> delete(ServerRequest request){
			return ServerResponse.noContent().build(userDao.delete(Integer.valueOf(request.pathVariable("id"))));
		}
		/**
		 * 执行保存
		 * @param request
		 * @return
		 */
		public Mono<ServerResponse> save(ServerRequest request){
			User user = new User();
			user.setAge(Integer.valueOf(request.pathVariable("age")));
			user.setName(request.pathVariable("name"));
			user.setRemark(request.pathVariable("remark"));
			return ServerResponse.noContent().build(userDao.add(user));
		}
	}
```

- 编写路由（相当于Controller层）

```java
	package com.doosan.spring.boot2.config;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.web.reactive.config.EnableWebFlux;
	import org.springframework.web.reactive.function.server.RequestPredicates;
	import org.springframework.web.reactive.function.server.RouterFunction;
	import org.springframework.web.reactive.function.server.RouterFunctions;
	import org.springframework.web.reactive.function.server.ServerResponse;
	import com.doosan.spring.boot2.handler.UserHandler;
	@EnableWebFlux
	@Configuration
	public class RouterConfig {
		
		@Bean
		public RouterFunction<ServerResponse> routers(UserHandler handler){
			return RouterFunctions.route(RequestPredicates.GET("/system/user/findall"), handler::findAll)
					.andRoute(RequestPredicates.GET("/system/user/get/{id}"), handler::get)
					.andRoute(RequestPredicates.GET("/system/user/delete/{id}"), handler::delete)
					.andRoute(RequestPredicates.GET("/system/user/add/{name}/{age}/{remark}"), handler::save);
		}
	}
```

## Spring Boot工程多模块化

- 创建一个Maven主模块

	- 将pom.xml文件中的"<packaging>jar</packaging>"改成"<packaging>pom</packaging>"
	
- 创建多个Maven子模块

	- 核心层:lib
	
	- 模型层:model
	
	- 持久层:persistence
	
	- 展现层:web
	
	操作方法：
	
		1.右击工程，选择new
		
		2.选择other，在搜索框中搜索maven，选择maven model
		
		3.勾选"Create a simple project(skip archetype selection)"
		
		4.填写"Model Name"，点击下一步，点击"Finish"
		
		5.新增的web工程里创建和父工程一样的src目录结构，将父工程的java/groovy文件及resources下的文件全部copy到web工程中，
		然后删除父工程的src及target文件夹，至此web子模块拆分完毕
		
		6.按照同样的步骤依次创建lib、model、persistence子模块
		
		7.创建依赖关系
		
			web依赖于persistence， persistence依赖于model，model依赖于lib
			创建方法，在web、persistence、model的pom.xml增加依赖
			
```xml
	<!-- web pom.xml -->
	<dependencies>
		<!-- web引入持久层 -->
		<dependency>
			<artifactId>persistence</artifactId>
			<groupId>doosan.spring.boot2</groupId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	</dependencies>
	<!-- persistence pom.xml -->
	<dependencies>
		<!-- persistence依赖model -->
		<dependency>
			<artifactId>model</artifactId>
			<groupId>doosan.spring.boot2</groupId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	</dependencies>
	<!-- model pom.xml -->
	<dependencies>
		<!-- model依赖lib -->
		<dependency>
			<artifactId>lib</artifactId>
			<groupId>doosan.spring.boot2</groupId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	</dependencies>	
```
		
## Spring Boot构造一个统一的接口返回对象

- 前后端分离开发，以JSON进行数据交互

	- SpringBoot默认问Jackson
	
- 统一返回接口数据渲染

	- Web(HTML5)
	
	- Android
	
	- iOS
	
	- 微信
	
	- 其他客户端

