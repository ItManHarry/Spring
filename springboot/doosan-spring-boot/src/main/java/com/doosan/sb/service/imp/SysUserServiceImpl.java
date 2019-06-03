package com.doosan.sb.service.imp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doosan.sb.dao.SysUserMapper;
import com.doosan.sb.dao.domain.SysUser;
import com.doosan.sb.service.SysUserService;
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

	@Override
	public void save(SysUser user) {
		sysUserMapper.save(user);
	}
	@Autowired
	private SysUserMapper sysUserMapper;
}