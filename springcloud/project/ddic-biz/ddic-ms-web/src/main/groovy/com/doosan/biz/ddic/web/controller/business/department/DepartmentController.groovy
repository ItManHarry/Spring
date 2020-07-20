package com.doosan.biz.ddic.web.controller.business.department
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
import com.doosan.biz.ddic.web.api.hr.department.HrDepartmentService
@Controller
@RequestMapping("/web/biz/department")
class DepartmentController {

	@Autowired
	HrCompanyService hrCompanyService
	@Autowired
	HrDepartmentService hrDepartmentService
	final String URL = "biz/hr/department"
	/**
	 * 跳转至清单画面
	 * @return
	 */
	@GetMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		def total = hrDepartmentService.total()
		println "Total is : $total"
		map.put("total", total)
		def companyTotal = hrCompanyService.total()
		println "companyTotal is : $companyTotal"
		map.put("companyTotal", companyTotal)
		return URL + "/list"
	}
	/**
	 * 跳转至新增画面
	 * @return
	 */
	@GetMapping("/add")
	def add(Map map){
		def companyTotal = hrCompanyService.total()
		println "companyTotal is : $companyTotal"
		map.put("companyTotal", companyTotal)
		return URL + "/add"
	}
	/**
	 * 执行保存
	 * @param department
	 * @return
	 */
	@PostMapping("/save")
	def save(HttpServletRequest request, Map map){
		def params = [:]
		params.put("companyid",request.getParameter("companyid"))
		params.put("name",request.getParameter("name"))
		params.put("code",request.getParameter("code"))
		params.put("parentid",Integer.parseInt(request.getParameter("parentid")))
		params.put("user", request.getSession().getAttribute("currentUser").toString())
		hrDepartmentService.save(JsonUtils.objectToJson(params))
		return list(map)
	}
	/**
	 * 跳转至修改画面
	 * @param id
	 * @param map
	 * @return
	 */
	@GetMapping("/update")
	def update(String id, Map map){
		def department = hrDepartmentService.getDepartment(id)
		map.put("department", department)
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
	def modify(HttpServletRequest request, Map map){
		def params = [:]
		params.put("id",request.getParameter("id"))
		params.put("companyid",request.getParameter("companyid"))
		params.put("name",request.getParameter("name"))
		params.put("code",request.getParameter("code"))
		params.put("parentid",Integer.parseInt(request.getParameter("parentid")))
		params.put("user", request.getSession().getAttribute("currentUser").toString())
		hrDepartmentService.modify(JsonUtils.objectToJson(params))
		return list(map)
	}
	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	@GetMapping("/move")
	def move(String id, Map map){
		hrDepartmentService.move(id)
		return list(map)
	}
	/**
	 * 执行数据获取-全部数据分页
	 * @param department
	 * @param order
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(String order, Integer page, Integer limit, String name){
		def result = hrDepartmentService.getAll(order, page, limit, name)
		return result
	}	
}