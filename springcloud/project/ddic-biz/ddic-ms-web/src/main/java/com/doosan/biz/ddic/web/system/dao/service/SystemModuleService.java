package com.doosan.biz.ddic.web.system.dao.service;
import java.util.List;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemModule;
/**
 * 	系统模块
 */
public interface SystemModuleService {

	/**
	 *	 获取记录总数
	 * 	@return
	 */
	long getCount();
	/**
	 * 	获取所有的模块
	 * 	@return
	 */
	List<SystemModule> getAll();
	/**
	 * 	分页获取模块数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemModule> getAllByPages(Integer page, Integer limit);
	/**
	 * 	新增修改模块
	 * 	@param module
	 */
	void save(SystemModule module);
	/**
	 * 	根据ID获取模块
	 * 	@param id
	 * 	@return
	 */
	SystemModule getModuleById(Integer id);
}