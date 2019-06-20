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
