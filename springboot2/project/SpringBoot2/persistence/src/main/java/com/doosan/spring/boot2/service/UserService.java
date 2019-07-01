package com.doosan.spring.boot2.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doosan.spring.boot2.dao.UserDao;
import com.doosan.spring.boot2.dao.entity.User;
@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public User login(Integer id, String password) throws Exception{
		User user = userDao.getUserById2(id);
		if(user == null){
			System.out.println("User is not exist.");
			throw new Exception("User is not exist.");
		}
		if(!user.getPassword().equals(password)){
			System.out.println("Password is not right.");
			throw new Exception("Password is not right.");
		}
		return user;
	}
}