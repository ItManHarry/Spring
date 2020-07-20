package com.doosan.biz.ddic.web.system.dao.domain.sys;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.doosan.biz.ddic.web.system.dao.domain.base.BaseModel;
/**
 * 	系统模块
 */
@Entity
@Table(name="sys_module")
public class SystemModule extends BaseModel implements Serializable {

	private static final long serialVersionUID = -2592599802272969170L;
	@Column(name="moduleIcon",length=50)
	private String moduleIcon;	//模块图标 使用font-awesome4.7版本对应的图标
	@Column(name="moduleNm",length=30)
	private String moduleNm;	//模块名称
	@Column(name="moduleUrl",length=40)
	private String moduleUrl;	//模块对应的URL地址
	@Column(name="moduleRmk",length=120)
	private String moduleRmk;	//模块说明
	@Transient				
	private String status;		//模块状态
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModuleIcon() {
		return moduleIcon;
	}
	public void setModuleIcon(String moduleIcon) {
		this.moduleIcon = moduleIcon;
	}
	public String getModuleNm() {
		return moduleNm;
	}
	public void setModuleNm(String moduleNm) {
		this.moduleNm = moduleNm;
	}
	public String getModuleUrl() {
		return moduleUrl;
	}
	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}
	public String getModuleRmk() {
		return moduleRmk;
	}
	public void setModuleRmk(String moduleRmk) {
		this.moduleRmk = moduleRmk;
	}	
}