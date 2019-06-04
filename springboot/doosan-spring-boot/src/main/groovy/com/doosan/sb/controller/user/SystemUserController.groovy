package com.doosan.sb.controller.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import com.doosan.sb.dao.domain.SysUser
import com.doosan.sb.service.user.SysUserService
@Controller
@RequestMapping("/system/user")
class SystemUserController {
	/**
	 * 跳转到新增画面
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	def add(SysUser user){
		return "view/thymeleaf/user/add"
	}
	/**
	 * 执行保存
	 * @param user
	 * @return
	 */
	@RequestMapping("/save")
	def save(SysUser user){
		sysUserService.save(user)
		return "view/thymeleaf/user/success"
	}
	/**
	 * 跳转到用户清单
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	def list(Map map){
		def list = sysUserService.all()
		map.put("list", list)
		return "view/thymeleaf/user/list"
	}
	/**
	 * 跳转到修改画面
	 * @param tid
	 * @param map
	 * @return
	 */
	@RequestMapping("/modify")
	def modify(int tid, Map map){
		def user = sysUserService.getUserById(tid)
		map.put("user", user)
		return "view/thymeleaf/user/update"
	}
	/**
	 * 执行修改,然后跳转到用户清单页面
	 * @param user
	 * @param map
	 * @return
	 */
	@RequestMapping("/update")
	def update(SysUser user, Map map){
		sysUserService.update(user)
		return list(map)
	}
	/**
	 * 执行删除,然后跳转到用户清单页面
	 * @param user
	 * @param map
	 * @return
	 */
	@RequestMapping("/delete")
	def delete(int tid, Map map){
		sysUserService.delete(tid)
		return list(map)
	}
	@Autowired
	private SysUserService sysUserService;
}
