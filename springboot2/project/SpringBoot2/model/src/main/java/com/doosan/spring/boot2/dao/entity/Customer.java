package com.doosan.spring.boot2.dao.entity;
import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
@Configuration
@ConfigurationProperties(prefix="com.doosan")
@PropertySource(value="classpath:customer.properties")
public class Customer implements Serializable{

	private static final long serialVersionUID = 7164220872305562512L;
	private String project;
	private String version;
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}