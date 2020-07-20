package com.doosan.biz.ddic.web.system.dao.domain.sys;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.doosan.biz.ddic.web.system.dao.domain.base.BaseModel;
/**
 *	 系统下拉列表字典
 */
@Entity
@Table(name="sys_dict")
public class SystemDict extends BaseModel implements Serializable {

	private static final long serialVersionUID = -3480811753283153391L;
	@Column(name="code")
	private String code;	//下拉列表代码	
	@Column(name="name")				
	private String name;	//下拉列表名称
	@Transient				
	private String remark;	//此注解对应的属性不会生成列
	@Transient				
	private String status;	//字典状态
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
}