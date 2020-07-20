package com.doosan.biz.ddic.web.system.dao.service;
import java.util.List;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemUser;
/**
 * 	系统用户
 */
public interface SystemUserService {

	/**
	 *	 获取记录总数
	 * 	@return
	 */
	long getCount();
	/**
	 * 	分页获取用户数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemUser> getAllByPages(Integer page, Integer limit);
	/**
	 * 	新增修改用户
	 * 	@param user
	 */
	void save(SystemUser user);
	/**
	 * 	根据账号获取用户
	 * 	@param code
	 * 	@return
	 */
	SystemUser getUserByCode(String code);
	/**
	 * 	根据ID获取用户
	 * 	@param id
	 * 	@return
	 */
	SystemUser getUserById(Integer id);
	/**
	 * 根据用户密码和代码进行模糊分页查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @return
	 */
	List<SystemUser> findByNameAndCode(Integer page, Integer limit, String name, String code);
	/**
	 * 根据用户密码和代码进行模糊分页查询记录数
	 * @param name
	 * @param code
	 * @return
	 */
	long getCountByNameAndCode(String name, String code);
}