package com.doosan.sb.dao.employee;
import java.util.List;
import org.springframework.data.repository.Repository;
import com.doosan.sb.dao.domain.Tb_Employee;

public interface EmployeeQueryRepository extends Repository<Tb_Employee, Integer> {
	//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
	List<Tb_Employee> findByName(String name);
	//根据姓名和性别查询
	List<Tb_Employee> findByNameAndGender(String name, String gender);
	//模糊查询
	List<Tb_Employee> findByTelphoneLike(String telphone);
}
