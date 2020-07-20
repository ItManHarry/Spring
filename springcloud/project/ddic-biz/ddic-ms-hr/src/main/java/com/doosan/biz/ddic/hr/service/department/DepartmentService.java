package com.doosan.biz.ddic.hr.service.department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.common.exception.ServerException;
import com.doosan.biz.ddic.common.results.ServerResults;
import com.doosan.biz.ddic.hr.dao.entity.department.Tb_Department;
import com.doosan.biz.ddic.hr.dao.mapper.department.DepartmentDao;
import com.doosan.biz.ddic.hr.service.base.BaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
@Service
public class DepartmentService extends BaseService<Tb_Department, DepartmentDao> {
	
	private Logger logger = LoggerFactory.getLogger(DepartmentService.class);
	@Autowired
	private DepartmentDao departmentDao;
	@Transactional(propagation=Propagation.SUPPORTS)
	public int recordCntByTerm(String name){
		try{
			return departmentDao.recordCntByTerm(name);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	public Page<Tb_Department> findAllByTerm(String order, Integer page, Integer pageSize, String name){
		try{
			if(page == null)
				page = 1;
			if(pageSize == null)
				pageSize = 10;
			Page<Tb_Department> pageHelper = PageHelper.startPage(page, pageSize, order);
			return departmentDao.findAllByTerm(name);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
	}
}