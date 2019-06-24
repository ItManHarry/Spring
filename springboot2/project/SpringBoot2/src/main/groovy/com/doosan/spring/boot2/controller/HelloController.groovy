package com.doosan.spring.boot2.controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
@RestController
class HelloController {
	@GetMapping("/hello")
	String hello(){
		println "The get method..."
		return "Hello SpringBoot2"
	}
}