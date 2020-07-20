package com.doosan.biz.ddic.web
import org.springframework.context.ConfigurableApplicationContext
import com.doosan.biz.ddic.web.system.utils.SpringContextUtils
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients //开启OpenFeign客户端
class DdicWebApplication {
	static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdicWebApplication, args)
		SpringContextUtils.setApplicationContext(context)
	}
}
