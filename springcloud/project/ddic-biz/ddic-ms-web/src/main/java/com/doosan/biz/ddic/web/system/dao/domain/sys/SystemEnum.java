package com.doosan.biz.ddic.web.system.dao.domain.sys;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.doosan.biz.ddic.web.system.dao.domain.base.BaseModel;
/**
 * 	系统下拉列表字典枚举值
 */
@Entity
@Table(name="sys_enum")
public class SystemEnum extends BaseModel implements Serializable {

	private static final long serialVersionUID = -3873696531438457612L;
	@Column(name="dictId")
	private int dictId;		//字典ID - 表:sys_dict
	@Column(name="value")
	private String value;	//下拉列表value:对应option标签的value<option value = "value"></option>
	@Column(name="view")
	private String view;	//下拉列表view:对应<option>view</option>
	@Transient				
	private String remark;	//此注解对应的属性不会生成列
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getDictId() {
		return dictId;
	}
	public void setDictId(int dictId) {
		this.dictId = dictId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
}