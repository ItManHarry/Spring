package com.doosan.biz.ddic.doc.dao.jpa.sys
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentAuthed

interface DocumentAuthedDao extends JpaRepository<DocumentAuthed, String> {
	
	List<DocumentAuthed> findByUserId(String userId)
	
}