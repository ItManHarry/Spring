package com.doosan.biz.ddic.web.api.hr.company.fallback;

import org.springframework.stereotype.Component;
import com.doosan.biz.ddic.common.results.ServerResultJson;
import com.doosan.biz.ddic.web.api.hr.company.HrCompanyService;
@Component
public class HrCompanyServiceFallback implements HrCompanyService {

	@Override
	public Object save(String params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object total() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAll(String order, Integer page, Integer limit) {
		// TODO Auto-generated method stub
		return ServerResultJson.success(null, -1);
	}

	@Override
	public Object getCompany(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object modify(String params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object move(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
