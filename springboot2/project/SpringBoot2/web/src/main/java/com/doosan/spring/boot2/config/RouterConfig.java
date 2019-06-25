package com.doosan.spring.boot2.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.doosan.spring.boot2.handler.UserHandler;
@EnableWebFlux
@Configuration
public class RouterConfig {
	
	@Bean
	public RouterFunction<ServerResponse> routers(UserHandler handler){
		return RouterFunctions.route(RequestPredicates.GET("/system/user/findall"), handler::findAll)
				.andRoute(RequestPredicates.GET("/system/user/get/{id}"), handler::get)
				.andRoute(RequestPredicates.GET("/system/user/delete/{id}"), handler::delete)
				.andRoute(RequestPredicates.GET("/system/user/add/{name}/{age}/{remark}"), handler::save);
	}
}
