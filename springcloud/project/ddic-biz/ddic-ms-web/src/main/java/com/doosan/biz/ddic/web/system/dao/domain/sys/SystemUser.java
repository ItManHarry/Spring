package com.doosan.biz.ddic.web.system.dao.domain.sys;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.doosan.biz.ddic.web.system.dao.domain.base.BaseModel;
/**
 * 	系统用户
 */
@Entity
@Table(name="sys_user")
public class SystemUser extends BaseModel implements Serializable{

	private static final long serialVersionUID = 1533606629464905365L;
	//用户代码-AD账号
	@Column(name="code",length=40)
	private String code;
	//用户姓名
	@Column(name="name",length=50)
	private String name;
	//用户密码
	@Column(name="pwd",length=40)
	private String pwd;
	//使用状态(0 : 停用  1 : 在用)
	@Column(name="status")
	private int status;
	//系统角色
	@Column(name="sysRole")
	private int sysRole;
	//用户类型
	@Column(name="userType")
	private int userType;
	//文档角色
	@Column(name="docRole")
	private int docRole;
	//使用状态-前端显示(在用 / 停用)
	@Transient
	private String stsStr;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStsStr() {
		return stsStr;
	}
	public void setStsStr(String stsStr) {
		this.stsStr = stsStr;
	}
	public int getSysRole() {
		return sysRole;
	}
	public void setSysRole(int sysRole) {
		this.sysRole = sysRole;
	}
	public int getDocRole() {
		return docRole;
	}
	public void setDocRole(int docRole) {
		this.docRole = docRole;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}