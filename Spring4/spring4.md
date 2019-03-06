# Spring4

## Spring4 Bean装配

	Spring4基于注解(org.springframework.context.annotation.AnnotationConfigApplicationContext)

- 装配方式一，使用配置类

	1.编写bean类
	
```java
	package com.doosan.spring.beans;
	public class Dog {

		public String toString(){
			return "It is a Dog " + this.hashCode();
		}
	}
```
	2.编写配置文件

```java
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
```

	3.编写bean读取类
	
```java
	package com.doosan.spring;
	import org.springframework.context.annotation.AnnotationConfigApplicationContext;
	import com.doosan.spring.beans.Dog;
	import com.doosan.spring.config.DoosanConfiguration;
	/**
	 * Spring4
	 */
	public class App{
		public static void main( String[] args ){
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DoosanConfiguration.class);
			System.out.println(context.getBean(Dog.class));	//根据类实例获取
			System.out.println(context.getBean("dog"));		//根据bean名称获取
			context.close();
		}
	}
```