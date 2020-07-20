package com.doosan.biz.ddic.web.system.dao.domain.base;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
/**
 *    基础模型
 * @author 20112004
 * 20191212
 */
@MappedSuperclass
public class BaseModel implements Serializable{

	private static final long serialVersionUID = -8221817919019987695L;
	/*唯一标识*/
	@Id
   	@GeneratedValue(strategy=GenerationType.AUTO)
   	@Column(name="tid")
	private Integer tid;
	/*创建者*/
	@Column(name="createuserid")
	private String createuserid;
	@Column(name="createtime")
	private Date createtime;
	/*最后修改人*/
	@Column(name="modifyuserid")
	private String modifyuserid;
	/*修改时间*/
	@Column(name="modifytime")
	private Date modifytime;
	/*行版本号*/
	@Column(name="rowversion")
	private int rowversion;
	/*删除标记(0 : 正常  1 : 删除  )*/
	@Column(name="delflag")
	private String delflag;
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public String getCreateuserid() {
		return createuserid;
	}
	public void setCreateuserid(String createuserid) {
		this.createuserid = createuserid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getModifyuserid() {
		return modifyuserid;
	}
	public void setModifyuserid(String modifyuserid) {
		this.modifyuserid = modifyuserid;
	}
	public Date getModifytime() {
		return modifytime;
	}
	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}
	public int getRowversion() {
		return rowversion;
	}
	public void setRowversion(int rowversion) {
		this.rowversion = rowversion;
	}
	public String getDelflag() {
		return delflag;
	}
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}
}