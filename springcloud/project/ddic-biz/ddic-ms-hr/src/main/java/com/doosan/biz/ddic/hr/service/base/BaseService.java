package com.doosan.biz.ddic.hr.service.base;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.biz.ddic.common.exception.ServerException;
import com.doosan.biz.ddic.common.results.ServerResults;
import com.doosan.biz.ddic.hr.dao.entity.base.BaseEntity;
import com.doosan.biz.ddic.hr.dao.mapper.base.BaseMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
/**
 * 数据操作Service
 * @author 20112004
 * @param <T>
 * @param <M>
 */
public abstract class BaseService<T extends BaseEntity, M extends BaseMapper<T>> {
	private Logger logger = LoggerFactory.getLogger(BaseService.class);
	@Autowired
	protected M dao;
	/**
	 * 查找全部
	 * @param entity
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<T> findAll(T entity){
		try{
			return dao.findAll(entity);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
	}
	/**
	 * 根据条件进行查找 - 含分页功能
	 * @param entity
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<T> findAll(T entity, String order, Integer page, Integer pageSize){
		try{
			if(page == null)
				page = 1;
			if(pageSize == null)
				pageSize = 10;
			Page<T> pageHelper = PageHelper.startPage(page, pageSize, order);
			return dao.findAll(entity);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
	}
	/**
	 * 查询记录条数
	 * @param entity
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public int recordCnt(T entity){
		try{
			return dao.recordCnt(entity);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
	}
	/**
	 * 查找单条数据
	 * @param id
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public T findById(String id){
		try{
			return dao.findById(id);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
	}
	/**
	 * 保存/修改 数据
	 * @param entity
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public T save(T entity){
		try{
			int i = 0;
			if(entity.getIsNewRecord()){
				entity.prepareInsert();
				i = dao.insert(entity);
			}else{
				entity.prepareUpdate();
				i = dao.update(entity);
			}
			if(i == -1)
				throw new ServerException(ServerResults.ERROR);
		}catch(DuplicateKeyException e){
			logger.error(e.getCause().getMessage(), e);
			throw new ServerException(ServerResults.ERROR_KEY);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
		return entity;
	}
	/**
	 * 删除数据
	 * @param ids
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(String[] ids){
		try{
			return dao.delete(ids);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServerException(ServerResults.ERROR);
		}
	}
}