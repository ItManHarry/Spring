# Springboot整合Ehcache

## 整合步骤

- 导入Ehcache包,编辑pom.xml文件

```xml
	<!-- SpringBoot缓存 -->
  	<dependency>
  		<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-cache</artifactId>
  	</dependency>
  	<!-- Echcache支持  -->
	<dependency>
	    <groupId>net.sf.ehcache</groupId>
	    <artifactId>ehcache</artifactId>
	    <version>2.10.6</version>
	</dependency>
```

- 配置Ehcache的配置文件:ehcache.xml(路径在resources下即可,和application.properties同级)

```xml
	<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
    <diskStore path="java.io.tmpdir"/>
		<!-- ehcache默认配置 -->
		<defaultCache
				maxElementsInMemory="10000"
				eternal="false"
				timeToIdleSeconds="120"
				timeToLiveSeconds="120"
				maxElementsOnDisk="10000000"
				diskExpiryThreadIntervalSeconds="120"
				memoryStoreEvictionPolicy="LRU">
			<persistence strategy="localTempSwap"/>
		</defaultCache>
		<!-- 创建一名称为employee的缓存 -->
		 <cache name = "employee"
				maxElementsInMemory="10000"
				eternal="false"
				timeToIdleSeconds="120"
				timeToLiveSeconds="120"
				maxElementsOnDisk="10000000"
				diskExpiryThreadIntervalSeconds="120"
				memoryStoreEvictionPolicy="LRU">
			<persistence strategy="localTempSwap"/>
		</cache>
	</ehcache>
```

- 配置application.properties

```
	spring.cache.ehcache.config=ehcache.xml
```

- 启动类里启动缓存

```java
	package com.doosan.sb;
	import javax.servlet.ServletRegistration;
	import org.mybatis.spring.annotation.MapperScan;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.web.servlet.FilterRegistrationBean;
	import org.springframework.boot.web.servlet.ServletComponentScan;
	import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
	import org.springframework.boot.web.servlet.ServletRegistrationBean;
	import org.springframework.cache.annotation.EnableCaching;
	import org.springframework.context.annotation.Bean;
	import com.doosan.sb.filter.FilterUsage;
	import com.doosan.sb.listener.ListenerUsage;
	import com.doosan.sb.servlet.ServletUsage;
	/**
	 * web工程启动器
	 */
	@SpringBootApplication
	//@ServletComponentScan	//SpringBoot扫描@WebServlet注解对应的类
	@MapperScan("com.doosan.sb.dao")	//MyBtis扫描
	@EnableCaching						//启用缓存
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

- 实现方法上增加@Cacheable注解

```java
	package com.doosan.sb.service.imp.employee;
	import java.util.List;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.cache.annotation.Cacheable;
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;
	import com.doosan.sb.dao.domain.Tb_Employee;
	import com.doosan.sb.dao.employee.EmployeeDao;
	import com.doosan.sb.service.employee.EmployeeService;
	@Service
	@Transactional
	public class EmployeeServiceImpl implements EmployeeService {
		@Autowired
		private EmployeeDao employeeDao;
		
		@Override
		public List<Tb_Employee> findAll() {
			// TODO Auto-generated method stub
			return employeeDao.findAll();
		}

		@Override
		@Cacheable(value="employee") //启用缓存,缓存名称为：employee
		public Tb_Employee findById(int id) {
			// TODO Auto-generated method stub
			return employeeDao.findOne(id);
		}

		@Override
		public Page<Tb_Employee> findByPage(Pageable page) {
			// TODO Auto-generated method stub
			return employeeDao.findAll(page);
		}

		@Override
		public void save(Tb_Employee employee) {
			// TODO Auto-generated method stub
			employeeDao.save(employee);
		}

	}
```

- 编写单元测试

```java
	package com.doosan.sb.test.cache;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	import com.doosan.sb.service.employee.EmployeeService;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class EmployeeCacheTest {
		
		@Autowired
		private EmployeeService employeeService;
		
		@Test
		public void testFindOne(){
			//first query
			System.out.println(employeeService.findById(1));
			//secondary query
			//缓存启用后,查询数据库只执行一次，第二次查询就是走的缓存,可从后台打印的HQL看出
			System.out.println(employeeService.findById(1));
		}
	}
```

- 执行单元测试报错：

	java.io.NotSerializableException: com.doosan.sb.dao.domain.Tb_Employee
	
	解决方法：让Tb_Employee实现Serializable接口即可
	
## Ehcache注解

- @Cacheable:把方法的返回值放入缓存

	value:属性执行对应于ehcache.xml中的缓存名称
	key:给缓存的值取个名称,只要查询同一个名称的数据,缓存都有效
	
```java
	package com.doosan.sb.test.cache;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.data.domain.PageRequest;
	import org.springframework.data.domain.Pageable;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	import com.doosan.sb.service.employee.EmployeeService;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class EmployeeCacheTest {
		
		@Autowired
		private EmployeeService employeeService;
		
		@Test
		public void testFindOne(){
			//第一次查询
			System.out.println(employeeService.findById(1));
			//第二次查询
			//缓存启用后,查询数据库只执行一次，第二次查询就是走的缓存,可从后台打印的HQL看出
			System.out.println(employeeService.findById(1));
		}
		@Test
		public void testFindByPage(){
			Pageable pageable = new PageRequest(0, 5);
			//第一次查询 - 查询数据库
			System.out.println(employeeService.findByPage(pageable).getTotalElements());
			//第二次查询 - 不走数据库查询,走缓存
			System.out.println(employeeService.findByPage(pageable).getTotalElements());
			//换页查询 - 查询数据库
			pageable = new PageRequest(1, 5);
			System.out.println(employeeService.findByPage(pageable).getTotalElements());
		}
	}
```
	

- @CacheEvict:把数据从缓存中清除出去

```java
	package com.doosan.sb.service.imp.employee;
	import java.util.List;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.cache.annotation.CacheEvict;
	import org.springframework.cache.annotation.Cacheable;
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;
	import com.doosan.sb.dao.domain.Tb_Employee;
	import com.doosan.sb.dao.employee.EmployeeDao;
	import com.doosan.sb.service.employee.EmployeeService;
	@Service
	@Transactional
	public class EmployeeServiceImpl implements EmployeeService {
		@Autowired
		private EmployeeDao employeeDao;
		
		@Override
		@Cacheable(value="employee") //启用缓存,缓存名称为：employee
		public List<Tb_Employee> findAll() {
			// TODO Auto-generated method stub
			return employeeDao.findAll();
		}

		@Override
		@Cacheable(value="employee") //启用缓存,缓存名称为：employee
		public Tb_Employee findById(int id) {
			// TODO Auto-generated method stub
			return employeeDao.findOne(id);
		}

		@Override
		@Cacheable(value="employee") //启用缓存,缓存名称为：employee
		public Page<Tb_Employee> findByPage(Pageable page) {
			// TODO Auto-generated method stub
			return employeeDao.findAll(page);
		}

		@Override
		@CacheEvict(value="employee",allEntries=true)
		public void save(Tb_Employee employee) {
			// TODO Auto-generated method stub
			employeeDao.save(employee);
		}
	}
```

- 最终的单元测试代码：

```java
	package com.doosan.sb.test.cache;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.cache.annotation.Cacheable;
	import org.springframework.data.domain.PageRequest;
	import org.springframework.data.domain.Pageable;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	import com.doosan.sb.dao.domain.Tb_Employee;
	import com.doosan.sb.service.employee.EmployeeService;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class EmployeeCacheTest {
		
		@Autowired
		private EmployeeService employeeService;
		
		@Test
		public void testFindOne(){
			//第一次查询
			System.out.println(employeeService.findById(1));
			//第二次查询
			//缓存启用后,查询数据库只执行一次，第二次查询就是走的缓存,可从后台打印的HQL看出
			System.out.println(employeeService.findById(1));
		}
		@Test
		public void testFindByPage(){
			Pageable pageable = new PageRequest(0, 5);
			//第一次查询 - 查询数据库
			System.out.println(employeeService.findByPage(pageable).getTotalElements());
			//第二次查询 - 不走数据库查询,走缓存
			System.out.println(employeeService.findByPage(pageable).getTotalElements());
			//换页查询 - 查询数据库,之所以重新走数据库查询,主要是@Cacheable的key属性起了作用
			pageable = new PageRequest(1, 5);
			System.out.println(employeeService.findByPage(pageable).getTotalElements());
		}
		@Test
		public void testFindAll(){
			//第一次查询
			System.out.println(employeeService.findAll().size());
			//新增一个雇员
			Tb_Employee employee = new Tb_Employee();
			employee.setAddress("For cache test");
			employee.setAge(25);
			employee.setGender("F");
			employee.setName("ForCache");
			employee.setTelphone("13652645268");
			//如果在sava方法上不加@CacheEvict注解的话,二次查询的数据还是查的之前的缓存,造成数据不准确
			employeeService.save(employee);
			//第二次查询
			System.out.println(employeeService.findAll().size());
		}
	}
```

# Springboot整合Redis

	Spring data Redis是属于springdata项目的模块，作用就是简化Redis技术开发。

## 整合步骤


- 导入Redis包,配置pom.xml文件

```xml
	<!-- SpringBoot-Redis缓存 -->
  	<dependency>
  		<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-data-redis</artifactId>
  	</dependency>
```

- 编写Redis配置类

```java
	package com.doosan.sb.cache.redis;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.serializer.StringRedisSerializer;
	import redis.clients.jedis.JedisPoolConfig;
	/**
	 * 整合Spring Data Redis
	 *
	 */
	@Configuration
	public class RedisConfiguration {
		/**
		 * 创建JedisPoolConfig：设置连接池参数
		 * @return
		 */
		@Bean
		public JedisPoolConfig getJedisPoolConfig(){
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			//设置最大最小闲置数
			poolConfig.setMaxIdle(5);
			poolConfig.setMinIdle(2);
			//设置最大连接数
			poolConfig.setMaxIdle(20);
			return poolConfig;
		}
		/**
		 * 创建JedisConnectionFactory：配置redis连接参数
		 * @param poolConfig
		 * @return
		 */
		@Bean
		public JedisConnectionFactory getJedisConnectionFactory(JedisPoolConfig poolConfig){
			JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
			//设置连接池
			connectionFactory.setPoolConfig(poolConfig);
			//设置主机地址
			connectionFactory.setHostName("localhost");
			//设置端口
			connectionFactory.setPort(6379);
			//设置数据库索引 - Redis默认有16个数据库,这里设置访问第二个数据库
			connectionFactory.setDatabase(1);
			return connectionFactory;
		}
		/**
		 * 创建RedisTemplate：执行Redis的操作方法
		 * @param connectionFactory
		 * @return
		 */
		@Bean
		public RedisTemplate<String, Object> getRedisTemplate(JedisConnectionFactory connectionFactory){
			RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
			//关联连接工厂
			template.setConnectionFactory(connectionFactory);
			//设置key的序列化
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new StringRedisSerializer());
			return template;
		}
	}
```

- 编写测试类

```java
	package com.doosan.sb.test.cache.redis;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class RedisTest {
		
		@Autowired
		private RedisTemplate<String, Object> redisTemplate;
		/**
		 * 存入字符串
		 */
		@Test
		public void testSet(){
			redisTemplate.opsForValue().set("email", "guoqian.cheng@doosan.com");
		}
		/**
		 * 读取字符串
		 */
		@Test
		public void testGet(){
			String email = (String)redisTemplate.opsForValue().get("email");
			System.out.println("Email is : " + email);
		}
	}
```

- 抽取Redis的参数

	- 配置application.properties文件,config类会自动读取配置文件里的值
	
```
	spring.redis.host=localhost
	spring.redis.port=6379
	spring.redis.database=1
	spring.redis.pool.max-idle=5
	spring.redis.pool.min-idle=2
	spring.redis.pool.max-active=20
```
 
	- 修改配置文件，增加@ConfigurationProperties(prefix="spring.redis")配置Ehcache的配置文件
	
```java
	package com.doosan.sb.cache.redis;
	import org.springframework.boot.context.properties.ConfigurationProperties;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.serializer.StringRedisSerializer;
	import redis.clients.jedis.JedisPoolConfig;
	/**
	 * 整合Spring Data Redis
	 *
	 */
	@Configuration
	public class RedisConfiguration {
		/**
		 * 创建JedisPoolConfig：设置连接池参数
		 * @return
		 */
		@Bean
		@ConfigurationProperties(prefix="spring.redis")
		public JedisPoolConfig getJedisPoolConfig(){
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			/**
			 * 从application.properties配置文件自动装配
			 */
	//		//设置最大最小闲置数
	//		poolConfig.setMaxIdle(5);
	//		poolConfig.setMinIdle(2);
	//		//设置最大连接数
	//		poolConfig.setMaxTotal(20);
			return poolConfig;
		}
		/**
		 * 创建JedisConnectionFactory：配置redis连接参数
		 * @param poolConfig
		 * @return
		 */
		@Bean
		@ConfigurationProperties(prefix="spring.redis")
		public JedisConnectionFactory getJedisConnectionFactory(JedisPoolConfig poolConfig){
			JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
			/**
			 * 从application.properties配置文件自动装配
			 */
	//		//设置连接池
	//		connectionFactory.setPoolConfig(poolConfig);
	//		//设置主机地址
	//		connectionFactory.setHostName("localhost");
	//		//设置端口
	//		connectionFactory.setPort(6379);
	//		//设置数据库索引 - Redis默认有16个数据库,这里设置访问第二个数据库
	//		connectionFactory.setDatabase(1);
			return connectionFactory;
		}
		/**
		 * 创建RedisTemplate：执行Redis的操作方法
		 * @param connectionFactory
		 * @return
		 */
		@Bean
		public RedisTemplate<String, Object> getRedisTemplate(JedisConnectionFactory connectionFactory){
			RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
			//关联连接工厂
			template.setConnectionFactory(connectionFactory);
			//设置key的序列化
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new StringRedisSerializer());
			return template;
		}
	}
```

- 存取javabean

	编写测试类：
	
```java
	package com.doosan.sb.test.cache.redis;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	import com.doosan.sb.dao.domain.Tb_Employee;
	import com.doosan.sb.dao.employee.EmployeeDao;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class RedisTest {
		
		@Autowired
		private RedisTemplate<String, Object> redisTemplate;
		@Autowired
		private EmployeeDao employeeDao;
		/**
		 * 存入字符串
		 */
		@Test
		public void testSet(){
			redisTemplate.opsForValue().set("email", "guoqian.cheng@doosan.com");
		}
		/**
		 * 读取字符串
		 */
		@Test
		public void testGet(){
			String email = (String)redisTemplate.opsForValue().get("email");
			System.out.println("Email is : " + email);
		}
		/**
		 * 存放JavaBean
		 */
		@Test
		public void testSetJavaBean(){
			Tb_Employee employee = employeeDao.findOne(1);
			//重置设置序列化
			redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
			redisTemplate.opsForValue().set("employee", employee);
		}
		/**
		 * 读取JavaBean
		 */
		@Test
		public void testGetJavaBean(){
			//重置设置序列化
			redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
			Tb_Employee employee = (Tb_Employee)redisTemplate.opsForValue().get("employee");
			System.out.println("Employee name : " + employee.getName());		
		}
	}
```

- JSON格式存取javabean

	编写测试类：
	
```java
	package com.doosan.sb.test.cache.redis;
	import java.util.HashMap;
	import java.util.Map;

	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
	import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import com.doosan.sb.ApplicationStarter;
	import com.doosan.sb.dao.domain.SysUser;
	import com.doosan.sb.dao.domain.Tb_Employee;
	import com.doosan.sb.dao.employee.EmployeeDao;
	@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
	@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
	public class RedisTest {
		
		@Autowired
		private RedisTemplate<String, Object> redisTemplate;
		@Autowired
		private EmployeeDao employeeDao;
		/**
		 * 存入字符串
		 */
		@Test
		public void testSet(){
			redisTemplate.opsForValue().set("email", "guoqian.cheng@doosan.com");
		}
		/**
		 * 读取字符串
		 */
		@Test
		public void testGet(){
			String email = (String)redisTemplate.opsForValue().get("email");
			System.out.println("Email is : " + email);
		}
		/**
		 * 存放JavaBean
		 */
		@Test
		public void testSetJavaBean(){
			Tb_Employee employee = employeeDao.findOne(1);
			//重置设置序列化
			redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
			redisTemplate.opsForValue().set("employee", employee);
		}
		/**
		 * 读取JavaBean
		 */
		@Test
		public void testGetJavaBean(){
			//重置设置序列化
			redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
			Tb_Employee employee = (Tb_Employee)redisTemplate.opsForValue().get("employee");
			System.out.println("Employee name : " + employee.getName());		
		}
		/**
		 * JSON格式存放JavaBean
		 */
		@Test
		public void testSetJavaBeanJSON(){
			SysUser user = new SysUser();
			user.setBg(1);
			user.setRoleid(1);
			user.setStatus(1);
			user.setTeamid(1);
			user.setTid(2);
			user.setUsercd("20112003");
			user.setUsernm("Jack");
			//重置设置序列化
			redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SysUser.class));
			redisTemplate.opsForValue().set("user", user);
		}
		/**
		 * JSON读取JavaBean
		 */
		@Test
		public void testGetJavaBeanJSON(){
			//重置设置序列化
			redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SysUser.class));
			SysUser user = (SysUser)redisTemplate.opsForValue().get("user");
			System.out.println("User name : " + user.getUsernm());		
		}
	}
```