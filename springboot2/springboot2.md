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

	编写自定义异常类
	
```java
	package com.ch.dev.system.exception;
	import com.ch.dev.system.results.WebServerResults;
	/**
	 * 自定义一个Exception
	 * 	扩展RuntimeException, 增加一个status属性
	 * @author 20112004
	 * 注意：
	 * 	如果继承RuntimeException,Spring框架是支持事务回滚的
	 * 	如果继承Exception,Spring框架是不支持事务回滚的
	 *
	 */
	public class WebServerException extends RuntimeException {

		private static final long serialVersionUID = 6258084126128409605L;
		private Integer status;
		
		public WebServerException(){
			
		}
		
		public WebServerException(Integer status, String message){
			super(message);
			this.status = status;
		}
		
		public WebServerException(WebServerResults result){
			super(result.getMessage());
			this.status = result.getStatus();
		}
		
		public WebServerException(WebServerResults result, Exception e){
			super((e.getMessage() != null) ? (result.getMessage() + ":" + e.getMessage()) : result.getMessage());
			this.status = result.getStatus();
		}
		
		
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
	}
```

	编写异常处理类
	
```java
	package com.ch.dev.system.exception.handler;
	import javax.servlet.http.HttpServletRequest;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.web.bind.annotation.ControllerAdvice;
	import org.springframework.web.bind.annotation.ExceptionHandler;
	import org.springframework.web.bind.annotation.ResponseBody;
	import org.springframework.web.servlet.ModelAndView;
	import com.ch.dev.system.exception.WebServerException;
	import com.ch.dev.system.results.WebServerResultJson;
	/**
	 * Exception异常处理
	 * 兼容说明：
	 * 	支持HTTP WEB异常页面跳转
	 * 	支持AJAX JSON异常数据返回
	 * @author 20112004
	 *
	 */
	@ControllerAdvice
	public class WebServerExceptionHandler {
		private final static Logger logger = LoggerFactory.getLogger(WebServerExceptionHandler.class);
		public static final String ERROR_PAGE = "system/common/error";
		@ExceptionHandler(value=WebServerException.class)
		@ResponseBody
		public Object handle(HttpServletRequest request, Exception e){
			//获取异常页面URL地址
			String exceptionURL = null;
			Object o = request.getRequestURL();
			if(o != null)
				exceptionURL = request.getRequestURL().toString();
			//异常处理
			if(e instanceof WebServerException){
				logger.error("000 自定义异常：{}", e);
				WebServerException se = (WebServerException)e;
				if(!isAjax(request))
					return exceptionView(se.getStatus(), se.getMessage(), exceptionURL);
				else
					return WebServerResultJson.error(se.getStatus(), se.getMessage(), exceptionURL);
			}else{
				logger.error("500 系统异常", e);
				if(!isAjax(request))
					return exceptionView(500, e.toString(), exceptionURL);
				else
					return WebServerResultJson.error(500, e.toString(), exceptionURL);
			}
		}
		/**
		 * 判断请求是否是异步请求
		 * @return
		 */
		private static boolean isAjax(HttpServletRequest request){
			String header = request.getHeader("x-requested-with");
			boolean ajax = "XMLHttpRequest".equals(header) ? true : false;
			return ajax;
		}
		/**
		 * 错误页面跳转
		 * @param status
		 * @param message
		 * @param exceptionURL
		 * @return
		 */
		private static ModelAndView exceptionView(Integer status, String message, String exceptionURL){
			ModelAndView mav = new ModelAndView();
			mav.addObject("status",status);
			mav.addObject("message", message);
			mav.addObject("exceptionURL", exceptionURL);
			mav.setViewName(ERROR_PAGE);
			return mav;
		}
	}	
```

## Spring Boot异步任务处理

- 在项目启动类增加@EnableAsync注解，开启SpringBoot异步任务

- 编写异步处理类

```java
	package com.ch.dev.task;
	import java.time.Duration;
	import java.time.Instant;
	import java.util.concurrent.Future;
	import org.springframework.scheduling.annotation.Async;
	import org.springframework.scheduling.annotation.AsyncResult;
	import org.springframework.stereotype.Component;
	/**
	 * Asynchronize
	 * @author 20112004
	 *
	 */
	@Component
	public class WebServerAsyncTask {

		/**
		 * Do the delete logger task
		 * @return
		 * @throws Exception
		 */
		@Async
		public Future<Boolean> deleteLogger() throws Exception{
			Instant t1 = Instant.now();
			for(int i = 0; i < 1000; i++){
				System.out.println("--- doing delete the log action ---");
			}
			Thread.sleep(1000);
			Instant t2 = Instant.now();
			Duration duration = Duration.between(t1, t2);
			System.out.println("Delete logger used " + duration.toMillis() + " ms");
			return new AsyncResult<Boolean>(true);		
		}
		/**
		 * Do delete 
		 * @return
		 * @throws Exception
		 */
		@Async
		public Future<Boolean> deleteOrders() throws Exception{
			Instant t1 = Instant.now();
			for(int i = 0; i < 1000; i++){
				System.out.println("*** doing delete the order action ***");
			}
			Thread.sleep(600);
			Instant t2 = Instant.now();
			Duration duration = Duration.between(t1, t2);
			System.out.println("Delete orders used " + duration.toMillis() + " ms");
			return new AsyncResult<Boolean>(true);		
		} 
	}
```

- 编写测试类型，使用定时任务执行

```java
	package com.ch.dev.task;
	import java.time.Duration;
	import java.time.Instant;
	import java.util.concurrent.Future;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Component;
	/**
	 * Do the task
	 * @author 20112004
	 *
	 */
	@Component
	public class DoWebServerAsyncTask {

		@Autowired
		private WebServerAsyncTask wsat;
		@Scheduled(cron="0 0/1 * * * ?")
		public void test() throws Exception{
			System.out.println("Now do the async task...");
			runAsync();
		}
		
		private void runAsync() throws Exception{
			Instant start = Instant.now();
			Future<Boolean> logger = wsat.deleteLogger();
			Future<Boolean> orders = wsat.deleteOrders();
			while(!logger.isDone() || !orders.isDone()){
				if(logger.isDone() && orders.isDone())
					break;
				
			}
			Instant end = Instant.now();
			Duration d = Duration.between(start, end);
			System.out.println("All task have been finished , used (" + d.toMillis() + ") ms");
		}	
	}
```

## SpringBoot拦截器

- 创建一个配置类，继承WebMVCConfigurationSupport

	- 重写 addInterceptors方法
	
```java
	package com.ch.dev.configuration.inteceptors;
	import org.springframework.beans.factory.annotation.Value;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
	import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
	import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
	/**
	 * System Intercepttors
	 * @author 20112004
	 *
	 */
	@Configuration
	public class WebServerMvcConfigration extends WebMvcConfigurationSupport {

		@Value("${system.image.path}")
		private String imagePath;

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

		@Override
		protected void addInterceptors(InterceptorRegistry registry) {
			System.out.println("Image path : " + imagePath);
			//拦截器是按照顺序来执行的,先执行前面的拦截器
			registry.addInterceptor(new TokenInterceptor())
				.excludePathPatterns("/register","/favicon.ico")
				.addPathPatterns("/*/**");
			
			registry.addInterceptor(new LoggerInteceptor())
				.addPathPatterns("/*/**");
			super.addInterceptors(registry);
		}

		@Override
		protected void addResourceHandlers(ResourceHandlerRegistry registry) {
			
			registry.addResourceHandler("/favicon.ico")
				.addResourceLocations("/static/**")
				.addResourceLocations("classpath:/static/");
			System.out.println("Image path : " + imagePath);
			registry.addResourceHandler("/custom/**")
				.addResourceLocations("file:"+imagePath);
			super.addResourceHandlers(registry);
		}		
	}
```
	
- 创建拦截器实现类，继承HandlerInterceptor

	- 重写父类的三个方法
	
```java
	package com.ch.dev.configuration.inteceptors;
	import java.io.OutputStream;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import org.springframework.web.servlet.HandlerInterceptor;
	import org.springframework.web.servlet.ModelAndView;
	import com.ch.dev.system.exception.WebServerException;
	import com.ch.dev.system.results.WebServerResultJson;
	import com.ch.dev.system.results.WebServerResultObject;
	import com.ch.dev.system.results.WebServerResults;
	import com.ch.dev.utils.JsonUtils;
	/**
	 * 系统登录拦截器
	 */
	public class TokenInterceptor implements HandlerInterceptor {

		
		/**
		 * 在Controller执行前调用
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
			System.out.println("令牌处理拦截器...... URL : " + request.getRequestURI());
			String user = request.getParameter("username");
			if(user != null && user.equals("Harry")){
				errorOutput(response, WebServerResultJson.error(WebServerResults.ERROR_USERNOTFOUND));
				System.out.println("The user has logined ...");
				return true;
			}
			throw new WebServerException(800, "Please input user name");
		}
		/**
		 * 在Controller执行之后,视图渲染前调用
		 */
		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mav)
				throws Exception {
			// TODO Auto-generated method stub
			if(mav != null){
				System.out.println("ViewName : " + mav.getViewName() + "------------------" + request.getRequestURI());
			}
		}
		/**
		 * 在Controller执行完成,视图渲染完毕后调用
		 */
		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e)
				throws Exception {
			System.out.println("Finished the request ......" + request.getRequestURI());		
		}
		/**
		 * 拦截后的错误输出
		 * @param response
		 * @param result
		 * @throws Exception
		 */
		private void errorOutput(HttpServletResponse response, WebServerResultObject<Object> result) throws Exception{
			OutputStream output = null;
			try{
				response.setContentType("text/json");
				response.setCharacterEncoding("utf-8");
				output = response.getOutputStream();
				output.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
				output.flush();
			}finally{
				if(output != null)
					output.close();
			}
		}
	}
```

	- JSON处理类
	
```java
	package com.ch.dev.utils;
	import java.util.List;
	import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.databind.JavaType;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import com.fasterxml.jackson.databind.util.JSONPObject;
	/**
	 * Json转换工具
	 */
	public class JsonUtils {

		private static final ObjectMapper MAPPER = new ObjectMapper();
		/**
		 * 对象转为字符串
		 * @param data
		 * @return
		 */
		public static String objectToJson(Object data){
			try{
				String string = MAPPER.writeValueAsString(data);
				return string;
			}catch(JsonProcessingException e){
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * 将json字符串转换为对象
		 * @param jsonData
		 * @param clazz
		 * @return
		 */
		public static <T> T jsonToBean(String jsonData, Class<T> clazz){
			try{
				T t = MAPPER.readValue(jsonData, clazz);
				return t;
			}catch(Exception e){
				e.printStackTrace();
			}		
			return null;
		}
		/**
		 * 将json字符串转换为pojo对象list
		 * @param jsonData
		 * @param beanType
		 * @return
		 */
		public static <T>List<T> jsonToList(String jsonData, Class<T> beanType){
			JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
			try{
				List<T> list = MAPPER.readValue(jsonData, javaType);
				return list;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * 输出JSONP字符串
		 * @param functionName
		 * @param o
		 * @return
		 */
		public String toJsonpString(String functionName, Object o){
			JSONPObject jsonp = new JSONPObject(functionName, o);
			return jsonp.toString();
		}
	}
```

- postman验证地址：http://localhost:8080/login?usercd=20112004&password=123&username=Harry

- springboot设置不拦截静态资源的方法（这个问题我查了很久资料才解决！）

	1.拦截器中的复写addResourceHandlers，重新指定静态资源路径
	
```java
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/");
		
		System.out.println("Image path : " + imagePath);
		
		registry.addResourceHandler("/images/**")
			.addResourceLocations("file:"+imagePath);
		super.addResourceHandlers(registry);
	}		
```
	
	2.访问地址需要增加"static"
	
	如之前的访问地址："http://localhost:8080/html/index.html",需要变为"http://localhost:8080/static/html/index.html"
	
	3.页面改造；
	
	改之前：
	
```html
	<link rel="shortcut icon" type="image/x-icon" th:href = "@{/images/logoes/way.ico}" media="screen" />
	<link rel = "stylesheet" th:href = "@{/css/bootstrap.min.css}" media="screen"/>
	<script type = "text/javascript" th:src = "@{/js/bootstrap.min.js}" charset="UTF-8"></script>
	<script type = "text/javascript" th:src = "@{/js/jquery-1.11.3.min.js}" charset="UTF-8"></script>
	<script type = "text/javascript" th:src = "@{/js/vue.js}" charset="UTF-8"></script>
```

	改之后(路径增加static)：

```html
	<link rel="shortcut icon" type="image/x-icon" th:href = "@{/static/images/logoes/way.ico}" media="screen" />
	<link rel = "stylesheet" th:href = "@{/static/css/bootstrap.min.css}" media="screen"/>
	<script type = "text/javascript" th:src = "@{/static/js/bootstrap.min.js}" charset="UTF-8"></script>
	<script type = "text/javascript" th:src = "@{/static/js/jquery-1.11.3.min.js}" charset="UTF-8"></script>
	<script type = "text/javascript" th:src = "@{/static/js/vue.js}" charset="UTF-8"></script>
```
	
	4.参考网址
	
	https://www.cnblogs.com/kangkaii/p/9023751.html

## 集成AOP

- 引入aop

```xml
	<!-- 引入AOP -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-aop</artifactId>
	</dependency>
```

- 编写AOP类

```java
	package com.ch.dev.configuration.aspects;
	import javax.servlet.http.HttpServletRequest;
	import org.aspectj.lang.JoinPoint;
	import org.aspectj.lang.annotation.After;
	import org.aspectj.lang.annotation.AfterReturning;
	import org.aspectj.lang.annotation.Aspect;
	import org.aspectj.lang.annotation.Before;
	import org.aspectj.lang.annotation.Pointcut;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.stereotype.Component;
	import org.springframework.web.context.request.RequestContextHolder;
	import org.springframework.web.context.request.ServletRequestAttributes;
	@Aspect
	@Component
	public class HTTPAspectComponent {
		
		private static final Logger logger = LoggerFactory.getLogger(HTTPAspectComponent.class);
		
		@Pointcut("execution(public * com.ch.dev.controller.system.login.*.*(..))")
		public void path(){
			
		}

		@Before("path()")
		public void beforeLogin(JoinPoint joinPoint){
			ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			//URL
			logger.info("URL : " + request.getRequestURL());
			//IP
			logger.info("IP : " + request.getRemoteAddr());
			//Method
			logger.info("Method : " + request.getMethod());
			//Class
			logger.info("Class : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
			//Parameters
			logger.info("Args : " + joinPoint.getArgs().toString());
			logger.info("LOG BEFORE LOGIN AOP...");
		}
		
		@After("path()")
		public void afterLogin(){
			logger.info("LOG AFTER LOGIN AOP...");
		}
		
		@AfterReturning(pointcut="path()", returning="object")
		public void result(Object object){
			if(object != null)
				logger.info("Result : " + object.toString());
		}
	}
```

## 单元测试

- 方式一 : Service方法测试

- 方式二 : API(Controller层)访问测试

代码：

```java
	package com.ch.dev.test;
	import java.util.List;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import org.springframework.test.web.servlet.MockMvc;
	import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
	import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
	import com.ch.dev.WebServerStarter;
	import com.ch.dev.domain.SysUser;
	import com.ch.dev.service.SysUserService;
	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes={WebServerStarter.class})
	@AutoConfigureMockMvc
	public class LoginControllerTest {

		@Autowired
		private SysUserService sysUserService;
		@Autowired
		private MockMvc mvc;
		/**
		 * 测试方式一:service层测试
		 * @throws Exception
		 */
		@Test
		public void testLoginByService() throws Exception{
			List<SysUser> users = sysUserService.findByUsercd("20112004");
			for(SysUser u : users){
				System.out.println("User Name : " + u.getUsernm() + ", password : " + u.getPassword());
			}
		}	
		/**
		 * 测试方式二:controller层测试
		 */
		@Test
		public void testLoginByUrl() throws Exception{
			mvc.perform(MockMvcRequestBuilders.get("/system/login?usercd=20112004&password=12345678"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("20112004"));
		}
	}
```

- maven打包跳过测试包，安装使用maven插件即可(pom.xml)

```xml
	<!-- 跳过测试文件不打包 -->
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-surefire-plugin</artifactId>
		<version>2.18.1</version>
		<configuration>
			<skipTests>true</skipTests>
		</configuration>
	</plugin>
```

## SpringBoot日志配置

- SpringBoot默认使用Logback

- 配置application.properties文件，指定logback配置文件

```
	logging.config=classpath:logback-spring.xml
```

- resources下配置logback-spring.xml文件中的

```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<configuration debug="false" scan="true" scanPeriod="60 second">
		<!-- 定义日志文件路径,勿在Logback配置文件中使用相对路径 -->
		<property name="LOG_HOME" value="D:/spring/logs" />
		<property name="FILESIZE" value="20MB" />
		<property name="MAXHISTORY" value="30" />
		<timestamp key="DATETIME" datePattern="yyyy-MM-dd HH:mm:ss" />
		<!-- 控制台打印 -->
		<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
			<!-- 格式化输出 : %d表示日期 ;%thread表示线程;%-5level:级别从左显示5个字符宽度;%msg:日志消息;%n：换行符 -->
			<encoder charset="utf-8" class = "ch.qos.logback.classic.encoder.PatternLayoutEncoder">
				<pattern>%d{${DATETIME}} [%thread] %-5level %logger{50} - %msg%n</pattern>
			</encoder>
		</appender>
		<!-- 按照每天生成日志 -->
		<appender name = "FILE" class = "ch.qos.logback.core.rolling.RollingFileAppender">
			<rollingPolicy class = "ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
				<!-- 日志文件名 -->
				<fileNamePattern>${LOG_HOME}/webserver.log.%d{yyyy-MM-dd}.log</fileNamePattern>
				<!-- 日志文件保留天数 -->
				<maxHistory>${MAXHISTORY}</maxHistory>
			</rollingPolicy>
			<!-- 格式化输出 : %d表示日期 ;%thread表示线程;%-5level:级别从左显示5个字符宽度;%msg:日志消息;%n：换行符 -->
			<encoder charset="utf-8" class = "ch.qos.logback.classic.encoder.PatternLayoutEncoder">
				<pattern>%d{${DATETIME}} [%thread] %-5level %logger{50} - %msg%n</pattern>
			</encoder>
			<triggeringPolicy class = "ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
				<MaxFileSize>${FILESIZE}</MaxFileSize>
			</triggeringPolicy>
		</appender>
		<!-- 按照每天生成错误日志 -->
		<appender name = "ERROR-FILE" class = "ch.qos.logback.core.rolling.RollingFileAppender">
			<rollingPolicy class = "ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
				<!-- 日志文件名 -->
				<fileNamePattern>${LOG_HOME}/webserver-error.log.%d{yyyy-MM-dd}.log</fileNamePattern>
				<!-- 日志文件保留天数 -->
				<maxHistory>${MAXHISTORY}</maxHistory>
			</rollingPolicy>
			<!-- 格式化输出 : %d表示日期 ;%thread表示线程;%-5level:级别从左显示5个字符宽度;%msg:日志消息;%n：换行符 -->
			<encoder charset="utf-8" class = "ch.qos.logback.classic.encoder.PatternLayoutEncoder">
				<pattern>%d{${DATETIME}} [%thread] %-5level %logger{50} - %msg%n</pattern>
			</encoder>
			<triggeringPolicy class = "ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
				<MaxFileSize>${FILESIZE}</MaxFileSize>
			</triggeringPolicy>
			<filter class = "ch.qos.logback.classic.filter.LevelFilter">
				<level>error</level>
				<onMismatch>DENY</onMismatch>
			</filter>
		</appender>
		<!-- 工程默认日志级别 -->
		<logger name = "com.ch.dev" level = "debug"/>
		<!-- SQL相关日志输出-->
		<logger name="jdbc.sqltiming" level="debug" />
		<logger name="com.ibatis" level="debug" />
		<logger name="com.ibatis.common.jdbc.SimpleDataSource" level="debug" />
		<logger name="com.ibatis.common.jdbc.ScriptRunner" level="debug" />
		<logger name="com.ibatis.sqlmap.engine.impl.SqlMapClientDelegate" level="debug" />
		<logger name="java.sql.Connection" level="debug" />
		<logger name="java.sql.Statement" level="debug" />
		<logger name="java.sql.PreparedStatement" level="debug" />
		<logger name="java.sql.ResultSet" level="debug" />    
		<!-- Logger 根目录 -->
		<root level="DEBUG">
			<appender-ref ref = "STDOUT" />
			<appender-ref ref = "FILE" />  
			<appender-ref ref = "ERROR-FILE"/>
		</root>
	</configuration>
```

注：配置完日志文件后，启动工程会报异常，异常信息如下：

```
2019-07-15 15:35:08 [restartedMain] DEBUG o.s.s.a.ScheduledAnnotationBeanPostProcessor - Could not find default TaskScheduler bean
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.scheduling.TaskScheduler' available
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveNamedBean(DefaultListableBeanFactory.java:989)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.resolveSchedulerBean(ScheduledAnnotationBeanPostProcessor.java:287)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.finishRegistration(ScheduledAnnotationBeanPostProcessor.java:228)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:205)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:99)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:393)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:347)
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:883)
	at org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.finishRefresh(EmbeddedWebApplicationContext.java:146)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:545)
	at org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.refresh(EmbeddedWebApplicationContext.java:124)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:693)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:360)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:303)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1118)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1107)
	at com.ch.dev.WebServerStarter.main(WebServerStarter.java:12)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49)
2019-07-15 15:35:08 [restartedMain] DEBUG o.s.s.a.ScheduledAnnotationBeanPostProcessor - Could not find default ScheduledExecutorService bean
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'java.util.concurrent.ScheduledExecutorService' available
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveNamedBean(DefaultListableBeanFactory.java:989)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.resolveSchedulerBean(ScheduledAnnotationBeanPostProcessor.java:287)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.finishRegistration(ScheduledAnnotationBeanPostProcessor.java:249)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:205)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:99)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:393)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:347)
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:883)
	at org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.finishRefresh(EmbeddedWebApplicationContext.java:146)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:545)
	at org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.refresh(EmbeddedWebApplicationContext.java:124)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:693)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:360)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:303)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1118)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1107)
	at com.ch.dev.WebServerStarter.main(WebServerStarter.java:12)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49)
2019-07-15 15:35:08 [restartedMain] INFO  o.s.s.a.ScheduledAnnotationBeanPostProcessor - No 
```
以上异常信息可忽略，不会影响定时任务执行，只是Spring的定时任务调度器抛出的debug信息，详细参考：https://blog.csdn.net/oarsman/article/details/52801877