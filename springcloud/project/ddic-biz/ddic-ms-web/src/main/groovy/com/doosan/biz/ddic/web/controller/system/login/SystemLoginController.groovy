package com.doosan.biz.ddic.web.controller.system.login
import javax.servlet.http.HttpServletRequest
import DSGAuthPkg.Auth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemUser
import com.doosan.biz.ddic.web.system.dao.service.SystemUserService
@Controller
@RequestMapping("/web/system")
class SystemLoginController {
	
	final String SYS_URL = "system/login"
	final String BIZ_URL = "biz"
	@Autowired
	SystemUserService systemUserService
	
	/**
	 * 跳转登录页面
	 * @return
	 */
	@RequestMapping("/login")
	def login() {
		return SYS_URL + "/login"
	}
	/**
	 * 系统登出
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping("/logout")
	def logout(HttpServletRequest  request, Map map) {
		request.getSession().removeAttribute("currentUser")
		return SYS_URL + "/login"
	}
	/**
	 * 跳转主页
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping("/home")
	def home(HttpServletRequest  request, Map map) {
		return BIZ_URL + "/home/index"
	}
	/**
	 * 执行登录
	 * @param name	//账号
	 * @param pwd	//密码
	 * @param vcode	//验证码
	 * @param request
	 * @return
	 */
	@RequestMapping("/enter")
	@ResponseBody
	def enter(String name, String pwd,String vcode, HttpServletRequest  request) {
		println "Vcode from parameter : $vcode"
		String s_vcode = request.getSession().getAttribute("radomValidatorCd").toString() //系统验证码
		println "Vcode from session : $s_vcode"
		def result = [:]
		def code = 1, message = "SUCCESS"
		if(vcode.toLowerCase().equals(s_vcode.toLowerCase())) {
			if("admin".equals(name)) {
				if("Ddic@2019".equals(pwd)) {
					request.getSession().setAttribute("currentUser", "admin")
					request.getSession().setAttribute("currentUserName", "Administrator")
				}else {
					code = 0
					message = "登录失败,请确认账号密码是否正确!"
				}
			}else {
				//检查系统权限
				SystemUser user = systemUserService.getUserByCode(name.toUpperCase())
				//验证用户是否存在
				if(user) {
					//验证账号是否正常在用
					if(user.getStatus() == 0) {
						code = 0
						message = "登录账号已停用!!"
					}else {
						boolean loginResult = false
						//用户类型 - 工厂走AD认证
						if(user.getUserType() == 1 || user.getUserType() == 2) {
							//AD账号登录
							Auth auth = new Auth("authsj.corp.doosan.com", "dsg\\"+name, pwd)
							if(auth.validateUser(name, pwd))
								loginResult = true
						}else {
							if(pwd.equals(user.getPwd()))
								loginResult = true
						}
						//登录结果
						if(loginResult){
							request.getSession().setAttribute("currentUser", name)					//登录账号
							request.getSession().setAttribute("currentUserName", user.getName())	//用户姓名
							request.getSession().setAttribute("currentUserType", user.getUserType())//用户类型
						}else{
							code = 0
							message = "登录失败,请确认账号密码是否正确!"
						}
					}
				}else {
					code = 0
					message = "登录账号不存在!!"
				}
			}
		}else {
			code = 0
			message = "验证码错误,请重新输入!"
		}
		result.put("code", code)
		result.put("message",message)
		return result
	}
}