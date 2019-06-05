package com.doosan.sb.config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
/**
 * 配置全局异常页面跳转
 */
//@Configuration
class DoosanSimpleMappingExceptionResolver {
	
//	@Bean
//	SimpleMappingExceptionResolver getSimpleMappingExceptionResolver(){
//		SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver()
//		Properties mappings = new Properties()
//		/**
//		 * key:异常类型全名
//		 * value:异常跳转页面地址
//		 */
//		mappings.put("java.lang.ArithmeticException", "view/thymeleaf/exception/arithmeticError")
//		mappings.put("java.lang.NullPointerException", "view/thymeleaf/exception/nullPointerError")
//		resolver.setExceptionMappings(mappings)
//		return resolver
//	}	
}