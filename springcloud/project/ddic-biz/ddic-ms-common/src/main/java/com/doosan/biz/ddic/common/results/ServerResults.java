package com.doosan.biz.ddic.common.results;

public enum ServerResults {
	
	SUCCESS(200, "执行成功"),
	NOTFOUND(0, "无数据"),
	NOTACCESS(403, "拒绝请求"),
	ERROR(500, "内部错误"),
	ERROR_KEY(600, "数据库主键冲突"),
	ERROR_TOKEN(502, "用户Token错误"),
	ERROR_TIMEOUT(503, "连接超时"),
	ERROR_UNKNOWN(555, "未知错误"),
	ERROR_USERNOTFOUND(700, "用户不存在"),
	ERROR_WRONGPWD(701, "密码错误");
	
	private int status;
	private String message;
	
	ServerResults(int status, String message){
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