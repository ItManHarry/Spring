package com.doosan.biz.ddic.common.beans.tree
/**
 * 	树节点
 */
class TreeNode implements Serializable {
	
	String 	id			//目录ID
	String 	pId 		//父目录ID
	String 	name		//目录名称
	boolean open		//是否展开
	String  icon		//图标
	String 	iconOpen	//展开时的图标
	String 	iconClose	//闭合时的图标
	int     leaf		//是否是最终目录(0:否 1:是)
	List<TreeNode> children//子节点
}