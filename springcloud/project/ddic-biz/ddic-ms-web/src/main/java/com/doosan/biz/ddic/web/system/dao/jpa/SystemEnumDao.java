package com.doosan.biz.ddic.web.system.dao.jpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemEnum;
/**
 * 	系统字典枚举值
 */
public interface SystemEnumDao extends JpaRepository<SystemEnum, Integer> {
	/**
	 * 	根据字典ID获取枚举值
	 * 	@param dictId
	 * 	@return
	 */
	List<SystemEnum> findByDictId(int dictId);
}