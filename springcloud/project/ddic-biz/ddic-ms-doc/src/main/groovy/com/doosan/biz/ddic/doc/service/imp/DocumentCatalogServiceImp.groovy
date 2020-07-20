package com.doosan.biz.ddic.doc.service.imp
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaBuilder.In
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentCatalog
import com.doosan.biz.ddic.doc.dao.jpa.sys.DocumentCatalogDao
import com.doosan.biz.ddic.doc.service.DocumentCatalogService
@Service
class DocumentCatalogServiceImp implements DocumentCatalogService {
	
	@Autowired
	DocumentCatalogDao documentCatalogDao
	
	@Transactional
	int getCount() {
		return documentCatalogDao.count().intValue()
	}
	@Transactional
	List<DocumentCatalog> getAllByPages(Integer page, Integer limit) {
		Sort sort = Sort.by("name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)		//page从0开始
		Page<DocumentCatalog> pageData = documentCatalogDao.findAll(pageable)
		return pageData.getContent()
	}
	@Transactional
	void save(DocumentCatalog document) {
		documentCatalogDao.save(document)
	}
	@Transactional
	DocumentCatalog getCatalogById(String id) {
		return documentCatalogDao.getOne(id)
	}
	@Transactional
	List<DocumentCatalog> getALL(){
		return documentCatalogDao.findAll()
	}
	@Transactional
	List<DocumentCatalog> getCatalogsByParentId(String parentId){
		return documentCatalogDao.findByParentId(parentId)
	}
	@Transactional
	public List<DocumentCatalog> findByAuthedIds(Integer page, Integer limit, String ids) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)//page从0开始
		Page<DocumentCatalog> pageData = documentCatalogDao.findAll(getSpec(ids), pageable)
		return pageData.getContent()
	}
	
	@Transactional
	public long getCountByAuthedIds(String ids) {
		// TODO Auto-generated method stub
		return documentCatalogDao.count(getSpec(ids))
	}
	/**
	 * 查询条件
	 * @param name
	 * @param code
	 * @return
	 */
	public Specification<DocumentCatalog> getSpec(String ids) {
		// TODO Auto-generated method stub
		Specification<DocumentCatalog> spec = new Specification<DocumentCatalog>(){
			/**
			 * Root<DocumentCatalog>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *
			 */
			public Predicate toPredicate(Root<DocumentCatalog> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				//id list
				def idList = ids.split(",")
				List<Predicate> predicates = new ArrayList<Predicate>()
				In<String> ins = builder.in(root.get("id"));
				for(String id : idList) {
					ins.value(id)
				}
				// where id in (ids) ?
				predicates.add(ins)
				Predicate[] predicateArray = new Predicate[predicates.size()]
				return builder.and(predicates.toArray(predicateArray))
			}
		}
		return spec
	}
}