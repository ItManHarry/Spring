package com.doosan.sb.dao.domain;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * 部门表实体
 */
@Entity
@Table(name="tb_department")
public class Tb_Department implements Serializable{
	private static final long serialVersionUID = -7604716969887829695L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tid")
	private Integer tid;
	@Column(name="name")
	private String name;
	//一方关联多发,一个部门有多个雇员
	//如果不配置fetch属性时,fetch属性值默认为lazy,执行查询部门获取雇员信息会报异常:
	/**
	 * org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: 
	 * com.doosan.sb.dao.domain.Tb_Department.employees, could not initialize proxy - no Session
	 * ...
	 */
	@OneToMany(mappedBy="department",fetch=FetchType.EAGER)
	private Set<Tb_Employee> employees = new HashSet<Tb_Employee>();
	
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
	public Set<Tb_Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(Set<Tb_Employee> employees) {
		this.employees = employees;
	}	
}