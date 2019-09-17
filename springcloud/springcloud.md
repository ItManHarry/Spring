# 微服务快速入门

## Docker启动MySQL

- Linux下安装Docker

- 在Docker下运行MySQL服务

```
	docker run -di --name=mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 centos/mysql-57-centos7
```

## RPC和HTTP远程调用方式对比

- RPC ： Remote Produce Call远程过程调用。自定义数据格式，基于原生的TCP通讯，速度快，效率高。早期的webservice及dubbo就是RPC典型应用

- HTTP : HTTP其实是一种网络传输协议，基于TCP，规定了数据传输的格式。现在客户端浏览器和服务端通讯基本都是采用HTTP协议。也可以用以远程服务调用，确定就是消息封装臃肿。

- 目前热门的Rest风格，就可以通过http协议来实现。

	SpringCloud基于http协议实现。
	
## RestTemplate实现远程调用

- Spring提供了一个RestTemplate模板工具类，对基于http的客户端进行了封装，并且实现了对象与json的序列化和反序列化，非常方便


- 初始化RestTemplate

```java
	@bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
```

- 使用RestTemplate远程调用微服务

```java
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value= "/order", method = RequestMethod.GET)
	public User Order(){
		User user = restTemplate.getForObject("http://localhost:9001/user/1", User.class);
		return user;
	}
```

# SpringCloud