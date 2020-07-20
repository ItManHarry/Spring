package com.doosan.biz.ddic.web.system.dao.service.imp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemDict;
import com.doosan.biz.ddic.web.system.dao.jpa.SystemDictDao;
import com.doosan.biz.ddic.web.system.dao.service.SystemDictService;
import com.doosan.biz.ddic.web.system.dao.service.SystemEnumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
@Service
public class SystemDictServiceImp implements SystemDictService{

	@Autowired
	private SystemDictDao systemDictDao;
	@Autowired
	private SystemEnumService systemEnumService;
	
	@Transactional
	public long getCount() {
		return systemDictDao.count();
	}
	@Transactional
	public List<SystemDict> getAllByPages(Integer page, Integer limit){
		//Sort sort = new Sort(Sort.Direction.DESC, "code");		//code降序
		//sort.and(new Sort(Sort.Direction.ASC, "value"));		//value升序
		Sort sort = Sort.by("code","name");
		Pageable pageable = PageRequest.of(page-1, limit, sort);//page从0开始
		Page<SystemDict> pageData = systemDictDao.findAll(pageable);
		return pageData.getContent();		
	}
	
	@Transactional
	public void save(SystemDict dict) {
		systemDictDao.save(dict);
	}
	
	@Transactional
	public void batchSave(List<SystemDict> dicts) {
		systemDictDao.saveAll(dicts);
	}
	
	@Transactional
	public void delete(Integer tid) {
		//删除字典对应的枚举值
		systemEnumService.deleteByDictId(tid);
		//删除字典
		systemDictDao.deleteById(tid);
	}
	
	@Transactional
	public void batchDelete(List<SystemDict> dicts) {
		systemDictDao.deleteAll(dicts);
	}
	
	@Transactional
	public SystemDict getDictByCode(String code){
		List<SystemDict> dicts = systemDictDao.findByCode(code);
		if(dicts == null || dicts.size() == 0)
			return null;
		else
			return dicts.get(0);
	}
	
	@Transactional
	public SystemDict getDictById(Integer id){
		return systemDictDao.getOne(id);
	}
}