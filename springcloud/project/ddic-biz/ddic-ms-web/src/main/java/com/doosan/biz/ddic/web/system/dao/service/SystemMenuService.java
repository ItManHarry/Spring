package com.doosan.biz.ddic.web.system.dao.service;
import java.util.List;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemMenu;
/**
 * 	系统菜单
 */
public interface SystemMenuService {

	/**
	 *	 获取记录总数
	 * 	@return
	 */
	long getCount();
	/**
	 * 	获取所有的菜单
	 * 	@return
	 */
	List<SystemMenu> getAll();
	/**
	 * 	分页获取菜单数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemMenu> getAllByPages(Integer page, Integer limit);
	/**
	 * 	新增修改菜单
	 * 	@param menu
	 */
	void save(SystemMenu menu);
	/**
	 * 	根据ID获取菜单
	 * 	@param id
	 * 	@return
	 */
	SystemMenu getMenuById(Integer id);
	/**
	 * 根据模块ID获取模块下的所有菜单
	 * @param module
	 * @return
	 */
	List<SystemMenu> getByModule(Integer module);
}