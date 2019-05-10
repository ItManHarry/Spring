package com.doosan.spring.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.doosan.spring.beans.Animal;
import com.doosan.spring.beans.Bare;
import com.doosan.spring.beans.Car;
import com.doosan.spring.beans.CarFactory;
import com.doosan.spring.beans.Cat;
import com.doosan.spring.beans.Dog;
import com.doosan.spring.beans.RunnableFactoryBean;
@Configuration
public class DoosanConfiguration {
	@Bean(name="dog")	//设置bean名称,可以按照名称获取bean	
	@Scope("prototype")	//设置是否单例模式
	public Dog getDog(){
		return new Dog();
	}
	@Bean
	public RunnableFactoryBean getRunnableFactoryBean(){
		return new RunnableFactoryBean();
	}
	
	@Bean
	public CarFactory getCarFactory(){
		System.out.println("The car factory is already for the creating now ...");
		return new CarFactory();
	}
	
	@Bean
	public Car createCar(CarFactory factory){
		System.out.println("The car factory will create a new car for you .");
		return factory.createCar();
	}
	@Bean
	public Cat getCat(){
		return new Cat();
	}
	@Bean(initMethod="init",destroyMethod="destroy")
	public Bare getBare(){
		return new Bare();
	}
	@Bean
	public Animal getAnimal(){
		return new Animal();
	}
}