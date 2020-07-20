package com.doosan.biz.ddic.hr.dao.entity.base;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * Mybatis基类
 * @author 20112004
 *
 */
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -7279174283256064832L;
	/**
	 * 删除标记 (0 : 正常  1 : 删除  2 : 审核)
	 */
	public static final String DEL_FLAG_NOMAL 	= "0";
	public static final String DEL_FLAG_DELETE 	= "1";
	public static final String DEL_FLAG_AUDIT 	= "2";
	/**
	 * 是否是新纪录(默认false),调用setIsNewRecord()设置新纪录,使用自定义ID	 
	 * 设置为true后强制执行插入语句,ID不会自动生成,需手动导入
	*/
	@JsonIgnore
	protected boolean isNewRecord = false;
	
	public boolean getIsNewRecord() {
		return isNewRecord;
	}
	public void setNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}
	
	public BaseEntity(){
		super();
		this.delflag = DEL_FLAG_NOMAL;
	}
	
	public BaseEntity(String id){
		this();
		this.id = id;
	}
	/**
	 * 插入前执行的方法,需要手动调用
	 */
	public void prepareInsert(){
		this.id = null;
		this.createtime = new Date();
		this.rowver = 1;
	}
	/**
	 * 更新前执行的方法,需要手动调用
	 */
	public void prepareUpdate(){
		this.modifytime = new Date();
		this.rowver ++;
	}
	/*唯一标识*/
	protected String id;
	/*创建者*/
	protected String createuserid;
	/*创建时间*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	protected Date createtime;
	/*最后修改人*/
	protected String modifyuserid;
	/*修改时间*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	protected Date modifytime;
	/*行版本号*/
	protected int rowver;
	/*删除标记(0 : 正常  1 : 删除  2 : 审核)*/
	@JsonIgnore
	protected String delflag;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public int getRowver() {
		return rowver;
	}
	public void setRowver(int rowver) {
		this.rowver = rowver;
	}
	public String getDelflag() {
		return delflag;
	}
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}
}