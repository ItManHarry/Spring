package com.doosan.sb.dao.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.doosan.sb.dao.domain.Tb_User;

public interface UserDao extends JpaRepository<Tb_User, Integer>, JpaSpecificationExecutor<Tb_User> {

}
