# Spring Boot2

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

