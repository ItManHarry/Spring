package com.doosan.biz.ddic.web.api.doc.catalog.fallback
import org.springframework.stereotype.Component
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.api.doc.catalog.DocumentCatalogService
@Component
class DocumentCatalogServiceFallback implements DocumentCatalogService {

	@Override
	public Object save(String params) {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	public int total() {
		// TODO Auto-generated method stub
		return 0
	}

	@Override
	public Object all(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		return ServerResultJson.success([])
	}

	@Override
	public Object status(String params) {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	public Object tree() {
		// TODO Auto-generated method stub
		return ServerResultJson.success([])
	}

	@Override
	public Object auth(String params) {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	public Object undoAuth(String params) {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	public Object authed(String userId) {
		// TODO Auto-generated method stub
		return null
	}

	@Override
	public Object authedCs(Integer page, Integer limit, String user) {
		// TODO Auto-generated method stub
		return ServerResultJson.success([])
	}

	@Override
	public Object authedTree(String user) {
		// TODO Auto-generated method stub
		return ServerResultJson.success([])
	}
}
