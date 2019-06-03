package com.doosan.sb.controller.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import com.doosan.sb.dao.domain.SysUser
import com.doosan.sb.service.SysUserService
@Controller
@RequestMapping("/system/user")
class SystemUserController {
	
	@RequestMapping("/add")
	def add(SysUser user){
		return "view/thymeleaf/user/add"
	}
	
	@RequestMapping("/save")
	def save(SysUser user){
		sysUserService.save(user)
		return "view/thymeleaf/user/success"
	}
	@Autowired
	private SysUserService sysUserService;
}
