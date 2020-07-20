package com.doosan.biz.ddic.common.results;
import java.io.Serializable;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonInclude;

public class ServerResultObject<T> implements Serializable{

	private static final long serialVersionUID = 5819164734069539246L;
	//响应状态码
	private Integer status;	
	//响应消息
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String exceptionURL;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer number;
	//返回对象
	private T data;
	//时间戳
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
	public ServerResultObject(){}
	
	
	public ServerResultObject(ServerResults results){
		this.status = results.getStatus();
		this.message = results.getMessage();
	}
	
	public ServerResultObject(Integer status, String message, String exceptionURL) {
		super();
		this.status = status;
		this.message = message;
		this.exceptionURL = exceptionURL;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WebServerResultObject:{"
				+ "status:" + status + ","
				+ "message:" + message + ","
				+ "data:" + data + ","
				+ "exceptionURL:" + exceptionURL + ","
				+ "number:" + number + ","
				+ "timestamp:" + timestamp
				+ "}";
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
	
	public Integer getNumber() {
		return number;
	}


	public void setNumber(Integer number) {
		this.number = number;
	}
}
