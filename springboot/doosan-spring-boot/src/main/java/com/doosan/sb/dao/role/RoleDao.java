package com.doosan.sb.dao.role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.doosan.sb.dao.domain.Tb_Role;

public interface RoleDao  extends JpaRepository<Tb_Role, Integer>, JpaSpecificationExecutor<Tb_Role> {

}
