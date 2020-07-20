package com.doosan.biz.ddic.web.api.hr.company
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import com.doosan.biz.ddic.web.api.hr.company.fallback.HrCompanyServiceFallback
@FeignClient(value = "ddic-ms-hr",fallback = HrCompanyServiceFallback.class)
interface HrCompanyService {
	@PostMapping("/hr/biz/company/save")
	def save(@RequestParam("params") String params)
	@GetMapping("/hr/biz/company/total")
	def total()
	@GetMapping("/hr/biz/company/getAll")
	def getAll(@RequestParam("order") String order, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit)
	@GetMapping("/hr/biz/company/getcompany")
	def getCompany(@RequestParam("id") String id)
	@PostMapping("/hr/biz/company/modify")
	def modify(@RequestParam("params") String params)
	@GetMapping("/hr/biz/company/move")
	def move(@RequestParam("id") String id)
}