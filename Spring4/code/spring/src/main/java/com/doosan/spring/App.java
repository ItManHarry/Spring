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