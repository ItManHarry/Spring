package com.doosan.biz.ddic.web.controller.system.auth
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.biz.ddic.common.beans.tree.TreeNode
import com.doosan.biz.ddic.common.results.ServerResultJson
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemAuthed
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemMenu
import com.doosan.biz.ddic.web.system.dao.domain.sys.SystemModule
import com.doosan.biz.ddic.web.system.dao.service.SystemAuthedService
import com.doosan.biz.ddic.web.system.dao.service.SystemMenuService
import com.doosan.biz.ddic.web.system.dao.service.SystemModuleService
import com.doosan.biz.ddic.web.system.dao.service.SystemUserService
@Controller
@RequestMapping("/web/system/auth")
class SystemAuthController {
	
	final String WEB_URL = "system/auth"
	@Autowired
	SystemModuleService systemModuleService
	@Autowired
	SystemMenuService systemMenuService	
	@Autowired
	SystemUserService systemUserService
	@Autowired
	SystemAuthedService systemAuthedService
	/**
	 *	 跳转系统授权
	 * 	 @return
	 */
	@RequestMapping("/index")
	def index(HttpServletRequest request, Map map) {
		def module = request.getParameter("module")
		map.put("module", module)
		//用户总数
		def total = systemUserService.getCount().intValue()
		map.put("total", total)
		return WEB_URL + "/index"
	}
	/**
	 *	获取文档树
	 * 	@return
	 */
	@GetMapping("/tree")
	@ResponseBody
	def tree(){
		//权限树节点
		def nodes = []
		//获取所有的根目录
		def modules = systemModuleService.getAll().findAll{
			it.getDelflag() == "0"
		}
		modules.each {
			TreeNode node = new TreeNode(id:it.getTid()+"",
				pId:"0",
				name:it.getModuleNm(),
				open:true,
				icon:"",
				iconOpen:"",
				iconClose:"",
				leaf:0
			)
			//添加菜单
			addMenu(node)
			//添加节点
			nodes << node
		}
		return ServerResultJson.success(nodes)
	}
	/**
	 * 	添加菜单
	 * 	@param node
	 */
	void addMenu(TreeNode node){
		node.setIcon("");
		List<SystemMenu> menus = systemMenuService.getByModule(Integer.parseInt(node.getId()))
		if(menus != null && menus.size() != 0){
			node.setIconOpen("");
			node.setIconClose("");
			List<TreeNode> cns = new ArrayList<TreeNode>();
			for(SystemMenu menu : menus){
				if(menu.getDelflag() == "0") {
					TreeNode cn = new TreeNode(id:menu.getTid()+"",
						pId:menu.getModule()+"",
						name:menu.getName(),
						open:true,
						icon:"",
						leaf:1)
					cns.add(cn)
					addMenu(cn)
				}
			}
			node.setChildren(cns);
		}else{
			return;
		}
	}
	/**
	 *	执行系统授权
	 *	@param params
	 * 	@return
	*/
   @PostMapping("/doAuth")
   @ResponseBody
   def doAuth(HttpServletRequest request){
	   //接收参数
	   String operator = request.getSession().getAttribute("currentUser").toString()
	   String user = request.getParameter("user")
	   String menus = request.getParameter("menus")
	   //首先清空之前赋予的权限
	   systemAuthedService.deleteByUser(user)
	   //执行批量保存
	   def authed = []
	   def module = "", menu = ""
	   menus.split(",").each {
		   menu = it.split("/")[0]
		   module = it.split("/").size()>1?it.split("/")[1]:it.split("/")[0]		   
		   SystemAuthed auth = new SystemAuthed()
		   auth.setCreateuserid(operator)
		   auth.setCreatetime(new Date())
		   auth.setRowversion(0)
		   auth.setUser(user)
		   auth.setModule(Integer.parseInt(module))
		   auth.setMenu(Integer.parseInt(menu))
		   authed << auth
	   }
	   systemAuthedService.batchSave(authed)
	   return ServerResultJson.success("OK")
   }
   /**
	* 	执行目录撤权
	* 	@param params
	* 	@return
	*/
   @PostMapping("/undoAuth")
   @ResponseBody
   def undoAuth(HttpServletRequest request){
	   //接收参数
	   String user = request.getParameter("user")
	   //清空之前赋予的权限
	   systemAuthedService.deleteByUser(user)
	   return ServerResultJson.success("OK")
   }
   /**
	* 	获取已授权目录
	* 	@param userId
	* 	@return
	*/
   @GetMapping("/authed")
   @ResponseBody
   def authed(String user){
	   List<SystemAuthed> authed = systemAuthedService.findByUser(user)
	   def ids = []	//已授权权限
	   authed.each{
		   ids << it.getMenu()
	   }
	   return ServerResultJson.success(ids)
   }
}