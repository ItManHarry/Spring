package com.doosan.biz.ddic.web.system.dao.jpa;
import org.springframework.data.jpa.repository.JpaRepository;
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemModule;
/**
 * 	系统模块
 */
public interface SystemModuleDao extends JpaRepository<SystemModule, Integer> {

}
