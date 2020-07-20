package com.doosan.biz.ddic.hr.dao.mapper.department;
import org.apache.ibatis.annotations.Mapper;
import com.doosan.biz.ddic.hr.dao.entity.department.Tb_Department;
import com.doosan.biz.ddic.hr.dao.mapper.base.BaseMapper;
import com.github.pagehelper.Page;
@Mapper
public interface DepartmentDao extends BaseMapper<Tb_Department> {
	/**
	 * 根据name获取数据条数
	 * @param name
	 * @return
	 */
	int recordCntByTerm(String name);
	/**
	 * 根据name查找全部
	 * @param entity
	 * @return
	 */
	Page<Tb_Department> findAllByTerm(String name);
}
