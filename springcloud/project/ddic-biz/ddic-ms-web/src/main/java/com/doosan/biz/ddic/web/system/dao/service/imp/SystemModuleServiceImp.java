package com.doosan.biz.ddic.web.system.dao.service.imp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemModule;
import com.doosan.biz.ddic.web.system.dao.jpa.SystemModuleDao;
import com.doosan.biz.ddic.web.system.dao.service.SystemModuleService;
@Service
public class SystemModuleServiceImp implements SystemModuleService {

	@Autowired
	private SystemModuleDao systemModuleDao;
	
	@Transactional
	public long getCount() {
		// TODO Auto-generated method stub
		return systemModuleDao.count();
	}

	@Transactional
	public List<SystemModule> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("moduleNm");
		Pageable pageable = PageRequest.of(page-1, limit, sort);//page从0开始
		Page<SystemModule> pageData = systemModuleDao.findAll(pageable);
		return pageData.getContent();	
	}

	@Transactional
	public void save(SystemModule module) {
		// TODO Auto-generated method stub
		systemModuleDao.save(module);
	}

	@Transactional
	public SystemModule getModuleById(Integer id) {
		// TODO Auto-generated method stub
		return systemModuleDao.getOne(id);
	}

	@Transactional
	public List<SystemModule> getAll() {
		// TODO Auto-generated method stub
		return systemModuleDao.findAll();
	}

}
