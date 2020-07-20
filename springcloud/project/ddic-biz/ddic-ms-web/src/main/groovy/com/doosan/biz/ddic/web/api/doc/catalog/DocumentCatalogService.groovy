package com.doosan.biz.ddic.web.api.doc.catalog
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import com.doosan.biz.ddic.web.api.doc.catalog.fallback.DocumentCatalogServiceFallback
@FeignClient(value = "ddic-ms-doc",fallback = DocumentCatalogServiceFallback.class)
interface DocumentCatalogService {
	@PostMapping("/doc/biz/catalog/save")
	def save(@RequestParam("params") String params)
	@GetMapping("/doc/biz/catalog/total")
	int total()
	@GetMapping("/doc/biz/catalog/all")
	def all(@RequestParam("page")Integer page, @RequestParam("limit")Integer limit)
	@GetMapping("/doc/biz/catalog/authedCs")
	def authedCs(@RequestParam("page")Integer page, @RequestParam("limit")Integer limit, @RequestParam("user")String user)
	@PostMapping("/doc/biz/catalog/status")
	def status(@RequestParam("params") String params)
	@GetMapping("/doc/biz/catalog/tree")
	def tree()
	@GetMapping("/doc/biz/catalog/authedTree")
	def authedTree(@RequestParam("user") String user)
	@PostMapping("/doc/biz/catalog/auth")
	def auth(@RequestParam("params") String params)
	@PostMapping("/doc/biz/catalog/undoAuth")
	def undoAuth(@RequestParam("params") String params)
	@GetMapping("/doc/biz/catalog/authed")
	def authed(@RequestParam("userId")String userId)
	
}
