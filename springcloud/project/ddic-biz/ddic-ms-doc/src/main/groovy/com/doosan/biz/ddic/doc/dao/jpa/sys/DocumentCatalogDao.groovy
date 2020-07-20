package com.doosan.biz.ddic.doc.dao.jpa.sys
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentCatalog

interface DocumentCatalogDao extends JpaRepository<DocumentCatalog, String>, JpaSpecificationExecutor<DocumentCatalog>  {
	/**
	 * 	根据父目录ID获取目录
	 * @param parentId
	 * @return
	 */
	List<DocumentCatalog> findByParentId(String parentId)
}
