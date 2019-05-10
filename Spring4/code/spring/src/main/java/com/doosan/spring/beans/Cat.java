package com.doosan.spring.beans;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class Cat implements InitializingBean,DisposableBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("-----------------afterPropertiesSet of class Cat----------------------");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("-----------------destroy of class Cat----------------------");
		
	}
}