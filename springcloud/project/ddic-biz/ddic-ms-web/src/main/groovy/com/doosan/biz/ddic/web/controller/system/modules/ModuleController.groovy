package com.doosan.biz.ddic.web.controller.system.modules
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemAuthed
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemModule
import com.doosan.biz.ddic.web.system.dao.service.SystemAuthedService
import com.doosan.biz.ddic.web.system.dao.service.SystemModuleService
import com.doosan.biz.ddic.web.system.dao.service.SystemUserService
@Controller
@RequestMapping("/web/system/module")
class ModuleController {
	
	final String WEB_URL = "system/modules"
	@Autowired
	SystemModuleService systemModuleService
	@Autowired
	SystemAuthedService systemAuthedService
	
	/**
	 *	 跳转模块清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		if(systemModuleService.getCount())
			map.put("total", systemModuleService.getCount().intValue())
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
		def count = systemModuleService.getCount() ? systemModuleService.getCount().intValue() : 0
		def data = systemModuleService.getAllByPages(page, limit)
		data.each {
			if(it.getDelflag() == "1")
				it.setStatus("停用")
			else
				it.setStatus("在用")
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 	获取所有在用模块供菜单维护使用
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/getAll")
	def getAll(){
		def data = systemModuleService.getAll().findAll{
			it.getDelflag() == "0"
		}
		return ServerResultJson.success(data)
	}
	/**
	 * 	获取所有在用及已赋予用户权限的模块供主页展示
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/homeModules")
	def homeModules(HttpServletRequest  request){
		def user = request.getSession().getAttribute("currentUser").toString()
		//系统模块		
		def modules = []
		//超级管理员获取所有在用的模块
		if(user == "admin") {
			modules = systemModuleService.getAll().findAll{
				it.getDelflag() == "0"
			}
		}else {
			//获取授权信息
			def authed = systemAuthedService.findByUser(user)
			//未授权返回
			if(authed == null || authed.size() == 0)
				return ServerResultJson.success([])
			//获取授权模块ID
			def mids = []
			authed.each {
				mids << it.getModule()
			}
			//去重
			mids = mids.unique()
			modules = systemModuleService.getAll().findAll{
				it.getDelflag() == "0" && mids.contains(it.getTid())
			}
		}
		//处理模块URL地址,增加模块ID参数
		modules.each { 
			it.setModuleUrl(it.getModuleUrl()+"?module="+it.getTid())
		}
		/**
		 * 	默认一行显示四个模块
		 * 	后期如要调整,将下列所有的数字减去或在加上模块数的增减量即可
		 */
		def dms = []	//display modules
		if(modules.size() <= 4) {
			dms << modules
		}else {
			def rows = modules.size() % 4 == 0 ? modules.size() / 4 : (modules.size() / 4).toInteger() + 1
			if(modules.size() % 4 == 0){
				for(def row : (0..rows-1)) {
					if(row == 0)
						dms << modules[0..3]
					else
						dms << modules[row*4..row*4+3]
				}
			}else {
				for(def row : (0..rows-1)) {
					if(row == 0)
						dms << modules[0..3]
					else
						dms << modules[row*4..(row*4+3>=modules.size()?modules.size()-1:row*4+3)]
				}
			}
		}		
		return ServerResultJson.success(dms)
	}
	/**
	 * 	根据ID停用模块
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/move")
	def move(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemModule module = systemModuleService.getModuleById(id)
		module.setDelflag("1")
		module.setModifytime(new Date())
		module.setModifyuserid(userId)
		systemModuleService.save(module)
		return list(map)
	}
	/**
	 * 	根据ID恢复模块
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/recover")
	def recover(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemModule module = systemModuleService.getModuleById(id)
		module.setDelflag("0")
		module.setModifytime(new Date())
		module.setModifyuserid(userId)
		systemModuleService.save(module)
		return list(map)
	}
	/**
	 * 	保存模块
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemModule module = new SystemModule()
		int id = Integer.parseInt(request.getParameter("id"))
		if(id != 0) {
			module = systemModuleService.getModuleById(id);
			module.setModifytime(new Date())
			module.setModifyuserid(userId)
			module.setRowversion(module.getRowversion()+1)
		}else {
			module.setCreatetime(new Date())
			module.setCreateuserid(userId)
			module.setDelflag("0")
		}
		module.setModuleNm(request.getParameter("name"))
		module.setModuleUrl(request.getParameter("url"))
		module.setModuleIcon(request.getParameter("icon"))	
		module.setModuleRmk(request.getParameter("remark"))
		systemModuleService.save(module)
		return ServerResultJson.success()
	}
	
	static void main(String[] args) {
		println "-" * 80
		def all = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
		println "All size is : " + all.size()
		def pages = all.size() % 4 == 0 ? all.size() / 4 : (all.size() / 4).toInteger() + 1
		println "Pages is : $pages"
		for(def page : (0..pages-1)) {
			println "Page $page "			
		}
		def rows = []
		if(all.size() % 4 == 0){
			for(def page : (0..pages-1)) {
				if(page == 0)
					rows << all[0..3]
				else
					rows << all[page*4..page*4+3]
			}
		}else {
			for(def page : (0..pages-1)) {
				if(page == 0)
					rows << all[0..3]
				else
					rows << all[page*4..(page*4+3>=all.size()?all.size()-1:page*4+3)]
			}
		}
		rows.forEach { t -> 
			println t
		}
		println "-" * 80
		String str = "70/0"	
		println str.split("/").size()	
		println (2 / 4) + 1
		println "-" * 80
	}
}