package com.doosan.biz.ddic.hr.service.company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.doosan.biz.ddic.hr.dao.entity.company.Tb_Company;
import com.doosan.biz.ddic.hr.dao.mapper.company.CompanyDao;
import com.doosan.biz.ddic.hr.service.base.BaseService;
@Service
public class CompanyService extends BaseService<Tb_Company, CompanyDao> {
	
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(CompanyService.class);
}