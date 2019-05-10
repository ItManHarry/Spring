package com.doosan.spring.beans;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Animal {
	@PostConstruct
	public void init(){
		System.out.println("Do init method of Animal class");
	}
	@PreDestroy
	public void destroy(){
		System.out.println("Do destroy method of Animal class");
	}	
}
