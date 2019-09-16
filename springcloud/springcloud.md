# SpringCloud

## Docker启动MySQL

- Linux下安装Docker

- 在Docker下运行MySQL服务

```
	docker run -di --name=mysql =p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 centos/mysql-57-centos7
```