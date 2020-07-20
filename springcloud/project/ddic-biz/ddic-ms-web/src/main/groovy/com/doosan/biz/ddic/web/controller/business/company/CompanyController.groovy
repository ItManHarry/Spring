package com.doosan.biz.ddic.web.controller.business.company
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.common.utils.JsonUtils
import com.doosan.biz.ddic.web.api.hr.company.HrCompanyService
@Controller
@RequestMapping("/web/biz/company")
class CompanyController {

	@Autowired
	HrCompanyService hrCompanyService
	final String URL = "biz/hr/company"
	/**
	 * 跳转至清单画面
	 * @return
	 */
	@GetMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		def total = hrCompanyService.total()
		println "Total is : $total"
		map.put("total", total)
		return URL + "/list"
	}
	/**
	 * 跳转至新增画面
	 * @return
	 */
	@GetMapping("/add")
	def add(){
		return URL + "/add"
	}
	/**
	 * 执行保存
	 * @param company
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		def params = [:]
		params.put("name", request.getParameter("name"))
		params.put("code", request.getParameter("code"))
		params.put("user", request.getSession().getAttribute("currentUser").toString())
		params.each { k, v -> 
			print("Key is : $k, value is : $v")
		}
		hrCompanyService.save(JsonUtils.objectToJson(params))
		return "SUCCESS"
	}
	/**
	 * 跳转至修改画面
	 * @param id
	 * @param map
	 * @return
	 */
	@GetMapping("/update")
	def update(String id, Map map){
		def company = hrCompanyService.getCompany(id)
		map.put("company", company)
		return URL + "/update"
	}
	/**
	 * 执行更新
	 * @param id
	 * @param name
	 * @param job
	 * @return
	 */
	@PostMapping("/modify")
	@ResponseBody
	def modify(HttpServletRequest request, Map map){
		def params = [:]
		params.put("id",   request.getParameter("id"))
		params.put("name", request.getParameter("name"))
		params.put("code", request.getParameter("code"))
		params.put("user", request.getSession().getAttribute("currentUser").toString())
		hrCompanyService.modify(JsonUtils.objectToJson(params))
		return "SUCCESS"
	}
	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	@GetMapping("/move")
	def move(String id, Map map){
		hrCompanyService.move(id)
		return list(map)
	}
	/**
	 * 执行数据获取-全部数据分页
	 * @param company
	 * @param order
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(String order, Integer page, Integer limit){
		def result = hrCompanyService.getAll(order, page, limit)
		return result
	}	
}