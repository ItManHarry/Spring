package com.doosan.sb.service.imp.employee;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.sb.dao.domain.Tb_Employee;
import com.doosan.sb.dao.employee.EmployeeDao;
import com.doosan.sb.service.employee.EmployeeService;
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	@Cacheable(value="employee") //启用缓存,缓存名称为：employee
	public List<Tb_Employee> findAll() {
		// TODO Auto-generated method stub
		return employeeDao.findAll();
	}

	@Override
	@Cacheable(value="employee") //启用缓存,缓存名称为：employee
	public Tb_Employee findById(int id) {
		// TODO Auto-generated method stub
		return employeeDao.findOne(id);
	}

	@Override
	@Cacheable(value="employee") //启用缓存,缓存名称为：employee
	public Page<Tb_Employee> findByPage(Pageable page) {
		// TODO Auto-generated method stub
		return employeeDao.findAll(page);
	}

	@Override
	@CacheEvict(value="employee",allEntries=true)
	public void save(Tb_Employee employee) {
		// TODO Auto-generated method stub
		employeeDao.save(employee);
	}
}