package com.doosan.biz.ddic.doc.service
import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentAuthed
/**
 * 	文档权限Service
 * 	@author 20112004
 */
interface DocumentAuthedService {
	/**
	 * 	获取已授权目录
	 * 	@param userId
	 * 	@return
	 */
	List<DocumentAuthed> getAuthed(String userId)
	/**
	 * 	批量保存
	 * 	@param authed
	 */
	void batchSave(List<DocumentAuthed> authed)
	/**
	 * 	批量删除
	 * 	@param authed
	 */
	void batchDelete(List<DocumentAuthed> authed)
}