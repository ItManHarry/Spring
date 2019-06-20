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