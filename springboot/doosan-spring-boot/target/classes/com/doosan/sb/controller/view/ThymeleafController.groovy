package com.doosan.sb.controller.view
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import com.doosan.sb.beans.Users
@Controller
class ThymeleafController {
	@RequestMapping("/thymeleaf/list")
	def toList(HttpServletRequest request, Map map){
		def list = []
		list << new Users("name":"Harry", "age":20)
		list << new Users("name":"Tom", "age":23)
		list << new Users("name":"Jack", "age":22)
		//跳转到list.jsp页面
		map.put("list", list)					//对应前端th:each
		map.put("message", "Hello Thymeleaf")	//对应前端th:text
		map.put("gender", "M")					//对应前端th:if
		map.put("grade", "3")					//对应前端th:switch...th:case...
		//request域数据
		request.setAttribute("requestdata", "Request data.")
		//session域数据
		request.getSession().setAttribute("sessiondata", "Sesssion data.")
		//application域数据
		request.getSession().getServletContext().setAttribute("applicationdata", "Application data.")
		//跳转至list.html文件
		return "view/thymeleaf/list"
	}
}