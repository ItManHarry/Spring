package com.doosan.spring;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.doosan.spring.beans.scan.*;
/**
 * Spring4
 */
public class App2{
    public static void main( String[] args ){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.doosan.spring.beans.scan");
        System.out.println(context.getBean(Employee.class));
        context.close();
    }
}