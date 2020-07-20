package com.doosan.biz.ddic.doc
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
@SpringBootApplication
@EnableEurekaClient
class DdicDocumentApplication {
	static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdicDocumentApplication, args)
	}
}
