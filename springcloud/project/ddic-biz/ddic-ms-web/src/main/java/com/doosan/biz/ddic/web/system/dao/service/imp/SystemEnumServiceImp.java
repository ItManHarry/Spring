package com.doosan.biz.ddic.web.system.dao.service.imp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemEnum;
import com.doosan.biz.ddic.web.system.dao.jpa.SystemEnumDao;
import com.doosan.biz.ddic.web.system.dao.service.SystemEnumService;
@Service
public class SystemEnumServiceImp implements SystemEnumService{
	@Autowired
	private SystemEnumDao systemEnumDao;
	
	@Transactional
	public void save(SystemEnum em) {
		systemEnumDao.save(em);
	}
	
	@Transactional
	public SystemEnum getEnumById(Integer id){
		return systemEnumDao.getOne(id);
	}
	
	@Transactional
	public List<SystemEnum> findByDictId(int dictId){
		return systemEnumDao.findByDictId(dictId);
	}

	@Transactional
	public void deleteByDictId(int dictId) {
		List<SystemEnum> enums = findByDictId(dictId);
		systemEnumDao.deleteAll(enums);
	}
}