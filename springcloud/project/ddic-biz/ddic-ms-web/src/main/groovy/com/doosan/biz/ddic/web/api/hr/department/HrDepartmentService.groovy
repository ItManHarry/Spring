package com.doosan.biz.ddic.web.api.hr.department
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import com.doosan.biz.ddic.web.api.hr.department.fallback.HrDepartmentServiceFallback
@FeignClient(value = "ddic-ms-hr",fallback = HrDepartmentServiceFallback.class)
interface HrDepartmentService {
	@GetMapping("/hr/biz/department/getAll")
	def getAll(@RequestParam("order") String order, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("name") String name)
	@GetMapping("/hr/biz/department/total")
	def total()
	@PostMapping("/hr/biz/department/save")
	def save(@RequestParam("params") String params)
	@GetMapping("/hr/biz/department/getDepartment")
	def getDepartment(@RequestParam("id") String id)
	@PostMapping("/hr/biz/department/modify")
	def modify(@RequestParam("params") String params)
	@GetMapping("/hr/biz/department/move")
	def move(@RequestParam("id") String id)
}