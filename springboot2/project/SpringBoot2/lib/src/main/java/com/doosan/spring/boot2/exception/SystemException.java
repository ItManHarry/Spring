package com.doosan.spring.boot2.exception;
import com.doosan.spring.boot2.result.ResponseResults;
/**
 * 自定义一个Exception
 * 	扩展RuntimeException, 增加一个status属性
 * @author 20112004
 * 注意：
 * 	如果继承RuntimeException,Spring框架是支持事务回滚的
 * 	如果继承Exception,Spring框架是不支持事务回滚的
 *
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 6258084126128409605L;
	private Integer status;
	
	public SystemException(){
		
	}
	
	public SystemException(Integer status, String message){
		super(message);
		this.status = status;
	}
	
	public SystemException(ResponseResults result){
		super(result.getMessage());
		this.status = result.getStatus();
	}
	
	public SystemException(ResponseResults result, Exception e){
		super((e.getMessage() != null) ? (result.getMessage() + ":" + e.getMessage()) : result.getMessage());
		this.status = result.getStatus();
	}
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}