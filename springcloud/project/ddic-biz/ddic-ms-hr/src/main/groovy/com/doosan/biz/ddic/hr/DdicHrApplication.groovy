package com.doosan.biz.ddic.hr
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
@SpringBootApplication
@EnableEurekaClient
class DdicHrApplication {
	static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdicHrApplication, args)
	}
}
