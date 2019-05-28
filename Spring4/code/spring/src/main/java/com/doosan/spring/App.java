package com.doosan.spring;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.doosan.spring.beans.*;
import com.doosan.spring.config.DoosanConfiguration;
/**
 * Spring4
 */
public class App{
    public static void main( String[] args ){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DoosanConfiguration.class, User.class, UserDao.class, UserService.class,UserController.class);
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
        System.out.println(context.getBean(User.class));
        System.out.println(context.getBeansOfType(User.class));
        System.out.println(context.getBean(UserDao.class));
        System.out.println(context.getBean(UserService.class));
        System.out.println(context.getBean(UserController.class));
        User user = context.getBean(User.class);
        user.show();
        context.close();
    }
}