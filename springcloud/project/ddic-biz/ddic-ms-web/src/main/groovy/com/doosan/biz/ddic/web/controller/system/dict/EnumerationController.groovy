package com.doosan.biz.ddic.web.controller.system.dict
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemEnum
import com.doosan.biz.ddic.web.system.dao.service.SystemEnumService
/**
 * 字典枚举
 * @author 20112004
 *
 */
@Controller
@RequestMapping("/web/system/enums")
class EnumerationController {
	@Autowired
	SystemEnumService systemEnumService;
	/**
	 * 根据字典ID获取所有的枚举
	 * @param dictId
	 * @return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(int dictId){
		def data = systemEnumService.findByDictId(dictId)
		def result = ServerResultJson.success(data)
		return result
	}
	/**
	 * 新增修改枚举数据
	 * @param request
	 * @param map
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		println "add / update Enum : " + request.getParameter("dicId")
		def userId = request.getSession().getAttribute("currentUser")
		SystemEnum em = new SystemEnum()
		int id = Integer.parseInt(request.getParameter("id"))
		if(id != 0) {
			em = systemEnumService.getEnumById(id);
			em.setModifytime(new Date())
			em.setModifyuserid(userId)
			em.setRowversion(em.getRowversion()+1)
		}else {
			em.setCreatetime(new Date())
			em.setCreateuserid(userId)
			em.setDelflag("0")
		}
		em.setDictId(Integer.parseInt(request.getParameter("dictId")))
		em.setValue(request.getParameter("value"))
		em.setView(request.getParameter("view"))
		systemEnumService.save(em)
		return ServerResultJson.success()
	}
}