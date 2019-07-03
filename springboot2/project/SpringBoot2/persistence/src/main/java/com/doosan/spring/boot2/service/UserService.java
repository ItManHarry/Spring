package com.doosan.spring.boot2.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doosan.spring.boot2.dao.UserDao;
import com.doosan.spring.boot2.dao.entity.User;
import com.doosan.spring.boot2.exception.SystemException;
import com.doosan.spring.boot2.result.ResponseResults;
@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public User login(Integer id, String password) throws Exception{
		User user = userDao.getUserById2(id);
		if(user == null){
			System.out.println("User is not exist.");
			throw new SystemException(1000, "User is not exist.");
		}
		if(!user.getPassword().equals(password)){
			System.out.println("Password is not right.");
			throw new SystemException(ResponseResults.ERROR_WRONGPWD);
		}
		return user;
	}
}