package com.doosan.sb.dao.department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.doosan.sb.dao.domain.Tb_Department;

public interface DepartmentDao extends JpaRepository<Tb_Department, Integer>, JpaSpecificationExecutor<Tb_Department> {

}
