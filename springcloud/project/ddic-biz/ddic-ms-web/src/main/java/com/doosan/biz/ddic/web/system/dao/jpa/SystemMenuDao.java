package com.doosan.biz.ddic.web.system.dao.jpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemMenu;
/**
 * 	系统菜单
 */
public interface SystemMenuDao extends JpaRepository<SystemMenu, Integer> {

	/**
	 * 	获取模块下的所有菜单
	 *	@param module
	 * 	@return
	 */
	List<SystemMenu> findByModule(Integer module);
}
