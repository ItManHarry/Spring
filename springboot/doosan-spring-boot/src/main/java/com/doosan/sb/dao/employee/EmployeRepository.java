package com.doosan.sb.dao.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doosan.sb.dao.domain.Tb_Employee;
/**
 * 参数一：映射的实体
 * 参数二：实体对应的主键(ID)的类型
 */
public interface EmployeRepository extends JpaRepository<Tb_Employee, Integer> {

}