package com.doosan.biz.ddic.web.system.dao.service.imp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemMenu;
import com.doosan.biz.ddic.web.system.dao.jpa.SystemMenuDao;
import com.doosan.biz.ddic.web.system.dao.service.SystemMenuService;
@Service
public class SystemMenuServiceImp implements SystemMenuService {

	@Autowired
	private SystemMenuDao systemMenuDao;
	
	@Transactional
	public long getCount() {
		// TODO Auto-generated method stub
		return systemMenuDao.count();
	}

	@Transactional
	public List<SystemMenu> getAll() {
		// TODO Auto-generated method stub
		return systemMenuDao.findAll();
	}

	@Transactional
	public List<SystemMenu> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("module");
		Pageable pageable = PageRequest.of(page-1, limit, sort);//page从0开始
		Page<SystemMenu> pageData = systemMenuDao.findAll(pageable);
		return pageData.getContent();
	}

	@Transactional
	public void save(SystemMenu menu) {
		// TODO Auto-generated method stub
		systemMenuDao.save(menu);
	}

	@Transactional
	public SystemMenu getMenuById(Integer id) {
		// TODO Auto-generated method stub
		return systemMenuDao.getOne(id);
	}

	@Override
	public List<SystemMenu> getByModule(Integer module) {
		// TODO Auto-generated method stub
		return systemMenuDao.findByModule(module);
	}
}
