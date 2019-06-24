package com.doosan.spring.boot2.handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.doosan.spring.boot2.dao.UserDao;
import com.doosan.spring.boot2.dao.entity.User;
import reactor.core.publisher.Mono;
@Component
public class UserHandler {
	@Autowired
	private UserDao userDao;
	/**
	 * 查询所有的数据
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> findAll(ServerRequest request){
		return ServerResponse.ok().body(userDao.findAll(), User.class);
	}
	/**
	 * 根据ID查询
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> get(ServerRequest request){
		return userDao.getUserById(Integer.valueOf(request.pathVariable("id")))
				.flatMap(user -> ServerResponse.ok().syncBody(user))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	/**
	 * 执行删除
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> delete(ServerRequest request){
		return ServerResponse.noContent().build(userDao.delete(Integer.valueOf(request.pathVariable("id"))));
	}
}
