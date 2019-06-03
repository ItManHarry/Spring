package com.doosan.sb.dao;
import com.doosan.sb.dao.domain.SysUser;
/**
 * 系统用户操作
 */
public interface SysUserMapper {
	/**
	 * 保存用户
	 * @param user
	 */
	void save(SysUser user);
}