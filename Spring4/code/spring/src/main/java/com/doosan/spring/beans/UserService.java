package com.doosan.spring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	public void executeDao(){
		dao.execute();
	}
	@Autowired
	private UserDao dao;
}
