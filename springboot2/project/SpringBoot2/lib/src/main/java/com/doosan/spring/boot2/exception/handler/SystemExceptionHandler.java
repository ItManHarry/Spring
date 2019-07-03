package com.doosan.spring.boot2.exception.handler;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.server.ServerRequest;
import com.doosan.spring.boot2.exception.SystemException;
import com.doosan.spring.boot2.result.ResponseResultJson;
/**
 * Exception异常处理
 * 兼容说明：
 * 	支持HTTP WEB异常页面跳转
 * 	支持AJAX JSON异常数据返回
 * @author 20112004
 *
 */
@ControllerAdvice
public class SystemExceptionHandler {
	private final static Logger logger = LoggerFactory.getLogger(SystemExceptionHandler.class);
	public static final String ERROR_PAGE = "system/common/error";
	@SuppressWarnings("rawtypes")
	//@ExceptionHandler(value=SystemException.class)
	@ResponseBody
	public Object handle(ServerRequest request, Exception e){
		Map map = new HashMap();
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		//获取异常页面URL地址
		String exceptionURL = null;
		Object o = httpRequest.getRequestURI();
		if(o != null)
			exceptionURL = httpRequest.getRequestURL().toString();
		//异常处理
		if(e instanceof SystemException){
			logger.error("000 自定义异常：{}", e);
			SystemException se = (SystemException)e;
			if(!isAjax(httpRequest))
				return resultView(se.getStatus(), se.getMessage(), exceptionURL, map);
			else
				return ResponseResultJson.error(se.getStatus(), se.getMessage(), exceptionURL);
		}else{
			logger.error("500 系统异常", e);
			if(!isAjax(httpRequest))
				return resultView(500, e.toString(), exceptionURL, map);
			else
				return ResponseResultJson.error(500, e.toString(), exceptionURL);
		}
	}
	/**
	 * 判断请求是否是异步请求
	 * @return
	 */
	private static boolean isAjax(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");
		System.out.println("Header is : " + header);
		boolean ajax = "".equals(header) ? true : false;
		System.out.println("Is ajax request : " + ajax);
		return ajax;
	}
	/**
	 * 错误页面跳转
	 * @param status
	 * @param message
	 * @param exceptionURL
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String resultView(Integer status, String message, String exceptionURL, Map map){
		map.put("status",status);
		map.put("message", message);
		map.put("exceptionURL", exceptionURL);
		return ERROR_PAGE;
	}
}