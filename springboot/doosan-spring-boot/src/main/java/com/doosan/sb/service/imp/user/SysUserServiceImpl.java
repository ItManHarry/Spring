package com.doosan.sb.service.imp.user;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.sb.dao.domain.SysUser;
import com.doosan.sb.dao.user.SysUserMapper;
import com.doosan.sb.service.user.SysUserService;
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

	@Override
	public void save(SysUser user) {
		sysUserMapper.save(user);
	}
	@Override
	public List<SysUser> all() {
		// TODO Auto-generated method stub
		return sysUserMapper.all();
	}
	
	@Override
	public SysUser getUserById(int tid) {
		// TODO Auto-generated method stub
		return sysUserMapper.getUserById(tid);
	}
	@Override
	public int update(SysUser user) {
		// TODO Auto-generated method stub
		return sysUserMapper.update(user);
	}
	@Override
	public int delete(int tid) {
		// TODO Auto-generated method stub
		return sysUserMapper.delete(tid);
	}
	
	@Autowired
	private SysUserMapper sysUserMapper;
}