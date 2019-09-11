# Spring Boot2

## Linux安装Redis

- 下载源码，解压缩

	wget http://download.redis.io/release/redis-5.0.3.tar.gz
	
	tar -zxvf redis-5.0.3.tar.gz
	
	cd redis-5.0.3
	
- 进入src目录，编译安装

	make编译操作	
		注意：
			1 . make编译需要C语言编译器gcc的支持，如有没有，需要安装gcc。可以使用rpm -q gcc查看是否安装，如果没有安装，使用yum -y install gcc进行安装；
			2 . 如果编译出错，请使用make clean清除临时文件之后，找到出错原因，解决问题后再重新安装）
	
	cd src
	make install
	
- 比较重要的三个可执行文件

	1 . redis-server : Redis服务器程序
	
	2 . redis-cli ：Redis客户端程序
	
	3 . redis-benchmark : Redis性能测试工具，测试Redis在系统及配置下的读写性能
	
- 编译完成后， 在src目录下，将以下四个文件拷贝到一个目录下(mkdir /usr/redis)

	cp redis-server/redis-cli/redis-benchmark/../redis.conf /usr/redis
	
- 启动Redis服务

	redis-server redis.conf
	
- 开机启动Redis

	1. 进入redis下的utils文件夹
	
		cd /usr/redis/utils
		
	2. 打开并编辑redis_init_script
	
		vi redis_init_script
		
	3. 将以下配置改成实际的路径
	
		EXEC=/usr/redis/redis-server
		CLIEXEC=/usr/redis/redis-cli
		CONF="/usr/redis/redis.conf"
		
		注意：PIDFILE先去/var/run下看看有没有redis打头的pdi文件，没有的话先去redis-x.x.x/src下执行“./redis-server ../redis.conf”,手动执行redis，pid文件就会生成
		
	4. 将redis_init_script拷贝到/etc/init.d目录下，修改名称为redis
	
		cp redis_init_script /etc/init.d/
		cd /etc/init.d/
		mv redis_init_script redis
	
	5. 修改读写权限
	
		chmod +x /etc/init.d/redis
		
	6. 测试启动停止redis
		
			service redis start
			
			service redis stop
			
	7. 开机自启动
	
		chkconfig redis on
		
	8. 加入开机自启动
	
		chkconfig --add redis

## SpringBoot集成redis

- pom.xml引入redis&pool2包

```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-redis</artifactId>
	</dependency>
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
	</dependency>
	<dependency>        
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-annotations</artifactId>
	</dependency>
```

- 配置redis(application.properties)

```
	#configure redis cache
	spring.cache.type=redis
	spring.redis.database=0
	spring.redis.host=10.40.123.210
	spring.redis.port=6379
	spring.redis.password=
	spring.redis.lettuce.shutdown-timeout=100
	spring.redis.lettuce.pool.max-active=8
	spring.redis.lettuce.pool.max-wait=-1
	spring.redis.lettuce.pool.max-idle=8
	spring.redis.lettuce.pool.min-idle=0
```

- 编写工具类(字符串存取工具)

```java
	package com.ch.sys.biz.system.cache.redis;
	import java.util.Map;
	import java.util.Set;
	import java.util.concurrent.TimeUnit;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.redis.core.StringRedisTemplate;
	import org.springframework.stereotype.Component;
	@Component
	public class RedisFactoryString {

		@Autowired
		private StringRedisTemplate stringRedisTemplate;
		
		public long ttl(String key) {
			return stringRedisTemplate.getExpire(key);
		}
		
		public void expire(String key, long timeout) {
			stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
		
		public long incr(String key, long delta) {
			return stringRedisTemplate.opsForValue().increment(key, delta);
		}
		
		public long getIncr(String key, long timeout, int maxnil) {
			Long id = null;
			id = stringRedisTemplate.opsForValue().increment(key, 1);
			if(timeout > 0)
				stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
			if(id > maxnil)
				stringRedisTemplate.delete(key);
			return id;
		}
		
		public Set<String> keys(String pattern){
			return stringRedisTemplate.keys(pattern);
		}
		
		public void del(String key) {
			stringRedisTemplate.delete(key);
		}
		
		public void set(String key, String value) {
			stringRedisTemplate.opsForValue().set(key, value);
		}
		
		public void set(String key, String value, long timeout) {
			stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
		}
		
		public String get(String key) {
			return stringRedisTemplate.opsForValue().get(key);
		}
		
		public void hset(String key, String field, String value) {
			stringRedisTemplate.opsForHash().put(key, field, value);
		}
		
		public String hget(String key, String field) {
			return (String)stringRedisTemplate.opsForHash().get(key, field);
		}
		
		public void hdel(String key, Object... keys) {
			stringRedisTemplate.opsForHash().delete(key, keys);
		}
		
		public Map<Object, Object> hgetall(String key){
			return stringRedisTemplate.opsForHash().entries(key);
		}
		
		public long lpush(String key, String value) {
			return stringRedisTemplate.opsForList().leftPush(key, value);
		}
		
		public String lpop(String key) {
			return (String)stringRedisTemplate.opsForList().leftPop(key);
		}
		
		public long rpush(String key, String value) {
			return stringRedisTemplate.opsForList().rightPush(key, value);
		}
	}
```
- 编写工具类(Json存取工具)

```java
	package com.ch.sys.biz.system.cache.redis;
	import java.util.concurrent.TimeUnit;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.stereotype.Component;
	@Component
	public class RedisFactoryJson {

		@Autowired
		private RedisTemplate<Object, Object> redisTemplateJson;
		/**
		 *	实现命令TTL key,返回给定key的剩余生存时间(TTL Time To Live)
		 * @param key
		 * @return
		 */
		public long ttl(String key) {
			return redisTemplateJson.getExpire(key);
		}
		/**
		 * 	实现命令expire,设置过期时间(单位秒)
		 * @param key
		 * @param timeout
		 */
		public void expire(String key, long timeout) {
			redisTemplateJson.expire(key, timeout, TimeUnit.SECONDS);
		}
		/**
		 * 	实现DEL命令,删除一个key
		 * @param key
		 */
		public void del(String key) {
			redisTemplateJson.delete(key);
		}
		/**
		 * 	实现set命令,设置key值
		 * @param key
		 * @param value
		 */
		public void set(String key, Object value) {
			redisTemplateJson.opsForValue().set(key, value);
		}
		/**
		 * 	实现set命令,设置key值及超时时间
		 * @param key
		 * @param value
		 */
		public void set(String key, Object value, long timeout) {
			redisTemplateJson.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
		}
		/**
		 * 	实现get命令,获取key对应的value
		 * @param key
		 * @return
		 */
		public Object get(String key) {
			return redisTemplateJson.opsForValue().get(key);
		}	
	}
```

- 重写Redis序列化类

```java
	package com.ch.sys.biz.system.cache.redis;
	import org.springframework.core.convert.converter.Converter;
	import org.springframework.core.serializer.support.DeserializingConverter;
	import org.springframework.core.serializer.support.SerializingConverter;
	import org.springframework.data.redis.serializer.RedisSerializer;
	import org.springframework.data.redis.serializer.SerializationException;
	/**
	 * 	重写Redis序列化类
	 * @author 20112004
	 *
	 */
	public class RedisObjectSerializer implements RedisSerializer<Object> {

		private Converter<Object, byte[]> serializer = new SerializingConverter();
		private Converter<byte[], Object> deserializer = new DeserializingConverter();
		static final byte[] EMPTY_ARRAY = new byte[0];
		
		private boolean isEmpty(byte[] data) {
			return (data == null || data.length == 0);
		}
		
		@Override
		public byte[] serialize(Object object) {
			if(object == null)
				return EMPTY_ARRAY;
			try {
				return serializer.convert(object);
			}catch(Exception e) {
				return EMPTY_ARRAY;
			}
		}

		@Override
		public Object deserialize(byte[] bytes) {
			if(isEmpty(bytes))
				return null;
			try {
				return deserializer.convert(bytes);
			}catch(Exception e) {
				throw new SerializationException("Can not deserialize", e); 
			}	
		}

	}
```

- 编写注册类

```java
	package com.ch.sys.biz.system.configuration.redis;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.data.redis.connection.RedisConnectionFactory;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
	import org.springframework.data.redis.serializer.StringRedisSerializer;
	import com.ch.sys.biz.system.cache.redis.RedisObjectSerializer;
	import com.fasterxml.jackson.annotation.JsonAutoDetect;
	import com.fasterxml.jackson.annotation.PropertyAccessor;
	import com.fasterxml.jackson.databind.ObjectMapper;
	/**
	 * Redis Configuration
	 * @author 20112004
	 *
	 */
	@Configuration
	public class RedisConfig {
		/**
		 * byte格式序列化
		 * @param factory
		 * @return
		 */
		@Bean
		public RedisTemplate<Object, Object> redisTemplateBytes(RedisConnectionFactory factory){
			RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
			template.setConnectionFactory(factory);
			//开启事务
			template.setEnableTransactionSupport(true);
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new RedisObjectSerializer());
			return template;
		}
		/**
		 * json格式序列化
		 * 	此处将redis使用的默认序列化JDKSerializer更改为了Spring的Serializer
		 * @param factory
		 * @return
		 */
		@Bean
		public RedisTemplate<Object, Object> redisTemplateJson(RedisConnectionFactory factory){
			RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
			template.setConnectionFactory(factory);
			ObjectMapper om = new ObjectMapper();
			om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
			om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
			Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
			jackson2JsonRedisSerializer.setObjectMapper(om);
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(jackson2JsonRedisSerializer);
			template.afterPropertiesSet();
			return template;
		}	
	}
```

- 编写测试Controller

```groovy
	package com.ch.sys.biz.controller.redis
	import com.ch.sys.biz.dao.business.entity.employee.Tb_Employee
	import com.ch.sys.biz.system.cache.redis.RedisFactoryJson
	import com.ch.sys.biz.system.cache.redis.RedisFactoryString
	import com.ch.sys.biz.system.results.ServerResultJson
	import com.ch.sys.biz.system.utils.JsonUtils
	import org.springframework.beans.factory.annotation.Autowired
	import org.springframework.web.bind.annotation.GetMapping
	import org.springframework.web.bind.annotation.RequestMapping
	import org.springframework.web.bind.annotation.RestController
	@RestController
	@RequestMapping("/redis")
	class RedisController {
		
		@Autowired
		private RedisFactoryJson redisFactoryJson
		@Autowired
		private RedisFactoryString redisFactoryString
		
		@GetMapping("/set")
		def set(String value) {
			redisFactoryString.set("str1", value)
			def v = redisFactoryString.get("str1")
			return ServerResultJson.success(v)
		}
		@GetMapping("/set/json")
		def setEntity() {
			Tb_Employee employee = new Tb_Employee()
			employee.setCode("ic20112004")
			employee.setName("Harry.Cheng")
			employee.setAddress("YT ShanDong")
			employee.setEmail("guoqian.cheng@doosan.com")
			employee.setGender(0)
			employee.setJob("IT Dev")
			employee.setMobile("13780924007")
			redisFactoryString.set("employee", JsonUtils.objectToJson(employee))
			String empStr = redisFactoryString.get("employee")
			Tb_Employee re = JsonUtils.jsonToBean(empStr, Tb_Employee)
			return ServerResultJson.success(re)
		}
		@GetMapping("/set/json/entity")
		def setEntityJson() {
			Tb_Employee employee = new Tb_Employee()
			employee.setCode("20112003")
			employee.setName("Jack")
			employee.setAddress("YT ShanDong")
			employee.setEmail("guoqian.cheng@doosan.com")
			employee.setGender(0)
			employee.setJob("IT Dev")
			employee.setMobile("13780924009")
			redisFactoryJson.set("employeeEntity", employee)
			Tb_Employee re = redisFactoryJson.get("employeeEntity")
			return ServerResultJson.success(re)
		}
		
	}
```

## Mybatis使用redis做缓存

- SpringBoot启用缓存(启动类增加@EnableCaching注解)

```java
	package com.ch.sys.biz
	import org.springframework.boot.SpringApplication
	import org.springframework.boot.autoconfigure.SpringBootApplication
	import org.springframework.cache.annotation.EnableCaching
	import org.springframework.context.ConfigurableApplicationContext
	import org.springframework.scheduling.annotation.EnableAsync
	import org.springframework.scheduling.annotation.EnableScheduling
	import com.ch.sys.biz.BizApplication
	import com.ch.sys.biz.system.utils.SpringContextUtils
	/**
	 * 工程启动
	 * @author 20112004
	 * 2019-07-23
	 */
	@SpringBootApplication
	@EnableAsync
	@EnableScheduling
	@EnableCaching
	class BizApplication {

		static void main(String[] args) {
			ConfigurableApplicationContext context = SpringApplication.run(BizApplication, args)
			SpringContextUtils.setApplicationContext(context)
		}

	}
```

- 编写SpringContextUtils工具类

```java
	package com.ch.sys.biz.system.utils;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.context.ApplicationContext;

	public class SpringContextUtils {

		/**
			* 上下文对象实例
		*/
		private static ApplicationContext applicationContext;
		
		private static Logger logger = LoggerFactory.getLogger(SpringContextUtils.class);
		
		private static void assertApplicationContext() {
			if(applicationContext == null)
				throw new IllegalStateException("application未定义,请在application.xml中定义SpringContextHolder");
		}
		
		@Autowired
		public static void setApplicationContext(ApplicationContext context)  {
			logger.info("ApplicationContext has been set");
			applicationContext = context;
		}
	 
		/**
		 * 获取applicationContext
		 * @return
		 */
		public static ApplicationContext getApplicationContext() {
			assertApplicationContext();
			return applicationContext;
		}
		
		 /**
			* 通过name获取 Bean.
			* @param name
			* @return
		 */
		public static Object getBean(String name){
			return getApplicationContext().getBean(name);
		}
	 
		/**
			* 通过class获取Bean.
			* @param clazz
			* @param <T>
			* @return
		 */
		public static <T> T getBean(Class<T> clazz){
			return getApplicationContext().getBean(clazz);
		}
	 
		/**
			* 通过name,以及Clazz返回指定的Bean
			 * @param name
			 * @param clazz
			 * @param <T>
			 * @return
		 */
		public static <T> T getBean(String name,Class<T> clazz){
			return getApplicationContext().getBean(name, clazz);
		}
		
	}
```

	注：此类中的applicationContext实例化的操作在启动类中设置(SpringContextUtils.setApplicationContext(context))：
	
```groovy
	package com.ch.sys.biz
	import org.springframework.boot.SpringApplication
	import org.springframework.boot.autoconfigure.SpringBootApplication
	import org.springframework.cache.annotation.EnableCaching
	import org.springframework.context.ConfigurableApplicationContext
	import org.springframework.scheduling.annotation.EnableAsync
	import org.springframework.scheduling.annotation.EnableScheduling
	import com.ch.sys.biz.BizApplication
	import com.ch.sys.biz.system.utils.SpringContextUtils
	/**
	 * 工程启动
	 * @author 20112004
	 * 2019-07-23
	 */
	@SpringBootApplication
	@EnableAsync
	@EnableScheduling
	@EnableCaching
	class BizApplication {

		static void main(String[] args) {
			ConfigurableApplicationContext context = SpringApplication.run(BizApplication, args)
			SpringContextUtils.setApplicationContext(context)
		}

	}
```

- 编写org.apache.ibatis.cache.Cache接口实现类

```java
	package com.ch.sys.biz.system.cache.redis.mybatis;
	import java.util.Set;
	import java.util.concurrent.TimeUnit;
	import java.util.concurrent.locks.ReadWriteLock;
	import java.util.concurrent.locks.ReentrantReadWriteLock;
	import org.apache.ibatis.cache.Cache;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.dao.DataAccessException;
	import org.springframework.data.redis.connection.RedisConnection;
	import org.springframework.data.redis.core.RedisCallback;
	import org.springframework.data.redis.core.RedisTemplate;
	import com.ch.sys.biz.system.utils.SpringContextUtils;
	/**
	 * 	自定义实现Mybatis的Cache
	 * @author 20112004
	 *
	 */
	public class MybatisRedisCache implements Cache {

		private static Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);
		
		private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
		
		private RedisTemplate<String, Object> redisTemplate;
		
		private String id;
		
		private void getRedisTemplate() {
			if(redisTemplate == null)
				redisTemplate = (RedisTemplate<String, Object>)SpringContextUtils.getBean("redisTemplateJson");
		}
		
		public MybatisRedisCache() {
			
		}
		
		public MybatisRedisCache(final String id) {
			if(id == null)
				throw new IllegalArgumentException("Mybatis Cache Instance requires an ID");
			logger.info("Mybatis Cache ID is : " + id);
			this.id = id;				
		}
		
		@Override
		public String getId() {
			return this.id;
		}

		@Override
		public void putObject(Object key, Object value) {
			// TODO Auto-generated method stub
			if(value != null)
				this.getRedisTemplate();
			redisTemplate.opsForValue().set(key.toString(), value, 1, TimeUnit.DAYS);
		}

		@Override
		public Object getObject(Object key) {
			try {
				if(key != null) {
					this.getRedisTemplate();
					Object o = redisTemplate.opsForValue().get(key.toString());
					return o;
				}
			}catch(Exception e) {
				logger.error("Redis getObject of Mybatis");
			}
			return null;
		}

		@Override
		public Object removeObject(Object key) {
			try {
				if(key != null) {
					this.getRedisTemplate();
					redisTemplate.delete(key.toString());
				}
			}catch(Exception e) {
				logger.error("Redis removeObject of Mybatis");
			}
			return null;
		}

		@Override
		public void clear() {
			System.out.println("--------------do clear the mybatis cache------------------");
			// TODO Auto-generated method stub
			if(logger.isDebugEnabled())
				logger.debug("Clear Redis Cache of Mybatis");
			try {
				this.getRedisTemplate();	
				//使用*通配符获取所有相关key
				Set<String> keys = redisTemplate.keys("*"+this.id+"*");
				if(keys != null && keys.size() != 0) {
					redisTemplate.delete(keys);
				}else {
					System.out.println("-------------------------cache clear keys is empty...-------------------------");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getSize() {
			this.getRedisTemplate();
			
			Long size = (Long)redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					// TODO Auto-generated method stub
					return connection.dbSize();
				}
				
			});
			return size.intValue();
		}

		@Override
		public ReadWriteLock getReadWriteLock() {
			// TODO Auto-generated method stub
			return this.readWriteLock;
		}

	}
```

	注：执行clear方法进行缓存清除时，务必使用"*"通配符(Set<String> keys = redisTemplate.keys("*"+this.id+"*");)

- Mybatis SQL xml文件配置缓存

```xml
	<!-- 设置缓存 -->
	<cache type = "com.ch.sys.biz.system.cache.redis.mybatis.MybatisRedisCache">
		<property name = "eviction" value = "LRU"/><!-- 缓存清除机制：LRU/FIF0/SOFT/WEAK，默认LRU -->
		<property name = "flushInterval" value = "600000"/><!-- 缓存过期时间,若为空,只要空间足够则永不过期 -->
		<property name = "size" value = "1024"/><!-- 缓存个数,默认1024-->
		<property name = "readOnly" value = "false"/><!-- 是否只读，如果为true，则相同sql返回的是同一个对象（有助于提高性能，但是并发操作同一个对象时，可能不安全）。如果是false，则相同的sql后面访问的是cache的副本 -->
	</cache>
```