package com.doosan.sb.controller.exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
@Configuration
class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception e) {
		ModelAndView mv = new ModelAndView()
		if(e instanceof ArithmeticException)
			mv.setViewName("view/thymeleaf/exception/arithmeticError")
		if(e instanceof NullPointerException)
			mv.setViewName("view/thymeleaf/exception/nullPointerError")
		mv.addObject("exception", e.toString())
		return mv
	}
}