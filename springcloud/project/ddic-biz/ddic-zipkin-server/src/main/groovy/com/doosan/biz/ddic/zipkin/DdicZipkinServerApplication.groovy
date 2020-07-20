package com.doosan.biz.ddic.zipkin
import org.springframework.context.ConfigurableApplicationContext
import zipkin.server.internal.EnableZipkinServer
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
@SpringBootApplication
@EnableZipkinServer
class DdicZipkinServerApplication {
	static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DdicZipkinServerApplication, args)
	}
}