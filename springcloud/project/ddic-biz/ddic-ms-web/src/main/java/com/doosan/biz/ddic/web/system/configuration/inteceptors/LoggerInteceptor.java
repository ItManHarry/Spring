package com.doosan.biz.ddic.web.system.configuration.inteceptors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * 系统日志拦截器
 */
public class LoggerInteceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		System.out.println("Write log to the system..." + request.getRequestURI());
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mav)
			throws Exception {
		// TODO Auto-generated method stub
		if(mav != null){
			System.out.println("ViewName : " + mav.getViewName() + "------------------" + request.getRequestURI());
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e)
			throws Exception {
		System.out.println("Finished the request ......" + request.getRequestURI());				
	}
}