package com.doosan.spring.boot2.controller.login
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.doosan.spring.boot2.dao.entity.User
import com.doosan.spring.boot2.result.ResponseResultObject
import com.doosan.spring.boot2.result.ResponseResults
import com.doosan.spring.boot2.service.UserService
import com.doosan.spring.boot2.result.ResponseResultJson
@RestController
@RequestMapping("/system")
class LoginController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	ResponseResultObject<User> login(Integer id, String password){
		User user = userService.login(id, password)
		return ResponseResultJson.success(user)
	}
}