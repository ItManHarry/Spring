package com.doosan.spring.beans;
import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component("myUser")
public class User {

	public void show(){
		System.out.println("Service is : " + service);
		service.executeDao();
		car.show();
		cat.show();
	}
	@Autowired 	
	private UserService service;
	//JSR 250注解 
	@Resource
	private Car car;
	//JSR 330注解 
	@Inject
	private Cat cat;
}
