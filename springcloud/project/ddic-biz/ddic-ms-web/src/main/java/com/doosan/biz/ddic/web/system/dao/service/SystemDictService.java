package com.doosan.biz.ddic.web.system.dao.service;
import java.util.List;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemDict;
/**
 * 	系统字典
 */
public interface SystemDictService {

	/**
	 * 	获取记录总数
	 * 	@return
	 */
	long getCount();
	/**
	 * 	分页获取字典数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemDict> getAllByPages(Integer page, Integer limit);
	/**
	 * 	新增修改下拉字典
	 * 	@param dict
	 */
	void save(SystemDict dict);
	/**
	 * 	批量新增修改下拉字典
	 * 	@param dicts
	 */
	void batchSave(List<SystemDict> dicts);
	/**
	 * 	根据id删除下拉字典
	 * 	@param tid
	 */
	void delete(Integer tid);
	/**
	 * 	批量删除下拉字典
	 * 	@param dicts
	 */
	void batchDelete(List<SystemDict> dicts);
	/**
	 * 	根据code获取字典
	 * 	@param code
	 * 	@return
	 */
	SystemDict getDictByCode(String code);
	/**
	 * 	根据ID获取字典数据
	 * 	@param id
	 * 	@return
	 */
	SystemDict getDictById(Integer id);
}