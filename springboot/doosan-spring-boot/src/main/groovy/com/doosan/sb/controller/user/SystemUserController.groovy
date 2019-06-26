package com.doosan.sb.controller.user
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import com.doosan.sb.dao.domain.SysUser
import com.doosan.sb.service.user.SysUserService
import com.doosan.sb.app.response.ResponseResultObject
import com.doosan.sb.app.response.ResponseResultJson
import com.doosan.sb.app.results.ResponseResults
@Controller
@RequestMapping("/system/user")
class SystemUserController {
	/**
	 * 跳转到新增画面
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	def add(SysUser user, Map map){
		map.put("user", user)
		return "view/thymeleaf/user/add"
	}
	/**
	 * 执行保存
	 * @param user
	 * @return
	 */
	@RequestMapping("/save")
	def save(@Valid @ModelAttribute("user") SysUser user, BindingResult result, Map map){
		if(result.hasErrors()){
			println "The validation is not passed, user name or code is empty..."
			return add(user, map)
		}
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
	/**
	 * 统一返回接口数据
	 * @param id
	 * @return
	 */
	@GetMapping("/rest/json/{id}")
	@ResponseBody
	ResponseResultObject<SysUser> getJson1(@PathVariable("id") Integer id){
		SysUser user = sysUserService.getUserById(id)
		if(user)
			return ResponseResultJson.success(user)
		else
			return ResponseResultJson.error(ResponseResults.NOTFOUND)
	}
	/**
	 * 统一返回接口数据
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	@ResponseBody
	ResponseResultObject<SysUser> getJson2(Integer id){
		SysUser user = sysUserService.getUserById(id)
		if(user)
			return ResponseResultJson.success(user)
		else
			return ResponseResultJson.error(ResponseResults.NOTFOUND)
	}
	/**
	 * 统一返回接口数据
	 * @param id
	 * @return
	 */
	@GetMapping("/all")
	@ResponseBody
	ResponseResultObject<List<SysUser>> getAll(){
		List<SysUser> users = sysUserService.all()
		if(users)
			return ResponseResultJson.success(users)
		else
			return ResponseResultJson.error(ResponseResults.NOTFOUND)
	}
	
	@Autowired
	private SysUserService sysUserService;
}
