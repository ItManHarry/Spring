package com.doosan.biz.ddic.web.system.dao.service.imp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemUser;
import com.doosan.biz.ddic.web.system.dao.jpa.SystemUserDao;
import com.doosan.biz.ddic.web.system.dao.service.SystemUserService;
@Service
public class SystemUserServiceImp implements SystemUserService {

	@Autowired
	private SystemUserDao systemUserDao;
	
	@Transactional
	public long getCount() {
		// TODO Auto-generated method stub
		return systemUserDao.count();
	}

	@Transactional
	public List<SystemUser> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name");
		Pageable pageable = PageRequest.of(page-1, limit, sort);//page从0开始
		Page<SystemUser> pageData = systemUserDao.findAll(pageable);
		return pageData.getContent();
	}

	@Transactional
	public void save(SystemUser user) {
		// TODO Auto-generated method stub
		systemUserDao.save(user);
	}

	@Transactional
	public SystemUser getUserByCode(String code) {
		// TODO Auto-generated method stub
		List<SystemUser> users = systemUserDao.findByCode(code);
		if(users != null && users.size() != 0)
			return users.get(0);
		else
			return null;
	}

	@Transactional
	public SystemUser getUserById(Integer id) {
		// TODO Auto-generated method stub
		return systemUserDao.getOne(id);
	}
	
	@Transactional
	public List<SystemUser> findByNameAndCode(Integer page, Integer limit, String name, String code) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name");
		Pageable pageable = PageRequest.of(page-1, limit, sort);//page从0开始
		Page<SystemUser> pageData = systemUserDao.findAll(getSpec(name, code), pageable);
		return pageData.getContent();
	}
	
	@Transactional
	public long getCountByNameAndCode(String name, String code) {
		// TODO Auto-generated method stub
		return systemUserDao.count(getSpec(name, code));
	}
	/**
	 * 查询条件
	 * @param name
	 * @param code
	 * @return
	 */
	@SuppressWarnings("serial")
	public Specification<SystemUser> getSpec(String name, String code) {
		// TODO Auto-generated method stub
		Specification<SystemUser> spec = new Specification<SystemUser>(){
			/**
			 * Root<SystemUser>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *  
			 */
			public Predicate toPredicate(Root<SystemUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// where name like ? and gender like ?
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.like(root.get("name"), "%"+name+"%"));
				predicates.add(builder.like(root.get("code"), "%"+code+"%"));
				Predicate[] predicateArray = new Predicate[predicates.size()];
				return builder.and(predicates.toArray(predicateArray));
			}
		};
		return spec;
	}
}