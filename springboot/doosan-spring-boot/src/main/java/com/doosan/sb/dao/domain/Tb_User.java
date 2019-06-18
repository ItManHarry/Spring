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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
@Entity
@Table(name="tb_user")
public class Tb_User implements Serializable {
	private static final long serialVersionUID = -6455955497901736224L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tid")
	private Integer tid;
	@Column(name="name")
	private String name;
	@Column(name="password")
	private String password;
	//多对多,一个用户对应多个角色
	//@ManyToMany(cascade=CascadeType.PERSIST)
	@ManyToMany(fetch=FetchType.EAGER)
	//多对多的实现方式就是中间表方式实现
	@JoinTable(name="t_user_role",joinColumns=@JoinColumn(name="user_id"),inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Tb_Role> roles = new HashSet<Tb_Role>();
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Tb_Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Tb_Role> roles) {
		this.roles = roles;
	}
}
