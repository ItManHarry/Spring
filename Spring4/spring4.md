# Spring4

## Spring4 Bean装配

	Spring4基于注解(org.springframework.context.annotation.AnnotationConfigApplicationContext)

- 装配方式一，使用配置类

	- 编写bean类
	
```java
	package com.doosan.spring.beans;
	public class Dog {

		public String toString(){
			return "It is a Dog " + this.hashCode();
		}
	}
```

- 装配方式二，使用工厂bean1

	- 编写基础类

```java
	package com.doosan.spring.beans;

	public class Jeep {

	}	
```

	- 编写工厂类

```java
	package com.doosan.spring.beans;
	import org.springframework.beans.factory.FactoryBean;
	public class RunnableFactoryBean implements FactoryBean<Jeep> {

		@Override
		public Jeep getObject() throws Exception {
			return new Jeep();
		}

		@Override
		public Class<?> getObjectType() {
			return Jeep.class;
		}

		@Override
		public boolean isSingleton() {
			return false;
		}
	}	
```

- 装配方式三，使用工厂bean2

	- 编写基础类

```java
	package com.doosan.spring.beans;

	public class Car {

	}
	
```

	- 编写工厂类

```java
	package com.doosan.spring.beans;

	public class CarFactory {

		public Car createCar(){
			return new Car();
		}
	}
```

- 装配方式四，实现InitializingBean接口
	
	- 编写bean类

```java
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
```

- 装配方式五，自定义初始化、销毁方法

	- 编写bean类

```java
	package com.doosan.spring.beans;

	public class Bare {

		public void init(){
			System.out.println("------------init bare class-------------");
		}
		
		public void destroy(){
			System.out.println("------------destroy the bare class------------");
		}
	}
```

- 装配方式六，通过注解的方法实现初始化、销毁方法

	- 编写bean类

```java
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
```

- 编写配置文件

```java
	package com.doosan.spring.config;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.context.annotation.Scope;
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
```

- 编写bean读取类
	
```java
	package com.doosan.spring;
	import org.springframework.context.annotation.AnnotationConfigApplicationContext;
	import com.doosan.spring.beans.Animal;
	import com.doosan.spring.beans.Bare;
	import com.doosan.spring.beans.Car;
	import com.doosan.spring.beans.Cat;
	import com.doosan.spring.beans.Dog;
	import com.doosan.spring.beans.Jeep;
	import com.doosan.spring.beans.RunnableFactoryBean;
	import com.doosan.spring.config.DoosanConfiguration;
	/**
	 * Spring4
	 */
	public class App{
		public static void main( String[] args ){
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DoosanConfiguration.class);
			System.out.println(context.getBean(Dog.class));	//根据类实例获取
			System.out.println(context.getBean("dog"));		//根据bean名称获取
			System.out.println(context.getBean(Jeep.class));
			System.out.println(context.getBean("getRunnableFactoryBean"));
			System.out.println(context.getBean("&getRunnableFactoryBean"));
			System.out.println(context.getBean(RunnableFactoryBean.class));
			System.out.println(context.getBean(Car.class));
			System.out.println(context.getBean(Cat.class));
			System.out.println(context.getBean(Bare.class));
			System.out.println(context.getBean(Animal.class));
			context.close();
		}
	}
```