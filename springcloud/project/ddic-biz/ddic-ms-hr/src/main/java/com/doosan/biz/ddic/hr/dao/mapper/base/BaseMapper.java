package com.doosan.biz.ddic.hr.dao.mapper.base;
import org.apache.ibatis.annotations.Param;
import com.doosan.biz.ddic.hr.dao.entity.base.BaseEntity;
import com.github.pagehelper.Page;
/**
 * 基本Dao层
 * @author 20112004
 *
 */
public interface BaseMapper<T extends BaseEntity> {

	/**
	 * 查找全部
	 * @param entity
	 * @return
	 */
	Page<T> findAll(T entity);
	/**
	 * 根据ID查找一条数据
	 * @param id
	 * @return
	 */
	T findById(String id);
	/**
	 * 新增数据
	 * @param entity
	 * @return
	 */
	int insert(T entity);
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	int update(T entity);
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	int delete(@Param(value="ids") String[] ids);
	/**
	 * 查询总记录数据
	 * @param entity
	 * @return
	 */
	int recordCnt(T entity);
}
