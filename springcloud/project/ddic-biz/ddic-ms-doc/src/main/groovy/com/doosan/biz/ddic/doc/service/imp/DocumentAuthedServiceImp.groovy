package com.doosan.biz.ddic.doc.service.imp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentAuthed
import com.doosan.biz.ddic.doc.dao.jpa.sys.DocumentAuthedDao
import com.doosan.biz.ddic.doc.service.DocumentAuthedService
@Service
class DocumentAuthedServiceImp implements DocumentAuthedService {
	
	@Autowired
	DocumentAuthedDao documentAuthedDao

	@Transactional
	public List<DocumentAuthed> getAuthed(String userId) {
		return documentAuthedDao.findByUserId(userId)
	}

	@Transactional
	public void batchSave(List<DocumentAuthed> authed) {
		documentAuthedDao.saveAll(authed)
	}

	@Transactional
	public void batchDelete(List<DocumentAuthed> authed) {
		documentAuthedDao.deleteAll(authed)
	}
}