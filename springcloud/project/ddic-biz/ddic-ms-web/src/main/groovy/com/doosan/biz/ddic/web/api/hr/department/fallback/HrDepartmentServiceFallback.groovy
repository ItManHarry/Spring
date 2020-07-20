package com.doosan.biz.ddic.web.api.hr.department.fallback
import org.springframework.stereotype.Component
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.api.hr.department.HrDepartmentService
@Component
class HrDepartmentServiceFallback implements HrDepartmentService {

	@Override
	public Object getAll(String order, Integer page, Integer limit, String name) {
		// TODO Auto-generated method stub
		return ServerResultJson.success(null, -1);
	}

	@Override
	public Object total() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object save(String params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getDepartment(String id) {
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
