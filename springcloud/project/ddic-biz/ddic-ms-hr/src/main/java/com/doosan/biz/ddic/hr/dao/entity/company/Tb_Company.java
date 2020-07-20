package com.doosan.biz.ddic.hr.dao.entity.company;
import com.doosan.biz.ddic.hr.dao.entity.base.BaseEntity;
/**
 * 公司信息表
 * @author Harry Cheng
 *
 */
public class Tb_Company extends BaseEntity {

	private static final long serialVersionUID = -1912323687425829768L;
	private String name;		//公司名称
	private String code;		//公司代码
	
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
}