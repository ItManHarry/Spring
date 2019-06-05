package com.doosan.sb.controller.exception
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView
@ControllerAdvice
class GlobalExceptionHandler {
	
//	@ExceptionHandler(value=[java.lang.ArithmeticException])
//	ModelAndView handlerArithmeticException(Exception e){
//		ModelAndView mv = new ModelAndView()
//		mv.addObject("exception", e.toString())
//		mv.setViewName("view/thymeleaf/exception/arithmeticError")
//		return mv
//	}
//	@ExceptionHandler(value=[java.lang.NullPointerException])
//	ModelAndView handlerNullPointerException(Exception e){
//		ModelAndView mv = new ModelAndView()
//		mv.addObject("exception", e.toString())
//		mv.setViewName("view/thymeleaf/exception/nullPointerError")
//		return mv
//	}
}