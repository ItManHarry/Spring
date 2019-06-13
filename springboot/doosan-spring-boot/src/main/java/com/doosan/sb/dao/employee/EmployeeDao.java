package com.doosan.sb.dao.employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.doosan.sb.dao.domain.Tb_Employee;

public interface EmployeeDao extends JpaRepository<Tb_Employee, Integer>, JpaSpecificationExecutor<Tb_Employee> {
	//根据名称列进行查询(此处默认是equals,等同于:findByNameQqual)
	List<Tb_Employee> findByName(String name);
	//根据姓名和性别查询
	List<Tb_Employee> findByNameAndGender(String name, String gender);
	//模糊查询
	List<Tb_Employee> findByTelphoneLike(String telphone);	
	//使用@Query注解HQL查询
	@Query("from Tb_Employee where name = ?")
	List<Tb_Employee> queryByNameHQL(String name);
	//使用@Query注解SQL查询
	@Query(value="select * from Tb_Employee where name = ?",nativeQuery=true)
	List<Tb_Employee> queryByNameSQL(String name);
	@Query("update Tb_Employee set address = ? where tid = ?")
	@Modifying	//执行更新时,必须增加此注解
	void updateEmployeeById(String address, int id);
}