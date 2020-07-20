package com.doosan.biz.ddic.web.system.dao.jpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemAuthed;
/**
 * 	系统权限
 */
public interface SystemAuthedDao extends JpaRepository<SystemAuthed, Integer> {
	/**
	 * 	根据用户获取系统权限
	 * 	@param user
	 * 	@return
	 */
	List<SystemAuthed> findByUser(String user);
}
