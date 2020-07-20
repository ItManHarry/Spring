package com.doosan.biz.ddic.web.system.dao.domain.sys;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.doosan.biz.ddic.web.system.dao.domain.base.BaseModel;
/**
 * 	系统权限
 */
@Entity
@Table(name="sys_authed")
public class SystemAuthed extends BaseModel {

	private static final long serialVersionUID = 4338965252162180036L;
	//系统用户code
	@Column(name="user",length=40)
	private String user;
	//模块所属
	@Column(name="module")
	private int module;
	//模块下菜单
	@Column(name="menu")
	private int menu;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	public int getMenu() {
		return menu;
	}
	public void setMenu(int menu) {
		this.menu = menu;
	}	
}