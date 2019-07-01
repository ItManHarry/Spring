package com.doosan.spring.boot2.controller.user
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.doosan.spring.boot2.dao.entity.Customer
import com.doosan.spring.boot2.result.ResponseResultJson
import com.doosan.spring.boot2.result.ResponseResultObject
@RestController
class CustomerController {

	@Autowired
	private Customer customer
	@Value("\${system.file.image.path}")
	private String imagePath;
	/**
	 * 统一返回接口数据
	 * @param id
	 * @return
	 */
	@GetMapping("/customer/get")
	ResponseResultObject<Customer> getCustomer(){
		Customer vo = new Customer()
		BeanUtils.copyProperties(customer, vo)
		println "Image path is : $imagePath"
		return ResponseResultJson.success(vo)
	}
}