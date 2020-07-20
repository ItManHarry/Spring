package com.doosan.biz.ddic.web.system.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SpringContextUtils {

	/**
	 	* 上下文对象实例
    */
    private static ApplicationContext applicationContext;
    
	private static Logger logger = LoggerFactory.getLogger(SpringContextUtils.class);
	
	private static void assertApplicationContext() {
		if(applicationContext == null)
			throw new IllegalStateException("application未定义,请在application.xml中定义SpringContextHolder");
	}
	
	@Autowired
    public static void setApplicationContext(ApplicationContext context)  {
		logger.info("ApplicationContext has been set");
        applicationContext = context;
    }
 
    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
    	assertApplicationContext();
        return applicationContext;
    }
	
	 /**
	  	* 通过name获取 Bean.
	  	* @param name
	  	* @return
	 */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }
 
    /**
     	* 通过class获取Bean.
     	* @param clazz
     	* @param <T>
     	* @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }
 
    /**
     	* 通过name,以及Clazz返回指定的Bean
	     * @param name
	     * @param clazz
	     * @param <T>
	     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
	
}