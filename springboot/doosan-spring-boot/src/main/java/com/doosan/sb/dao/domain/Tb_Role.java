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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
/**
 * 角色表实体
 */
@Entity
@Table(name="tb_role")
public class Tb_Role implements Serializable {
	private static final long serialVersionUID = -6354767586648228560L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tid")
	private Integer tid;
	@Column(name="name")
	private String name;
	//多对多,一个角色对应多个用户
	@ManyToMany(mappedBy="roles",fetch=FetchType.EAGER)
	private Set<Tb_User> users = new HashSet<Tb_User>();
	
	public Set<Tb_User> getUsers() {
		return users;
	}
	public void setUsers(Set<Tb_User> users) {
		this.users = users;
	}
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
}