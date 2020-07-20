package com.doosan.biz.ddic.web.system.dao.service;
import java.util.List;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemAuthed;
/**
 * 	用户授权
 */
public interface SystemAuthedService {
	/**
	 * 	批量保存权限
	 * 	@param auth
	 */
	void batchSave(List<SystemAuthed> authes);
	/**
	 * 	根据用户账号获取模块菜单权限
	 * 	@param user
	 * 	@return
	 */
	List<SystemAuthed> findByUser(String user);
	/**
	 * 	根据用户账号删除模块菜单权限
	 * 	@param user
	 */
	void deleteByUser(String user);
}
