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
@RequestMapping("/web/biz/document/work")
class DocumentController {
	
	final String WEB_URL = "biz/document/work"
	@Autowired
	DocumentCatalogService documentCatalogService
	@Autowired
	SystemUserService systemUserService
	
	/**
	 * 	跳转文档工作区
	 * 	@return
	 */
	@RequestMapping("/portal")
	def portal(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		def total = documentCatalogService.total()
		map.put("total", total)
		return WEB_URL + "/portal"
	}
	
	/**
	 * 	跳转文档上传
	 * 	@return
	 */
	@RequestMapping("/uploads")
	def uploads(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		def total = documentCatalogService.total()
		map.put("total", total)
		return WEB_URL + "/uploads"
	}
	
	/**
	 * 	跳转文档目录管理 Catalog List
	 * 	@return
	 */
	@RequestMapping("/clist")
	def clist(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)		
		return WEB_URL + "/clist"
	}
	/**
	 * 获取用户授权目录清单
	 * @param page
	 * @param limit
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(Integer page, Integer limit, HttpServletRequest request){
		String user = request.getSession().getAttribute("currentUser").toString()
		return documentCatalogService.authedCs(page, limit, user)
	}
	/**
	 * 获取用户授权目录清单
	 * @param page
	 * @param limit
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/authedTree")
	def authedTree(HttpServletRequest request){
		String user = request.getSession().getAttribute("currentUser").toString()
		return documentCatalogService.authedTree(user)
	}
}