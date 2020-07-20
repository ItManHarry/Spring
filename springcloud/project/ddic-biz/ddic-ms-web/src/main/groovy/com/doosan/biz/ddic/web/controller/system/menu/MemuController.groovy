package com.doosan.biz.ddic.web.controller.system.menu
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemMenu
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemModule
import com.doosan.biz.ddic.web.system.dao.service.SystemAuthedService
import com.doosan.biz.ddic.web.system.dao.service.SystemMenuService
import com.doosan.biz.ddic.web.system.dao.service.SystemModuleService
import com.doosan.biz.ddic.web.system.dao.service.SystemUserService
@Controller
@RequestMapping("/web/system/menu")
class MemuController {
	
	final String WEB_URL = "system/menu"
	@Autowired
	SystemModuleService systemModuleService
	@Autowired
	SystemMenuService systemMenuService	
	@Autowired
	SystemAuthedService systemAuthedService
	/**
	 *	 跳转菜单清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		println "Menu total is : " + systemMenuService.getCount()
		if(systemMenuService.getCount())
			map.put("total", systemMenuService.getCount().intValue())
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
		def modules = [:]
		List<SystemModule> am = systemModuleService.getAll()
		am.each { 
			modules.put(it.getTid(), it.getModuleNm())
		}
		def count = systemMenuService.getCount() ? systemMenuService.getCount().intValue() : 0 
		def data = systemMenuService.getAllByPages(page, limit)
		data.each {
			if(it.getDelflag() == "1")
				it.setStatus("停用")
			else
				it.setStatus("在用")
			it.setModuleNm(modules.get(it.getModule()))
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 	根据ID停用菜单
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/move")
	def move(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemMenu menu = systemMenuService.getMenuById(id)
		menu.setDelflag("1")
		menu.setModifytime(new Date())
		menu.setModifyuserid(userId)
		systemMenuService.save(menu)
		return list(map)
	}
	/**
	 * 	根据ID恢复菜单
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/recover")
	def recover(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemMenu menu = systemMenuService.getMenuById(id)
		menu.setDelflag("0")
		menu.setModifytime(new Date())
		menu.setModifyuserid(userId)
		systemMenuService.save(menu)
		return list(map)
	}
	/**
	 * 	保存菜单
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemMenu menu = new SystemMenu()
		int id = Integer.parseInt(request.getParameter("id"))
		if(id != 0) {
			menu = systemMenuService.getMenuById(id);
			menu.setModifytime(new Date())
			menu.setModifyuserid(userId)
			menu.setRowversion(menu.getRowversion()+1)
		}else {
			menu.setCreatetime(new Date())
			menu.setCreateuserid(userId)
			menu.setDelflag("0")
		}
		menu.setModule(Integer.parseInt(request.getParameter("module")))
		menu.setName(request.getParameter("name"))
		menu.setUrl(request.getParameter("url"))	
		systemMenuService.save(menu)
		return ServerResultJson.success()
	}
	/**
	 * 	获取模块下授权的菜单(mms - module menus)
	 * 	@param request
	 * 	@return
	 */
	@GetMapping("/mms")
	@ResponseBody
	def mms(HttpServletRequest request) {
		def menus = []
		def module = Integer.parseInt(request.getParameter("module"))
		def user = request.getSession().getAttribute("currentUser").toString()
		if(user == "admin") {
			menus = systemMenuService.getAll().findAll{
				it.getDelflag() == "0" && it.getModule() == module
			}
		}else {
			//获取授权信息
			def authed = systemAuthedService.findByUser(user).findAll{
				it.getModule() == module && it.getMenu() != module
			}
			//已授权菜单ID(authorized menus)
			def ams = []
			authed.each {
				ams << it.getMenu()
			}
			//获取菜单
			menus = systemMenuService.getByModule(module).findAll{
				//在用同时已授权给该用户的菜单
				it.getDelflag() == "0" && ams.contains(it.getTid())
			}
		}
		return ServerResultJson.success(menus)
	}	
}