package com.doosan.biz.ddic.gateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
@SpringBootApplication
@EnableZuulProxy	//启用Zuul网关代理功能
public class DdicGatewayServerStarter {
	
	public static void main(String[] args) {
		SpringApplication.run(DdicGatewayServerStarter.class, args);
	}
}
