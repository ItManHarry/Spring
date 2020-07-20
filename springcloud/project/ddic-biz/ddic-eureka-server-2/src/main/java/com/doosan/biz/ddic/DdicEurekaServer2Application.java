package com.doosan.biz.ddic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@SpringBootApplication
@EnableEurekaServer //开启Eureka服务端自动配置
public class DdicEurekaServer2Application {
	
	public static void main(String[] args) {
		SpringApplication.run(DdicEurekaServer2Application.class, args);
	}
	
}