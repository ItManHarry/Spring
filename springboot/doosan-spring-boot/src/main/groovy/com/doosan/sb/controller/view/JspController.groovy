package com.doosan.sb.controller.view
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import com.doosan.sb.beans.Users
@Controller
class JspController {
	@RequestMapping("/jsp/list")
	def toList(Map map){
		def list = []
		list << new Users("name":"Harry", "age":20)
		list << new Users("name":"Tom", "age":23)
		list << new Users("name":"Jack", "age":22)
		//跳转到list.jsp页面
		map.put("list", list)
		return "users/list"
	}
}