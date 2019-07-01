package com.doosan.spring.boot2.dao.entity;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class User implements Serializable{

	private static final long serialVersionUID = 6266407321527104244L;
	private Integer id;
	private String name;
	private Integer age;
	@JsonInclude(JsonInclude.Include.NON_NULL)	//数据为空的话就不返回前台
	private String remark;
	@JsonIgnore 								//数据不会返回到前端
	private String password;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",locale="zh",timezone="GMT+8") 	//数据格式化
	private Date createDate;
	
	public User(){}

	public User(Integer id, String name, Integer age, String remark, String password, Date createDate) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.remark = remark;
		this.password = password;
		this.createDate = createDate;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}