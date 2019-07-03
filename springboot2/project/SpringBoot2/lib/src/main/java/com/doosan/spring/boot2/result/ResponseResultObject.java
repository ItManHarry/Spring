package com.doosan.spring.boot2.result;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * 返回接口数据类
 * @author 20112004
 * @param <T>
 */
public class ResponseResultObject<T> {
	//响应状态码
	private Integer status;
	//响应消息
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String exceptionURL;
	//返回对象
	private T data;
	//时间戳
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
	public ResponseResultObject(){}
	
	
	public ResponseResultObject(ResponseResults results){
		this.status = results.getStatus();
		this.message = results.getMessage();
	}
	
	public ResponseResultObject(Integer status, String message, String exceptionURL) {
		super();
		this.status = status;
		this.message = message;
		this.exceptionURL = exceptionURL;
	}

	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public T getData() {
		return data;
	}


	public void setData(T data) {
		this.data = data;
	}


	public Timestamp getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}


	public String getExceptionURL() {
		return exceptionURL;
	}


	public void setExceptionURL(String exceptionURL) {
		this.exceptionURL = exceptionURL;
	}
}