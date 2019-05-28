package com.doosan.spring.beans;
import org.springframework.stereotype.Repository;
@Repository
public class UserDao {

	public void execute(){
		System.out.println("Do the execution of DAO");
	}
}
