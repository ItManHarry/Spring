package com.doosan.biz.ddic.common
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
@SpringBootApplication
class DdicCommonApplication {
	static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdicCommonApplication, args)
	}
}
