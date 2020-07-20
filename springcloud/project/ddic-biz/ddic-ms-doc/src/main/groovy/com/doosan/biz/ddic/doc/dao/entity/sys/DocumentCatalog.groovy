package com.doosan.biz.ddic.doc.dao.entity.sys
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient
import com.doosan.biz.ddic.doc.dao.entity.base.BaseModel
/**
 * 文档目录表
 */
@Entity
@Table(name="doc_catalog")
class DocumentCatalog extends BaseModel implements Serializable {
	//目录名称
	@Column(name="name",length=50)
	String name
	//父目录ID
	@Column(name="parentId",length=36)
	String parentId
	//父目录路径-前端显示
	@Transient
	String parentPath;
	//是否最终目录(0 : 否  1 : 是)
	@Column(name="leaf")
	int leaf
	//是否最终目录-前端显示(是 / 否)
	@Transient
	String leafStr
	//使用状态(0 : 停用  1 : 在用)
	@Column(name="status") 
	int status
	//使用状态-前端显示(在用 / 停用)
	@Transient
	String stsStr
}