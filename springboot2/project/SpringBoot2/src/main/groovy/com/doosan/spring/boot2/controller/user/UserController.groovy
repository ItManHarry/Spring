package com.doosan.spring.boot2.controller.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import com.doosan.spring.boot2.dao.UserDao
import com.doosan.spring.boot2.dao.entity.User
import reactor.core.publisher.Mono
@RestController
class UserController {
	@Autowired
	private UserDao userDao
	
	@PostMapping("/user/save")
	User saveUser(String name, Integer age, String remark){
		User user = new User()
		user.setAge(age)
		user.setName(name)
		user.setRemark(remark)
		boolean r = userDao.save(user)
		if(r)
			println "The user has been saved succefully..." + user
		return user
	}
	/**
	 * 改用webflux框架后,必须返回Mono或Flux
	 * 1.如果返回的是0 -> 1个元素,采用Mono
	 * 2.如果返回的是0 -> n个元素,采用Flux
	 * @param id
	 * @return
	 */
	@GetMapping("/user/get")
	Mono<User> getUser(Integer id){
		return Mono.just(userDao.getUserById(id))
	}	
}