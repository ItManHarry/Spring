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

- 实现方式

	1.编写统一的返回状态码及状态信息 - 枚举数据
	
```java
	package com.doosan.spring.boot2.result;
	/**
	 * 集中管理返回的枚举类
	 * @author 20112004
	 *
	 */
	public enum ResponseResults {
		
		SUCCESS(200, "执行成功"),
		NOTFOUND(0, "无数据"),
		NOTACCESS(403, "拒绝请求"),
		ERROR(500, "内部错误"),
		ERROR_TOKEN(502, "用户Token错误"),
		ERROR_TIMEOUT(503, "连接超时"),
		ERROR_UNKNOWN(555, "未知错误");
		
		private int status;
		private String message;
		
		ResponseResults(int status, String message){
			this.status = status;
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}	
	}
```

	2.编写返回数据对象
	
```java
	package com.doosan.spring.boot2.result;
	import java.sql.Timestamp;
	/**
	 * 返回接口数据类
	 * @author 20112004
	 * @param <T>
	 */
	public class ResponseResultObject<T> {
		//响应状态码
		private Integer status;
		//响应消息
		private String message;
		//返回对象
		private T data;
		//时间戳
		private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		public ResponseResultObject(){}
		
		
		public ResponseResultObject(ResponseResults results){
			this.status = results.getStatus();
			this.message = results.getMessage();
		}


		public Integer getStatus() {
			return status;
		}


		public void setStatus(Integer status) {
			this.status = status;
		}


		public String getMessage() {
			return message;
		}


		public void setMessage(String message) {
			this.message = message;
		}


		public T getData() {
			return data;
		}


		public void setData(T data) {
			this.data = data;
		}


		public Timestamp getTimestamp() {
			return timestamp;
		}


		public void setTimestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
		}
	}
```

	3.编写返回JSON数据对象
	
```java
	package com.doosan.spring.boot2.result;
	/**
	 * 返回Json数据
	 * @author 20112004
	 *
	 */
	public class ResponseResultJson {
		/**
		 * 无结果返回成功
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public static ResponseResultObject success(){
			return success(null);
		}
		/**
		 * 有数据返回成功
		 * @param object
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static ResponseResultObject success(Object object){
			ResponseResultObject resultOject = new ResponseResultObject(ResponseResults.SUCCESS);
			resultOject.setData(object);
			return resultOject;
		}
		/**
		 * 返回错误
		 * @param results
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public static ResponseResultObject error(ResponseResults results){
			ResponseResultObject resultOject = new ResponseResultObject(results);
			return resultOject;
		}
		
	}
```

	4.实体对象类
	
```java
	package com.doosan.spring.boot2.dao.entity;
	import java.io.Serializable;
	import java.util.Date;
	import com.fasterxml.jackson.annotation.JsonFormat;
	import com.fasterxml.jackson.annotation.JsonIgnore;
	import com.fasterxml.jackson.annotation.JsonInclude;

	public class User implements Serializable{

		private static final long serialVersionUID = 6266407321527104244L;
		private Integer id;
		private String name;
		private Integer age;
		@JsonInclude(JsonInclude.Include.NON_NULL)	//数据为空的话就不返回前台
		private String remark;
		@JsonIgnore 								//数据不会返回到前端
		private String password;
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale="zh",timezone="GMT+8") 	//数据格式化
		private Date createDate;
		
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

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
	}
```
	
	5.编写dao层
	
```java
	/**
	 * web框架返回对象
	 * @param uid
	 * @return
	 */
	public User getUserById2(Integer uid){
		return db.get(uid);
	}
	/**
	 * web框架返回所有数据
	 * @return
	 */
	public List<User> getAll(){
		List<User> users = new ArrayList<>();
		Iterator<User> its = db.values().iterator();
		while(its.hasNext()){
			users.add(its.next());
		}
		return users;
	}
```

	6.变成Controller层代码
	
```groovy
	/**
	 * 统一返回接口数据
	 * @param id
	 * @return
	 */
	@GetMapping("/user/json")
	ResponseResultObject<User> getJson(Integer id){
		User user = userDao.getUserById2(id)
		if(user)
			return ResponseResultJson.success(user)
		else
			return ResponseResultJson.error(ResponseResults.NOTFOUND)
	}
	/**
	 * 统一返回接口数据
	 * @param id
	 * @return
	 */
	@GetMapping("/user/rest/json/{id}")
	ResponseResultObject<User> getJson2(@PathVariable("id") Integer id){
		User user = userDao.getUserById2(id)
		if(user)
			return ResponseResultJson.success(user)
		else
			return ResponseResultJson.error(ResponseResults.NOTFOUND)
	}
	/**
	 * 统一返回接口数据
	 * @param id
	 * @return
	 */
	@GetMapping("/user/rest/json/all")
	ResponseResultObject<List<User>> getAll(){
		def users = userDao.getAll()
		if(users)
			return ResponseResultJson.success(users)
		else
			return ResponseResultJson.error(ResponseResults.NOTFOUND)
	}
```

## Spring Boot静态资源配置

- 自定义配置文件映射到实体类

	- 创建一个properties文件
	
	- 创建一个对应的Javabean
	
- 引入maven包

```java
	<!-- 配置文件加载 -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-configuration-processor</artifactId>
		<optional>true</optional>
	</dependency>
```

- 配置文件(customer.properties)

```
	com.doosan.project=SpringBoot2.0
	com.doosan.version=1.0
```

- 实体代码

```java
	package com.doosan.spring.boot2.dao.entity;
	import java.io.Serializable;
	import org.springframework.boot.context.properties.ConfigurationProperties;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.context.annotation.PropertySource;
	@Configuration
	@ConfigurationProperties(prefix="com.doosan")
	@PropertySource(value="classpath:customer.properties")
	public class Customer implements Serializable{

		private static final long serialVersionUID = 7164220872305562512L;
		private String project;
		private String version;
		
		public String getProject() {
			return project;
		}
		public void setProject(String project) {
			this.project = project;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
	}
```

- 控制层代码

```java
	package com.doosan.spring.boot2.controller.user
	import org.springframework.beans.BeanUtils
	import org.springframework.beans.factory.annotation.Autowired
	import org.springframework.web.bind.annotation.GetMapping
	import org.springframework.web.bind.annotation.RestController
	import com.doosan.spring.boot2.dao.entity.Customer
	import com.doosan.spring.boot2.result.ResponseResultJson
	import com.doosan.spring.boot2.result.ResponseResultObject
	@RestController
	class CustomerController {

		@Autowired
		private Customer customer
		/**
		 * 统一返回接口数据
		 * @param id
		 * @return
		 */
		@GetMapping("/customer/get")
		ResponseResultObject<Customer> getCustomer(){
			Customer vo = new Customer()
			BeanUtils.copyProperties(customer, vo)
			return ResponseResultJson.success(vo)
		}
	}
```

## 异常处理

