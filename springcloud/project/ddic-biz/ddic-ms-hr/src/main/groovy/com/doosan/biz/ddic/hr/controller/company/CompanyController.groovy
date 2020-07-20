package com.doosan.biz.ddic.hr.controller.company
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
import com.doosan.biz.ddic.hr.dao.entity.company.Tb_Company
import com.doosan.biz.ddic.hr.service.company.CompanyService
import com.github.pagehelper.Page
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@RestController
@RequestMapping("/hr/biz/company")
class CompanyController {

	@Autowired
	CompanyService companyService	
	/**
	 * 执行保存
	 * @param company
	 * @return
	 */
	@PostMapping("/save")
	def save(String params){
		Tb_Company company = new Tb_Company()
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		company.setName(data.get("name").asString)
		company.setCode(data.get("code").asString)
		company.setCreateuserid(data.get("user").asString)
		company.setNewRecord(true)
		companyService.save(company)
		return ServerResultJson.success("Added successfully!")
	}
	/**
	 * 根据ID获取部门信息
	 * @param id
	 * @return
	 */
	@GetMapping("/getcompany")
	def getcompany(String id){
		Tb_Company company = companyService.findById(id)
		return ServerResultJson.success(company)
	}
	/**
	 * 执行更新
	 * @param id
	 * @param name
	 * @param job
	 * @return
	 */
	@PostMapping("/modify")
	def modify(String params){
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		Tb_Company company = companyService.findById(data.get("id").asString)
		company.setId(data.get("id").asString)
		company.setName(data.get("name").asString)
		company.setCode(data.get("code").asString)
		company.setModifyuserid(data.get("user").asString)
		company.setNewRecord(false)
		companyService.save(company)
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
		Integer i = companyService.delete(ids)
		return ServerResultJson.success("Deleted successfully!")
	}
	/**
	 * 执行数据获取-全部数据分页
	 * @param company
	 * @param order
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping("/getAll")
	def getAll(String order, Integer page, Integer limit){
		def count = companyService.recordCnt(new Tb_Company())
		Page<Tb_Company> all = companyService.findAll(new Tb_Company(), order, page, limit)
		return ServerResultJson.success(all, count)
	}
	@GetMapping("/total")
	def total(){
		def total = companyService.recordCnt(new Tb_Company());
		return total
	}
}