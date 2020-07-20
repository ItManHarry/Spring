package com.doosan.biz.ddic.doc.service
import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentCatalog
/**
 * 	文档目录Service
 * 	@author 20112004
 */
interface DocumentCatalogService {	
	/**
	 * 获取记录总数
	 * @return
	 */
	int getCount()
	/**
	 * 根据已授权id获取目录总数
	 * @param ids
	 * @return
	 */
	long getCountByAuthedIds(String ids)
	/**
	 * 分页获取文档目录数据
	 * @param page
	 * @param limit
	 * @return
	 */
	List<DocumentCatalog> getAllByPages(Integer page, Integer limit)
	/**
	 * 根据已授权id分页获取文档目录数据
	 * @param page
	 * @param limit
	 * @param ids
	 * @return
	 */
	List<DocumentCatalog> findByAuthedIds(Integer page, Integer limit, String ids)
	/**
	 * 新增/修改文档目录
	 * @param document
	 */
	void save(DocumentCatalog document)
	/**
	 * 根据ID获取文档目录
	 * @param id
	 * @return
	 */
	DocumentCatalog getCatalogById(String id)
	/**
	 * 	获取所有的目录
	 * @return
	 */
	List<DocumentCatalog> getALL()
	/**
	 * 	根据父目录ID获取所有的子目录
	 * @param parentId
	 * @return
	 */
	List<DocumentCatalog> getCatalogsByParentId(String parentId)
}
