package com.doosan.biz.ddic.web.system.dao.jpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemDict;
/**
 * 	系统下拉列表字典
 */
public interface SystemDictDao extends JpaRepository<SystemDict, Integer> {
	/**
	 * 	根据code获取字典列表
	 * 	@param code
	 * 	@return
	 */
	List<SystemDict> findByCode(String code);
}