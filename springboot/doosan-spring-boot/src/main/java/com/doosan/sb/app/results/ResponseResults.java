package com.doosan.sb.app.results;
/**
 * 集中管理返回的枚举类
 * @author 20112004
 *
 */
public enum ResponseResults {
	
	SUCCESS(200, "执行成功"),
	NOTFOUND(0, "无数据"),
	NOTACCESS(403, "拒绝请求"),
	ERROR(500, "内部错误"),
	ERROR_TOKEN(502, "用户Token错误"),
	ERROR_TIMEOUT(503, "连接超时"),
	ERROR_UNKNOWN(555, "未知错误");
	
	private int status;
	private String message;
	
	ResponseResults(int status, String message){
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}