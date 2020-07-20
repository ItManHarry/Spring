package com.doosan.biz.ddic.web.system.dao.service;
import java.util.List;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemEnum;
/**
 * 	字典枚举
 */
public interface SystemEnumService {
	/**
	 * 	新增/修改枚举
	 * 	@param em
	 */
	void save(SystemEnum em);
	/**
	 * 	根据ID获取枚举数据
	 * 	@param id
	 * 	@return
	 */
	SystemEnum getEnumById(Integer id);
	/**
	 * 	根据字典ID获取枚举值
	 * 	@param dictId
	 * 	@return
	 */
	List<SystemEnum> findByDictId(int dictId);
	/**
	 * 	根据字典ID删除枚举值
	 * 	@param dictId
	 */
	void deleteByDictId(int dictId);
}