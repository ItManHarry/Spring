package com.doosan.biz.ddic.web.system.dao.jpa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemUser;
/**
 * 	系统用户
 */
public interface SystemUserDao extends JpaRepository<SystemUser, Integer>, JpaSpecificationExecutor<SystemUser> {

	/**
	 * 	根据code获取用户列表
	 * 	@param code
	 * 	@return
	 */
	List<SystemUser> findByCode(String code);
}