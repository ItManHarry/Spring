package com.doosan.sb.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.doosan.sb.ApplicationStarter;
import com.doosan.sb.dao.domain.SysUser;
import com.doosan.sb.service.user.SysUserService;
@RunWith(SpringJUnit4ClassRunner.class)				//Junit和spring环境进行整合
@SpringBootTest(classes={ApplicationStarter.class})	//SpringBoot测试类，加载springboot启动类
public class UserDaoUnitTest {
	
	@Test
	public void testSave(){
		SysUser user = new SysUser();
		user.setBg(3);
		user.setRoleid(3);
		user.setStatus(3);
		user.setTeamid(3);
		user.setUsercd("test001");
		user.setUsernm("Junit001");
		sysUserService.save(user);
	}
	
	@Autowired
	private SysUserService sysUserService;
}