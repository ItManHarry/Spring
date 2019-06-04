package com.doosan.sb.dao.user;
import java.util.List;
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
	/**
	 * 查询所有用户
	 * @return
	 */
	List<SysUser> all();
	/**
	 * 根据id获取用户
	 * @param tid
	 * @return
	 */
	SysUser getUserById(int tid);
	/**
	 * 更新用户
	 * @param user
	 */
	int update(SysUser user);
	/**
	 * 删除用户
	 * @param tid
	 * @return
	 */
	int delete(int tid);
}