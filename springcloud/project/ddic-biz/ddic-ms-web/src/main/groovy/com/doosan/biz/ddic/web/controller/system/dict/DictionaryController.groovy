package com.doosan.biz.ddic.web.controller.system.dict
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemDict
import com.doosan.biz.ddic.web.system.dao.service.SystemDictService
import com.doosan.biz.ddic.web.system.dao.service.SystemEnumService
@Controller
@RequestMapping("/web/system/dict")
class DictionaryController {
	
	final String WEB_URL = "system/dict"
	@Autowired
	SystemDictService systemDictService
	@Autowired
	SystemEnumService systemEnumService;
	
	/**
	 * 	跳转字典清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		def total = systemDictService.getCount()
		if(total)
			map.put("total", total)
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
		def count = systemDictService.getCount() ? systemDictService.getCount().intValue() : 0
		def data = systemDictService.getAllByPages(page, limit)
		data.each { 
			if(it.getDelflag().equals("1"))
				it.setStatus("停用")
			else
				it.setStatus("在用")
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 	根据ID删除字典项
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/move")
	def move(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemDict dict = systemDictService.getDictById(id)
		dict.setDelflag("1")
		dict.setModifytime(new Date())
		dict.setModifyuserid(userId)
		systemDictService.save(dict)
		return list(map)
	}
	/**
	 * 	根据ID恢复字典项
	 * 	@param id
	 * 	@param map
	 * 	@return
	 */
	@RequestMapping("/recover")
	def recover(Integer id, HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemDict dict = systemDictService.getDictById(id)
		dict.setDelflag("0")
		dict.setModifytime(new Date())
		dict.setModifyuserid(userId)
		systemDictService.save(dict)
		return list(map)
	}
	/**
	 * 	保存字典
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemDict dict = new SystemDict()
		int id = Integer.parseInt(request.getParameter("id"))
		if(id != 0) {
			dict = systemDictService.getDictById(id);
			dict.setModifytime(new Date())
			dict.setModifyuserid(userId)
			dict.setRowversion(dict.getRowversion()+1)
		}else {
			def d = systemDictService.getDictByCode(request.getParameter("code").toUpperCase())
			if(d != null)
				return ServerResultJson.error(0, "字典代码已存在,请勿重复添加!", "")
			dict.setCreatetime(new Date())
			dict.setCreateuserid(userId)
			dict.setDelflag("0")
		}
		dict.setCode(request.getParameter("code").toUpperCase())
		dict.setName(request.getParameter("name"))
		systemDictService.save(dict)
		return ServerResultJson.success()
	}
	/**
	 * 	根据字典Code获取下拉列表
	 * 	@param code
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/enums")
	def enums(String code){
		def dict = systemDictService.getDictByCode(code.toUpperCase())
		//delflag为0的可用
		if(dict && dict.getDelflag().equals("0"))
			return ServerResultJson.success(systemEnumService.findByDictId(dict.getTid()))
		else
			return ServerResultJson.success([])
	}
}
