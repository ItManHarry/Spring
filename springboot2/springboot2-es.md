# Spring Boot2

## ElasticSearch安装

- Linux下安装

	1.环境要求
	
		1.1. JDK版本1.8以上
		
		1.2. ElasticSearch版本6.X
			
			1.2.1. ES非常占内存,默认启动是1G内存
			
			1.2.2. 设置ES开机启动
		
		1.3. 安装Kibana可视化界面平台

	2.下载elasticsearch-6.8.2.tar.gz & kibana-6.8.2-linux-x86_64.tar.gz
	
	3.上传服务器
	
	4.安装ElasticSearch
	
		4.1. 将文件elasticsearch-6.8.2.tar.gz放到/opt文件夹下
		
		4.2. 解压：tar -zxvf elasticsearch-6.8.2.tar.gz
		
		4.3. 创建ElasticSearch用户
		
			a. es规定root用户不能启动ElasticSearch，所以需要创建一个es用户来启动
			
			b.先创建组，在创建用户
			
				b.1. 创建用户组：
				
					-# groupadd elasticsearch
				
				b.2. 创建es用户并设置密码es123：	
					-# useradd es
					-# passwd es
				
				b.3. 用户es添加到elasticsearch用户组下

					-# usermod -G elasticsearch es
					-# chown -R es /opt/elasticsearch-6.8.2
					
				b.4. 设置es的root权限
				
					-# visudo
					
					在root ALL=(ALL) ALL 一行下新增：
					
					es ALL=(ALL) ALL
					
					修改完成:wq保存退出
					
				b.5. 切换至es用户 
				
					-# su es
	
	5.在es用户下修改ElasticSearch配置文件
	
		5.1. 进入/opt/elasticsearch-6.8.2/config目录
		
		5.2. 如果要修改JVM内存，编辑jvm.options文件即可

			-# vi jvm.options
			
			默认配置1g：
			
				-Xms1g
				-Xmx1g
		5.3. 配置elasticsearch.yml
		
			-# vi elasticsearch.yml
			
			a. 取消如下注释，并修改为当前主机地址（注：冒号前面不能有空格，后面必须有一个空格）
			
				cluster.name: my-application
				node.name: node-1
				bootstrap.memory_lock: false 
				注：ES官网建议生产环境下设置为true，但是设置为true后会报错，解决方法如下：
				1. 修改/etc/security/limits.config目录
					es soft memlock unlimited
					es hard memlock unlimited
				2. 修改/etc/sysctl.conf
					vm.swappiness=0
				3.修改完成后重启服务器
				network.host: 10.40.123.215
				http.port:9200
				path.data: /opt/elasticsearch-6.8.2/data
				path.logs: /opt/elasticsearch-6.8.2/logs
				修改完成保存退出 :wq
				
			b. 修改/etc/sysctl.conf文件，新增如下代码：
				vm.max_map_count=655360
				保存退出后执行如下命令：
				sysctl -p
			
			d. 编辑/etc/security/limits.conf文件，末尾添加如下代码：
				es soft nofile 65536
				es hard nofile 65536
				es soft nproc 4096
				es hard nproc 4096
				
		5.4 以上配置完成后，重启服务器reboot
		
	
	6. 启动ElasticSearch

		6.1. 重启服务器后，使用es用户登录
		
		6.2. 登录后到ElasticSearch的bin目录下
		
			-$ cd /opt/elasticsearch-6.8.2/bin
			-$ ./elasticsearch -d  (后台启动)
		
		6.3. 启动完后，浏览器进行访问：
		
			http://10.40.123.215:9200
			
			能够查看到json返回数据，证明ElasticSearch安装配置成功
	

	7. ElasticSearch设置开机启动
	
		7.1. elasticsearch目录创建run目录并设置权限（777）
		
			-$ cd /opt/elasticsearch-6.8.2
			-$ mkdir run
			-$ chmod 777 run
			
		7.2. 创建es服务系统配置文件
		
			该文件用于配置es服务的系统变量，用于systemd调用。里面配置了ES_HOME,ES_PATH_HOME,PID_DIR等，其中PID_DIR用于存放es进程的PID，用于systemd管理es进程的启动或停止
			在/etc/sysconfig目录下创建elasticsearch文件，内容如下：
		
		7.3. 创建es服务，在/usr/lib/systemd/system目录下创建elasticsearch.service文件，内容如下：
		
		7.4. 给脚本赋权限
		
			chmod +x /usr/lib/systemd/system/elasticsearch/service
			
		7.5. 重新加载systemd的守护进程
		
			systemctl daemon-reload
			
		7.6. 开机启动生效：
		
			systemctl enable elasticsearch.service
			
		7.7. 启动elasticsearch.service
			
			systemctl start elasticsearch.service
			
		7.8. 查看elasticsearch.service状态
		
			systemctl status elasticsearch.service
			
	8. 安装配置Kibana
	
		8.1. 在es用户的根目录下解压kibana-6.8.2-linux-x86_64.tar.gz
		
			tar -zxvf kibana-6.8.2-linux-x86_64.tar.gz
		
		8.2. 配置Kibana，修改配置kibana.yml
		
			-$ vi kibana.yml
			
			server.port: 5601
			server.host: "10.40.123.215"
			elasticsearch.hosts:["10.40.123.215"]
			
			修改完毕保存退出:wq
		
		8.3. 启动Kibana，/kibana-6.8.2/bin/kibana
		
		8.4. 启动成功后，网页访问：
		
			http://10.40.123.215:5601
			
## ElasticSearch查询语句

1. 基本操作

	1.1. 创建索引
	
		格式：put /索引名称(等同于关系型数据库)
		举例：put /db_index
		
	1.2. 查询索引
	
		格式： get /索引名称
		举例： get /db_indx
		
	1.3. 删除索引
	
		格式： delete /索引名称
		举例： delete /db_indx
		
	1.4. 添加文档
	
		格式： put /索引名称/类型(等同于关系型数据库里的表)/id(这条记录的ID)
		举例：
			put /doosan_db/user/1
			{
				"name":"Harry",
				"sex":1,
				"age":25,
				"book":"Spring Boot 1",
				"remark":"Hello world"
			}
			put /doosan_db/user/2
			{
				"name":"Harry2",
				"sex":1,
				"age":23,
				"book":"Spring Boot 1",
				"remark":"Hello world"
			}
			put /doosan_db/user/3
			{
				"name":"Harry",
				"sex":1,
				"age":28,
				"book":"Spring Boot 1",
				"remark":"Hello world"
			}
			put /doosan_db/user/4
			{
				"name":"Harry",
				"sex":0,
				"age":22,
				"book":"Spring Boot 4",
				"remark":"Hello world"
			}
			put /doosan_db/user/5
			{
				"name":"Harry",
				"sex":0,
				"age":25,
				"book":"Spring Boot 1",
				"remark":"Hello world"
			}
	1.5. 修改文档
	
		格式： put /索引名称/类型(等同于关系型数据库里的表)/id(这条记录的ID)
		举例：
			put /doosan_db/user/1
			{
				"name":"Harry222",
				"sex":1,
				"age":25,
				"book":"Spring Boot 1",
				"remark":"Hello world"
			}
			
	1.6. 查询文档
	
		格式：get /索引名称/类型/id
		举例：get /doosan_db/user/1
		
	1.7. 删除文档
	
		格式：delete /索引名称/类型/id
		举例：delete /doosan_db/user/1
	
2. 查询操作

	2.1. 查询当前类型中所有文档
		
		格式：get /索引名称/类型/_search
		举例：get /doosan_db/user/_search
		
	2.2. 条件查询，如查询age等于28的user
		
		格式：get /索引名称/类型/_search?q=*:**
		举例：get /doosan_db/user/_search?q=age:28
		等同于SQL：select * from user where age = 28
		
	2.3 范围查询，如查询年龄在23 ~ 28之间的user
	
		格式：get /索引名称/类型/_search?q=*[* TO *]
		举例：get /doosan_db/user/_search?q=age[23 TO 28]
		等同于SQL：select * from user where age between 23 and 28
		
	2.4. 批量查询_mget
	
		格式：get /索引名称/类型/_mget
		举例：get /doosan_db/user/_mget
			  {
				"ids":["1","2","3"]
			  }
		等同于SQL：select * from user where id in (1,2,3)
		
	2.5. 查询age小于等于25的user
		
		格式：get /索引名称/类型/_search?q=*:<=**
		举例：get /doosan_db/user/_search?q=age:<=25
		等同于SQL：select * from user where age <= 25
		
	2.6. 查询age大于25的user
		
		格式：get /索引名称/类型/_search?q=*:>**
		举例：get /doosan_db/user/_search?q=age:>25
		等同于SQL：select * from user where age > 25
		
	2.7. 分页查询from=*&size=*
	
		格式：get /索引名称/类型/_search?q=*:>**&from=*&size=*
		举例：get /doosan_db/user/_search?q=age:>25&from=1&size=2
		等同于SQL：select * from user where age > 25 limit 0, 1
		
	2.8. 查询具体字段 _source=column1, column2
	
		格式：get /索引名称/类型/_search?q=*:>**&from=*&size=*&_source=column1, column2, ...
		举例：get /doosan_db/user/_search?q=age:>25&from=1&size=2&_source=name,age,sex
		等同于SQL：select name, age , sex from user where age > 25 limit 0, 1
		
	2.9. 查询结果排序 sort=column1:desc/asc
		
		格式：get /索引名称/类型/_search?q=*:>**&from=*&size=*&_source=column1, column2, ...&sort=*:desc/asc
		举例：get /doosan_db/user/_search?q=age:>25&from=1&size=2&_source=name,age,sex&sort=age:desc
		等同于SQL：select name, age , sex from user where age > 25 limit 0, 1 order by age desc
		
		