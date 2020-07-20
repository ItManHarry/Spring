package com.doosan.biz.ddic.hr.controller.department
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.hr.dao.entity.department.Tb_Department
import com.doosan.biz.ddic.hr.service.department.DepartmentService
import com.github.pagehelper.Page
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@RestController
@RequestMapping("/hr/biz/department")
class DepartmentController {

	@Autowired
	DepartmentService departmentService	
	/**
	 * 执行保存
	 * @param params
	 * @return
	 */
	@PostMapping("/save")
	def save(String params){
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		Tb_Department department = new Tb_Department()
		department.setCompanyid(data.get("companyid").asInt)
		department.setName(data.get("name").asString)
		department.setCode(data.get("code").asString)
		department.setParentid(data.get("parentid").asInt)
		department.setCreateuserid(data.get("user").asString)
		department.setNewRecord(true)
		departmentService.save(department)
		return ServerResultJson.success("Added successfully!")
	}
	/**
	 * 根据ID获取部门信息
	 * @param id
	 * @return
	 */
	@GetMapping("/getDepartment")
	def getDepartment(String id){
		Tb_Department department = departmentService.findById(id)
		return ServerResultJson.success(department)
	}
	/**
	 * 执行更新
	 * @param params
	 * @return
	 */
	@PostMapping("/modify")
	def modify(String params){
		println "Modify params : $params"
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		Tb_Department department = departmentService.findById(data.get("id").asString)
		department.setCompanyid(data.get("companyid").asInt)
		department.setName(data.get("name").asString)
		department.setCode(data.get("code").asString)
		department.setParentid(data.get("parentid").asInt)
		department.setModifyuserid(data.get("user").asString)
		department.setNewRecord(false)
		departmentService.save(department)
		return ServerResultJson.success("Updated successfully!")
	}
	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	@GetMapping("/move")
	def move(String id){
		def ids = id.split(",")
		Integer i = departmentService.delete(ids)
		return ServerResultJson.success("Deleted successfully!")
	}
	/**
	 * 执行数据获取-全部数据分页
	 * @param department
	 * @param order
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping("/getAll")
	def getAll(String order, Integer page, Integer limit, String name){
		def countByTerm = departmentService.recordCntByTerm(name);
		Page<Tb_Department> allByTerm = departmentService.findAllByTerm(order, page, limit, name)
		return ServerResultJson.success(allByTerm, countByTerm)
	}
	@GetMapping("/total")
	def total(){
		def total = departmentService.recordCnt(new Tb_Department());
		return total
	}
}