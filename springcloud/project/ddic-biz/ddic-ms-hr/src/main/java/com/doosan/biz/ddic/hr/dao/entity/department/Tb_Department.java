package com.doosan.biz.ddic.hr.dao.entity.department;
import com.doosan.biz.ddic.hr.dao.entity.base.BaseEntity;
/**
 * 部门信息表
 * @author Harry Cheng
 *
 */
public class Tb_Department extends BaseEntity {

	private static final long serialVersionUID = -1912323687425829768L;
	private Integer companyid;	//所属公司
	private String name;		//部门名称
	private String code;		//部门代码
	private Integer parentid;	//上级部门
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public Integer getCompanyid() {
		return companyid;
	}
	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}
}