package com.doosan.sb.controller.upload
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
@RestController
@RequestMapping("/file")
class UploadController {
	@RequestMapping("/doUpload")
	//@RequestParam中的参数对应前端的file的name属性,也可不使用此注解，直接将form中的input的name属性改为"file"
	def upload(@RequestParam("attach")MultipartFile file){
		println "File Name : " + file.getOriginalFilename()
		println "File type : " + file.getContentType()
		//上传路径
		file.transferTo(new File("c:/" + file.getOriginalFilename()))
		result.put("result", "success")
		return result
	}	
	
	def result = [:]
}