package com.doosan.biz.ddic.web.controller.system.users
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemUser
import com.doosan.biz.ddic.web.system.dao.service.SystemUserService
@Controller
@RequestMapping("/web/system/user")
class UserController {
	
	final String WEB_URL = "system/users"
	@Autowired
	SystemUserService systemUserService
	
	/**
	 *	 跳转用户清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		if(systemUserService.getCount())
			map.put("total", systemUserService.getCount().intValue())
		else
			map.put("total", 0)	
		return WEB_URL + "/list"
	}
	/**
	 * 	获取数据
	 * 	@param order
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(Integer page, Integer limit){
		def count = systemUserService.getCount() ? systemUserService.getCount().intValue() : 0
		def data = systemUserService.getAllByPages(page, limit)
		data.each {
			if(it.getStatus() == 0)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 根据姓名和code进行模糊分页查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @return
	 */
	@ResponseBody
	@GetMapping("/query")
	def query(Integer page, Integer limit, String name, String code){
		def count = systemUserService.getCountByNameAndCode(name, code) ? systemUserService.getCountByNameAndCode(name, code).intValue() : 0
		def data = systemUserService.findByNameAndCode(page, limit, name, code)
		data.each {
			if(it.getStatus() == 0)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 	根据ID停用用户
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/move")
	def move(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemUser user = systemUserService.getUserById(id)
		user.setStatus(0)
		user.setDelflag("1")
		user.setModifytime(new Date())
		user.setModifyuserid(userId)
		systemUserService.save(user)
		return list(map)
	}
	/**
	 * 	根据ID恢复用户
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/recover")
	def recover(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemUser user = systemUserService.getUserById(id)
		user.setStatus(1)
		user.setDelflag("0")
		user.setModifytime(new Date())
		user.setModifyuserid(userId)
		systemUserService.save(user)
		return list(map)
	}
	/**
	 * 	保存用户
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemUser user = new SystemUser()
		int id = Integer.parseInt(request.getParameter("id"))
		if(id != 0) {
			user = systemUserService.getUserById(id);
			user.setModifytime(new Date())
			user.setModifyuserid(userId)
			user.setRowversion(user.getRowversion()+1)
		}else {
			def d = systemUserService.getUserByCode(request.getParameter("code").toUpperCase())
			if(d != null)
				return ServerResultJson.error(0, "用户已存在,请勿重复添加!", "")
			user.setCreatetime(new Date())
			user.setCreateuserid(userId)
			user.setDelflag("0")
		}
		user.setCode(request.getParameter("code").toUpperCase())
		user.setName(request.getParameter("name"))
		user.setStatus(1)
		user.setSysRole(0)		//此处先设置为固定值,后续修改
		user.setDocRole(Integer.parseInt(request.getParameter("dr")))
		user.setUserType(Integer.parseInt(request.getParameter("ut")))
		user.setPwd(request.getParameter("pwd"))
		//user.setDocRole(request.getParameter("docRole"))
		systemUserService.save(user)
		return ServerResultJson.success()
	}
}
