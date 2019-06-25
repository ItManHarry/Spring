package com.doosan.spring.boot2.dao.entity;
import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 6266407321527104244L;
	private Integer id;
	private String name;
	private Integer age;
	private String remark;
	
	public User(){}

	public User(Integer id, String name, Integer age, String remark) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.remark = remark;
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
}