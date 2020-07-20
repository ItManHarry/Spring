package com.doosan.biz.ddic.web.controller.business.document
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.common.utils.JsonUtils
import com.doosan.biz.ddic.web.api.doc.catalog.DocumentCatalogService
import com.doosan.biz.ddic.web.system.dao.service.SystemUserService
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
@Controller
@RequestMapping("/web/biz/document/catalog")
class CatalogController {
	
	final String WEB_URL = "biz/document/catalog"
	@Autowired
	DocumentCatalogService documentCatalogService
	@Autowired
	SystemUserService systemUserService
	
	/**
	 * 	跳转目录清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		def total = documentCatalogService.total()
		map.put("total", total)
		return WEB_URL + "/list"
	}
	/**
	 * 	启停目录
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/status")
	def status(HttpServletRequest request, Map map) {
		def params = [:]
		params.put("id", request.getParameter("id"))
		params.put("status", request.getParameter("sts"))
		params.put("user", request.getSession().getAttribute("currentUser").toString())
		documentCatalogService.status(JsonUtils.objectToJson(params))
		return list(map)
	}
	/**
	 * 	执行保存
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request){
		def params = [:]
		params.put("id", request.getParameter("id"))
		params.put("name", request.getParameter("name"))
		params.put("parent", request.getParameter("parent"))
		params.put("leaf", request.getParameter("leaf"))
		params.put("status", request.getParameter("status"))
		params.put("user", request.getSession().getAttribute("currentUser").toString())
		def result = documentCatalogService.save(JsonUtils.objectToJson(params))
		return result
	}
	/**
	 * 	分页获取数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(Integer page, Integer limit){
		return documentCatalogService.all(page, limit)
	}
	/**
	 * 	获取目录树
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/tree")
	def tree(){
		return documentCatalogService.tree()
	}
	/**
	 * 	跳转至目录授权页面
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/auth")
	def auth(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		//用户总数
		def total = systemUserService.getCount().intValue()
		map.put("total", total)
		return WEB_URL + "/auth"
	}
	/**
	 * 	目录授权
	 * 	@return
	 */
	@PostMapping("/doAuth")
	@ResponseBody
	def doAuth(HttpServletRequest request) {
		def params = [:]
		params.put("userId", request.getParameter("userId"))
		params.put("catalogs", request.getParameter("catalogs"))
		params.put("user", request.getSession().getAttribute("currentUser").toString())
		def result = documentCatalogService.auth(JsonUtils.objectToJson(params))
		return result
	}
	/**
	 * 	目录撤权
	 * 	@return
	 */
	@PostMapping("/undoAuth")
	@ResponseBody
	def undoAuth(HttpServletRequest request) {
		def params = [:]
		params.put("userId", request.getParameter("userId"))
		def result = documentCatalogService.undoAuth(JsonUtils.objectToJson(params))
		return result
	}
	/**
	 * 	获取已授权目录
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/authed")
	def authed(String userId){
		return documentCatalogService.authed(userId)
	}
}