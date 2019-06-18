package com.doosan.sb;
import javax.servlet.ServletRegistration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import com.doosan.sb.filter.FilterUsage;
import com.doosan.sb.listener.ListenerUsage;
import com.doosan.sb.servlet.ServletUsage;
/**
 * web工程启动器
 */
@SpringBootApplication
//@ServletComponentScan	//SpringBoot扫描@WebServlet注解对应的类
@MapperScan("com.doosan.sb.dao")	//MyBtis扫描
@EnableCaching						//启用缓存
public class ApplicationStarter {
	
	public static void main(String[] args){
		SpringApplication.run(ApplicationStarter.class, args);
	}
	/**
	 * 注册Servlet	
	 * @return
	 */
	@Bean
	public ServletRegistrationBean getServletRegistrationBean(){
		ServletRegistrationBean bean = new ServletRegistrationBean(new ServletUsage());
		bean.addUrlMappings("/servlet/HelloServlet");
		return bean;
	}
	/**
	 * 注册Filter	
	 * @return
	 */
	@Bean
	public FilterRegistrationBean getFilterRegistrationBean(){
		FilterRegistrationBean bean = new FilterRegistrationBean(new FilterUsage());
		bean.addUrlPatterns("/servlet/HelloServlet");
		return bean;
	}
	/**
	 * 注册Listener	
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<ListenerUsage> getServletListenerRegistrationBean(){
		ServletListenerRegistrationBean<ListenerUsage> bean = new ServletListenerRegistrationBean<ListenerUsage>(new ListenerUsage());
		return bean;
	}
}