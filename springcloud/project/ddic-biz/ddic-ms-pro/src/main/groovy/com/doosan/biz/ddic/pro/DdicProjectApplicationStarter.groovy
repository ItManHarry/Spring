package com.doosan.biz.ddic.pro
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
@SpringBootApplication
@EnableEurekaClient
class DdicProjectApplicationStarter {
	static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdicProjectApplicationStarter, args)
	}
}
