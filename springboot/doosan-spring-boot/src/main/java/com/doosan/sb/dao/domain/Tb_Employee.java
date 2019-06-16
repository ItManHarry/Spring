package com.doosan.sb.dao.domain;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 雇员表实体
 */
@Entity
@Table(name="tb_employee")
public class Tb_Employee {	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tid")
	private Integer tid;
	@Column(name="name")
	private String name;
	@Column(name="age")
	private Integer age;
	@Column(name="address")
	private String address;
	@Column(name="gender")
	private String gender;
	@Column(name="telphone")	
	private String telphone;
	//多发关联一方,一个雇员属于一个部门
	//@ManyToOne(cascade=CascadeType.PERSIST)
	@ManyToOne
	//维护外键
	@JoinColumn(name="department")
	private Tb_Department department;
	
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public Tb_Department getDepartment() {
		return department;
	}
	public void setDepartment(Tb_Department department) {
		this.department = department;
	}
}