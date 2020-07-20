package com.doosan.biz.ddic.doc.dao.entity.sys
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.biz.ddic.doc.dao.entity.base.BaseModel
@Entity
@Table(name="doc_authed")
class DocumentAuthed extends BaseModel implements Serializable {
	//用户ID
	@Column(name="userId",length=20)
	String userId
	//目录ID
	@Column(name="catalogId",length=36)
	String catalogId
}