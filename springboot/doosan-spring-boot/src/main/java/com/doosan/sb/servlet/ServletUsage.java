package com.doosan.sb.servlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * SpringBoot使用Servlet
 */
@WebServlet(name="userservlet",urlPatterns="/servlet/HelloServlet")	//声明该类为Servlet程序
/**
 * 等同于web.xml配置
 *<servlet>
 * 	<servlet-name>userservlet</servlet-name>
 *	<servlet-class>com.doosan.sb.servlet.ServletUsage</servlet-class>
 *</servlet>
 *<servlet-mapping>
 *	<servlet-name>userservlet</servlet-name>
 *	<url-pattern>/servlet/HelloServlet</url-pattern>
 *</servlet-mapping>
 */
public class ServletUsage extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("SpringBoot use the servlet successfully...");
	}
	
	private static final long serialVersionUID = 2083902449743154468L;
}