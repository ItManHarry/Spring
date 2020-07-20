package com.doosan.biz.ddic.hr.controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.doosan.biz.ddic.common.utils.JsonUtils
@RestController
@RequestMapping("/demo")
class StartController {

	@GetMapping("/hi")
	def hi(){
		def map = [:]
		def list = []
		(1..10).each{
			list << it
		}
		map.put("name", "Harry")
		map.put("age", 36)
		map.put("birthday", "1983-11-02")
		map.put("message", "Hi,Groovy Spring Boot.")
		map.put("data", list)
		return JsonUtils.objectToJson(map)
	}
}