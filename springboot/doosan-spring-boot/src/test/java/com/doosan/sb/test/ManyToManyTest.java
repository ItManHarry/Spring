package com.doosan.sb.test;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.Tb_Role;
import com.doosan.sb.dao.domain.Tb_User;
import com.doosan.sb.dao.role.RoleDao;
import com.doosan.sb.dao.user.UserDao;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class ManyToManyTest {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	
	@Test
	public void testSaveUserAndRole(){
		//Create user
		Tb_User user = new Tb_User();
		user.setName("Harry");
		user.setPassword("12345678");
		//Create role
		Tb_Role roleA = new Tb_Role();
		roleA.setName("RoleA");
		Tb_Role roleB = new Tb_Role();
		roleB.setName("RoleB");
		//connect user and role
		user.getRoles().add(roleA);
		user.getRoles().add(roleB);
		roleA.getUsers().add(user);
		roleB.getUsers().add(user);
		//save data
		userDao.save(user);
	}	
	@Test
	public void testSaveUser(){
		Tb_User user = new Tb_User();
		user.setName("UserD");
		user.setPassword("1234ABCD");
//		user.getRoles().add(roleDao.findOne(1));
//		user.getRoles().add(roleDao.findOne(2));
		userDao.save(user);
	}
	@Test
	public void testSaveRole(){
		Tb_Role role = new Tb_Role();
		role.setName("RoleD");
		userDao.findOne(5).getRoles().add(role);
		userDao.findOne(6).getRoles().add(role);
		role.getUsers().add(userDao.findOne(5));
		role.getUsers().add(userDao.findOne(6));
		roleDao.save(role);
	}
	@Test
	public void testUpdateUser(){
		Tb_User user = userDao.findOne(5);
//		user.getRoles().clear();
		user.getRoles().add(roleDao.findOne(3));
		user.getRoles().add(roleDao.findOne(5));
		userDao.save(user);
	}
	@Test
	public void testQuery(){
		Tb_User user = userDao.findOne(5);
		Iterator<Tb_Role> roles = user.getRoles().iterator();
		while(roles.hasNext()){
			Tb_Role role = roles.next();
			System.out.println("User : " + user.getName() + ", role Name : " + role.getName());
		}
		Tb_Role role = roleDao.findOne(3);
		Iterator<Tb_User> users = role.getUsers().iterator();
		while(users.hasNext()){
			Tb_User u = users.next();
			System.out.println("Role : " + role.getName() + ", user name is : " + u.getName());
		}
	}
} 