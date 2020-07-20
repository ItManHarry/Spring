package com.doosan.biz.ddic.doc.controller.catalog
import com.doosan.biz.ddic.common.beans.tree.TreeNode
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentAuthed
import com.doosan.biz.ddic.doc.dao.entity.sys.DocumentCatalog
import com.doosan.biz.ddic.doc.service.DocumentAuthedService
import com.doosan.biz.ddic.doc.service.DocumentCatalogService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import javax.persistence.Column
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/doc/biz/catalog")
class CatalogController {
	
	//目录字典 key:目录ID , value:子目录list
	def catalogMap = [:]
	@Autowired
	DocumentCatalogService documentCatalogService
	@Autowired
	DocumentAuthedService documentAuthedService
	/**
	 * 	执行保存
	 * 	@param params
	 * 	@return
	 */
	@PostMapping("/save")
	def save(String params){
		//接收参数
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		//执行保存
		String id = data.get("id").asString
		DocumentCatalog catalog = null
		if(id) {	//id不为空表示修改,否则新增
			catalog = documentCatalogService.getCatalogById(id)
			catalog.setModifyuserid(data.get("user").asString)
			catalog.setModifytime(new Date())
			catalog.setRowversion(catalog.getRowversion()+1)
		}else {
			catalog = new DocumentCatalog()
			catalog.setCreateuserid(data.get("user").asString)
			catalog.setCreatetime(new Date())
			catalog.setRowversion(0)
		}
		catalog.setName(data.get("name").asString)
		catalog.setParentId(data.get("parent").asString)
		catalog.setLeaf(data.get("leaf").asInt)
		catalog.setStatus(data.get("status").asInt)
		documentCatalogService.save(catalog)
		return ServerResultJson.success("OK")
	}
	/**
	 * 	启停目录
	 * 	@param params
	 * 	@return
	 */
	@PostMapping("/status")
	def status(String params){
		//接收参数
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		//执行保存
		String id = data.get("id").asString
		DocumentCatalog catalog = documentCatalogService.getCatalogById(id)
		catalog.setModifyuserid(data.get("user").asString)
		catalog.setModifytime(new Date())
		catalog.setRowversion(catalog.getRowversion()+1)
		catalog.setStatus(data.get("status").asInt)
		documentCatalogService.save(catalog)
		return ServerResultJson.success("OK")
	}
	/**
	 * 	获取总记录数
	 * 	@return
	 */
	@GetMapping("/total")	
	int total() {
		return documentCatalogService.getCount() ? documentCatalogService.getCount().intValue() : 0 
	}
	
	/**
	 * 	获取所有的目录
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@GetMapping("/all")
	def all(Integer page, Integer limit){
		def count = documentCatalogService.getCount() ? documentCatalogService.getCount().intValue() : 0
		def data = documentCatalogService.getAllByPages(page, limit)
		data.each {
			if(it.getStatus() == 0)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
			if(it.getLeaf() == 0)
				it.setLeafStr("否")
			else
				it.setLeafStr("是")
			if(it.getParentId() == "0")
				it.setParentPath("/")
			else {
				def ps = []
				path(it.getParentId(), ps)
				it.setParentPath("/"+ps.reverse().join("/"))
			}
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 	根据user id获取已授权的目录
	 * @param page
	 * @param limit
	 * @param user
	 * @return
	 */
	@GetMapping("/authedCs")
	def authedCs(Integer page, Integer limit, String user){
		List<DocumentAuthed> authed = documentAuthedService.getAuthed(user)
		def ids = []	//已授权目录
		authed.each{
			ids << it.getCatalogId()
		}
		def count = documentCatalogService.getCountByAuthedIds(ids.join(",")) ? documentCatalogService.getCountByAuthedIds(ids.join(",")).intValue() : 0
		def data = documentCatalogService.findByAuthedIds(page, limit, ids.join(","))
		data.each {
			if(it.getStatus() == 0)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
			if(it.getLeaf() == 0)
				it.setLeafStr("否")
			else
				it.setLeafStr("是")
			if(it.getParentId() == "0")
				it.setParentPath("/")
			else {
				def ps = []
				path(it.getParentId(), ps)
				it.setParentPath("/"+ps.reverse().join("/"))
			}
		}
		return ServerResultJson.success(data, count)
	}
	/**
	  *   获取已分配的目录树
	 * @param page
	 * @param limit
	 * @param user
	 * @return
	 */
	@GetMapping("/authedTree")
	def authedTree(String user){
		def count = documentCatalogService.getCount() ? documentCatalogService.getCount().intValue() : 0
		List<DocumentAuthed> authed = documentAuthedService.getAuthed(user)
		def ids = []	//已授权目录
		authed.each{
			ids << it.getCatalogId()
		}
		def data = documentCatalogService.findByAuthedIds(1, count, ids.join(","))
		
		List<TreeNode> all = getCatalogTree()
		def tree = []
		//获取已授权的树节点
		all.each{
			if(ids.contains(it.getId()))
				tree << it
		}
		return ServerResultJson.success(tree)
	}
	/**
	 *	获取文档树 - 全部
	 * 	@return
	 */
	@GetMapping("/tree")
	def tree(){
		return ServerResultJson.success(getCatalogTree())
	}
	/**
	 * 	获取文档树
	 * @return
	 */
	def getCatalogTree() {
		//初始化目录字典
		initCatalogMap()
		//目录树节点
		List<TreeNode> nodes = []
		//获取所有的根目录
		def roots = documentCatalogService.getCatalogsByParentId("0")
		roots.each {
			//只添加在用的节点
			if(it.getStatus() == 1) {
				TreeNode node = new TreeNode(id:it.getId(),
					pId:"0",
					name:it.getName(),
					open:true,
					icon:"/static/images/ztree/folder_close.png",
					iconOpen:"/static/images/ztree/folder_open.png",
					iconClose:"/static/images/ztree/folder_close.png",
					leaf:it.getLeaf()
				)
				//递归添加子节点
				addNodes(node)
				//添加节点
				nodes << node
			}
		}
		return nodes
	}
	/**
	 * 	执行目录授权
	 * 	@param params
	 * 	@return
	 */
	@PostMapping("/auth")
	def auth(String params){
		//接收参数
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		String operator = data.get("user").asString
		String userId = data.get("userId").asString
		String catalogs = data.get("catalogs").asString
		//首先清空之前赋予的权限
		documentAuthedService.batchDelete(documentAuthedService.getAuthed(userId))
		//执行批量保存
		def authed = []
		catalogs.split(",").each { 
			DocumentAuthed auth = new DocumentAuthed()
			auth.setCreateuserid(operator)
			auth.setCreatetime(new Date())
			auth.setRowversion(0)
			auth.setUserId(userId)
			auth.setCatalogId(it)
			authed << auth
		}
		documentAuthedService.batchSave(authed)
		return ServerResultJson.success("OK")
	}
	/**
	 * 	执行目录撤权
	 * 	@param params
	 * 	@return
	 */
	@PostMapping("/undoAuth")
	def undoAuth(String params){
		//接收参数
		JsonObject data = new JsonParser().parse(params).getAsJsonObject()
		String userId = data.get("userId").asString
		//清空之前赋予的权限
		documentAuthedService.batchDelete(documentAuthedService.getAuthed(userId))
		return ServerResultJson.success("OK")
	}
	/**
	 * 	获取已授权目录
	 * 	@param userId
	 * 	@return
	 */
	@GetMapping("/authed")
	def authed(String userId){
		List<DocumentAuthed> authed = documentAuthedService.getAuthed(userId)
		def ids = []	//已授权目录
		authed.each{
			ids << it.getCatalogId()
		}
		return ServerResultJson.success(ids)
	}
	/**
	 * 	初始化目录字典
	 */
	void initCatalogMap() {
		//清空字典
		catalogMap.clear()
		//获取全部的目录
		def all = documentCatalogService.getALL()
		//设置字典
		all.each {
			catalogMap.put(it.getId(), documentCatalogService.getCatalogsByParentId(it.getId()))
		}
	}
	/**
	 * 	递归添加子目录
	 * 	@param node
	 */
	void addNodes(TreeNode node){
		node.setIcon("/static/images/ztree/folder_close.png");
		List<DocumentCatalog> childNodes = catalogMap.get(node.getId());
		if(childNodes != null && childNodes.size() != 0){
			node.setIconOpen("/static/images/ztree/folder_open.png");
			node.setIconClose("/static/images/ztree/folder_close.png");
			List<TreeNode> cns = new ArrayList<TreeNode>();
			for(DocumentCatalog sc : childNodes){
				if(sc.getStatus() == 1) {
					TreeNode cn = new TreeNode(id:sc.getId(),
						pId:sc.getParentId(),
						name:sc.getName(),
						open:true,
						icon:"",
						leaf:sc.getLeaf())
					cns.add(cn)
					addNodes(cn)
				}
			}
			node.setChildren(cns);
		}else{
			return;
		}
	}
	/**
	 * 	递归获取父目录
	 * 	@param id	//父目录ID
	 * 	@param cs	//父目录名称
	 * 	@return
	 */
	void path(String id, List ps) {
		def catalog = documentCatalogService.getCatalogById(id)
		ps << catalog.getName()
		if(catalog.getParentId() != "0")
			path(catalog.getParentId(), ps)
		else
			return;
	}
}