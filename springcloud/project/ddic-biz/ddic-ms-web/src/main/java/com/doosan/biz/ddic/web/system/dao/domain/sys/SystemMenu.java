package com.doosan.biz.ddic.web.system.dao.domain.sys;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.doosan.biz.ddic.web.system.dao.domain.base.BaseModel;
/**
 * 	系统菜单
 */
@Entity
@Table(name="sys_menu")
public class SystemMenu extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 1139822515336030630L;
	//模块所属
	@Column(name="module")
	private int module;
	//菜单名称
	@Column(name="name",length=50)
	private String name;
	//链接地址
	@Column(name="url",length=30)
	private String url;
	//使用状态-前端显示(在用 / 停用)
	@Transient
	private String status;
	@Transient
	private String moduleNm;
	
	public String getModuleNm() {
		return moduleNm;
	}
	public void setModuleNm(String moduleNm) {
		this.moduleNm = moduleNm;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}