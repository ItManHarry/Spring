package com.doosan.spring.boot2.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Repository;
import com.doosan.spring.boot2.dao.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public class UserDao {

	//模拟数据库
	private final ConcurrentMap<Integer, User> db = new ConcurrentHashMap<>();
	//ID生成器
	private final static AtomicInteger id = new AtomicInteger();
	
	public UserDao(){
		db.put(1, new User(1, "Harry", 25, "Remark1", "123456", new Date()));
		db.put(2, new User(2, "Jack", 25, "Remark2", "123456", new Date()));
		db.put(3, new User(3, "Tom", 25, "Remark3", "123456", new Date()));
		db.put(4, new User(4, "Alex", 25, "Remark4", "123456", new Date()));
	}
	//保存
	public boolean save(User user){
		Integer uid = id.incrementAndGet();
		user.setId(uid);
		return db.putIfAbsent(uid, user) == null;
	}
	//保存
	public Mono<Void> add(User user){
		Integer uid = id.incrementAndGet();
		user.setId(uid);
		db.putIfAbsent(uid, user);
		return  Mono.empty();
	}
	/**
	 * 返回0或1个元素 - webflux返回对象
	 * @param uid
	 * @return
	 */
	public Mono<User> getUserById(Integer uid){
		if(db.get(uid) == null)
			return Mono.empty();
		else 
			return Mono.just(db.get(uid));
	}
	/**
	 * web框架返回对象
	 * @param uid
	 * @return
	 */
	public User getUserById2(Integer uid){
		return db.get(uid);
	}
	/**
	 * 返回0或n个元素
	 * @return
	 */
	public Flux<User> findAll(){
		return Flux.fromIterable(db.values());
	}
	/**
	 * web框架返回所有数据
	 * @return
	 */
	public List<User> getAll(){
		List<User> users = new ArrayList<>();
		Iterator<User> its = db.values().iterator();
		while(its.hasNext()){
			users.add(its.next());
		}
		return users;
	}
	/**
	 * 执行删除
	 * @param uid
	 * @return
	 */
	public Mono<Void> delete(Integer uid){
		db.remove(uid);
		return Mono.empty();
	}
}
