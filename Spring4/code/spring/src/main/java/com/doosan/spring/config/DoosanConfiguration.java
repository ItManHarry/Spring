package com.doosan.spring.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import com.doosan.spring.beans.Dog;
@Configuration
public class DoosanConfiguration {
	@Bean(name="dog")	//设置bean名称,可以按照名称获取bean	
	@Scope("prototype")	//设置是否单例模式
	public Dog getDog(){
		return new Dog();
	}
}