package com.doosan.sb.service.employee;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.doosan.sb.dao.domain.Tb_Employee;

public interface EmployeeService {

	List<Tb_Employee> findAll();
	Tb_Employee findById(int id);
	Page<Tb_Employee> findByPage(Pageable page);
	void save(Tb_Employee employee);
}