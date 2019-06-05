package com.doosan.sb.controller.login
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/system")
class LoginController {
	@RequestMapping("/login")
	def login(){
		String name = null
		name.toLowerCase()
		def result = [:]
		result.put("username", "20112004")
		result.put("pwd", "12345678")
		return result
	}
}
