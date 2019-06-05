package com.doosan.sb.controller.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
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
		println "Go to the user list page 999 ..."
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
		String name = null	//用于error测试
		name.toUpperCase()
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
		def i = 100 / 0 //用于error测试
		sysUserService.delete(tid)
		return list(map)
	}
	
//	@ExceptionHandler(value=[java.lang.ArithmeticException])
//	ModelAndView handlerArithmeticException(Exception e){
//		ModelAndView mv = new ModelAndView()
//		mv.addObject("exception", e.toString())
//		mv.setViewName("view/thymeleaf/user/arithmeticError")
//		return mv
//	}
//	@ExceptionHandler(value=[java.lang.NullPointerException])
//	ModelAndView handlerNullPointerException(Exception e){
//		ModelAndView mv = new ModelAndView()
//		mv.addObject("exception", e.toString())
//		mv.setViewName("view/thymeleaf/user/nullPointerError")
//		return mv
//	}
	
	@Autowired
	private SysUserService sysUserService;
}
