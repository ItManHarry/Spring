package com.doosan.biz.ddic.web.system.dao.service.imp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemAuthed;
import com.doosan.biz.ddic.web.system.dao.jpa.SystemAuthedDao;
import com.doosan.biz.ddic.web.system.dao.service.SystemAuthedService;
@Service
public class SystemAuthedServiceImp implements SystemAuthedService {

	@Autowired
	private SystemAuthedDao systemAuthedDao;
	
	@Transactional
	public void batchSave(List<SystemAuthed> authes) {
		// TODO Auto-generated method stub
		systemAuthedDao.saveAll(authes);
	}

	@Transactional
	public List<SystemAuthed> findByUser(String user) {
		// TODO Auto-generated method stub
		return systemAuthedDao.findByUser(user);
	}

	@Transactional
	public void deleteByUser(String user) {
		// TODO Auto-generated method stub
		List<SystemAuthed> authes = findByUser(user);
		systemAuthedDao.deleteAll(authes);
	}
}