package com.doosan.biz.ddic.common.exception;
import com.doosan.biz.ddic.common.results.ServerResults;

/**
 * 自定义一个Exception
 * 	扩展RuntimeException, 增加一个status属性
 * @author 20112004
 * 注意：
 * 	如果继承RuntimeException,Spring框架是支持事务回滚的
 * 	如果继承Exception,Spring框架是不支持事务回滚的
 *
 */
public class ServerException extends RuntimeException {

	private static final long serialVersionUID = 6258084126128409605L;
	private Integer status;
	
	public ServerException(){
		
	}
	
	public ServerException(Integer status, String message){
		super(message);
		this.status = status;
	}
	
	public ServerException(ServerResults result){
		super(result.getMessage());
		this.status = result.getStatus();
	}
	
	public ServerException(ServerResults result, Exception e){
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