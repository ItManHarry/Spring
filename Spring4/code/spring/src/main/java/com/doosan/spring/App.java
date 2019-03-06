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