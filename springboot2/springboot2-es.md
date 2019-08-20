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
		
	2.10. 多索引和多类别查询	
	
		参考地址：https://es.xiaoleilu.com/
		
3. DSL语言高级查询

	ES提供了强大的查询语句(DSL),它可以允许我们进行更加强大、复杂的查询。
	ElasticSearch DSL中有Query和Filter两种
	
	3.1. Query方式查询：该查询方式会在ES中索引的数据都会存储一个_score分值，分值越高就代表越匹配。但是分值计算比较复杂，所以查询相对比较耗时
	
		3.1.1. 根据名称精确查询term， term查询不会对字段进行分词查询，会采用精确匹配（注意：采用term精确查询，查询字段映射类型必须为keyword）
		
		举例：
		
			post /doosan_db/user/_search
			{
				"query":{
					"term":{
						"name":"Jack"
					}
				}
			}
		调整表结构：
		
			post /doosan_db/_mapping/user
			{
			  "user":{
				"properties":{
				  "name":{"type":"keyword"},
				  "sex":{"type":"integer"},
				  "age":{"type":"integer"},
				  "book":{"type":"text"},
				  "remark":{"type":"text"},
				  "test":{"type":"keyword"}
				}
			  }
			}
			
		3.1.2. 根据备注信息进行模糊查询match, match会根据该字段的分词器，进行分词查询
		
		举例：
		
			post /doosan_db/user/_search
			{
				"from":0,
				"size":2,
				"query":{
					"match":{
						"book":"Spring"
					}
				}
			}
			
		3.1.3. 多字段模糊匹配查询与精准查询 multi_match
		
		举例：
		
			post /doosan_db/user/_search
			{
				"query":{
					"multi_match":{
						"query":"Spring Hadoop",
						"fields":["book","remark"]
					}
				}
			}
		
		3.1.4. 未指定字段条件查询query_string, 含AND与OR条件
		
		举例：
		
			post /doosan_db/user/_search
			{
				"query":{
					"query_string":{
						"query":"(Spring Cloud AND 入门到精通) OR Spring Boot"
					}
				}
			}
		
		3.1.5. 指定字段条件查询query_string,含AND与OR条件

		举例：
		
			post /doosan_db/user/_search
			{
				"query":{
					"query_string":{
						"query":"Spring or boot",
						"fields":["boot","remark"]
					}
				}
			}
		
		3.1.6. 范围查询
		
		举例：
		
			post /doosan_db/user/_search
			{
				"query":{
					"range":{
						"age":{
							"gte":23,
							"lte":25
						}
					}
				}
			}
			
		3.1.7. 分页、输出字段、排序综合查询
		
		举例：
		
			post /doosan_db/user/_search
			{
				"query":{
					"range":{
						"age":{
							"gte":23,
							"lte":25
						}
					}
				},
				"from":0,
				"size":2,
				"_source":["age","name","book"],
				"sort":{"age":"desc"}
			}
	3.2. Filter过滤器方式查询，它的查询不会计算相关性分值，也不会对结果进行排序，因此效率会高一些，查询的结果可以被缓存。
	
		3.2.1. Filter Context对数据进行过滤
		
		举例：
		
			post /doosan_db/user/_search
			{
				"query":{
					"bool":{
						"filter":{
							"term":{
								"age":25
							}
						}
					}
				}
			}

## ElasticSearch文档映射

- Mapping概述

	ElasticSearch的核心概念和关系型数据库对比，索引（index）相当于数据库，类型（type）相当于数据表，映射（Mapping）相当于数据表的表结构
	
- 映射可以分为动态映射和静态映射

	- 动态映射
	
		ElasticSearch不需要定义Mapping映射（即关系型数据库的表、字段等），在文档写入ElasticSearch时，会根据文档字段自动识别类型，这种机制成为动态映射。
		
		动态映射规则如下：
		
		| JSON数据 | 自动推测的类型 |
		| ------------- | ------------- | 
		| null | 没有字段被添加 |
		| true或false | boolean型 |
		| 小数 | float型 |
		| 数字 | long型 |
		| 日期 | date或text |
		| 字符串 | text |
		| 数组 | 由数组第一个非空值决定 |
		| JSON对象 | object类型 |
		
	- 静态映射
	
		在ElasticSearch中也可以事先定义好映射，包含文档的各字段类型，分词器等，这种方法称之为静态映射。
	
	
- ElasticSearch文档映射-支持的数据类型

	- 核心类型（Core datatypes）
	
		1. 字符串：String，String类型包含text和keyword
		
			1.1. text：该类型被用来索引长文本，在创建索引前会将这些文本进行分词，转化为词的组合，建立索引；允许es来检索这些词，text类型不能用来排序和聚合。
			
			1.2. keyword：该类型不能分词，可以被用来检索过滤、排序和聚合，keyword类型不可用text进行分词模糊检索
			
		2. 数值型：long/intege/short/byte/double/float
		
		3. 日期型：date
		
		4. 布尔型：boolean
		
		5. 二进制型：binary
		
	- 复杂数据类型（Complex datatypes）
	
	- 地理位置类型（Geo datatypes）
	
	- 特定类型（Specialised datatypes）
	
- ElasticSearch文档映射

	- 获取映射
	
		get /doosan_db/user/_mapping
		
	- 动态映射
	
		1. 创建索引后，直接写入类型数据
		
			put /db_index
			
			put /db_index/user
			{
				"name":"Harry",
				"age":25,
				"gender":0,
				"address":"SD YT",
				"remark":"AN IT MAN"
			}
	
	- 静态映射
	
		1. 预先定义映射
		
			post /db_index/_mapping/user
			{
			  "user":{
				"properties":{
				  "name":{"type":"keyword"},
				  "sex":{"type":"integer"},
				  "age":{"type":"integer"},
				  "book":{"type":"text"},
				  "remark":{"type":"text"},
				  "test":{"type":"keyword"}
				}
			  }
			}
		
		2. 插入数据
		
			put /db_index/user/1
			{
				"name":"Harry",
				"sex":1,
				"age":25,
				"book":"Spring Boot 1",
				"remark":"Hello world"
			}
	
	- 对已存在的mapping映射进行修改
	
		具体方法：
		
		1. 新建已静态索引
		
		2. 把之前索引里的数据导入到新的索引中
		
		3. 删除原来的索引
		
		4. 为新索引起别名，为原索引名
		
		post _reindex
		{
			"source":{
				"index":"db_index"
			},
			"dest":{
				"index":"db_index1"
			}
		}
		
		以上步骤实现了索引的平滑过渡，并且是零停机。
		
## ElasticSearch索引实现

- ElasticSearch基于倒排索引的实现

## ElasticSearch的IK中文分词器插件

- 下载地址：https://github.com/medcl/elasticsearch-analysis-ik/releases

	注意：es-ik的版本必须要和es安装版本一致
	
- Linux下安装
	
	1. elasticsearch-analysis-ik-6.8.2.zip下载完成后，重命名为ik.zip
	
	2. 上传到/opt/elasticsearch-6.8.2/plugins
	
	3. 重启ES
	
	4. 通过_analyze指令分词器演示ik_smart(ES默认分词器是standard)
	
		get _anylyze
		{
			"analyzer":"ik_smart",
			"text":"我爱祖国"
		}

- ElasticSearch的IK中文分词自定义词语

	1. 在/opt/elasticsearch-6.8.2/plugins/ik/config目录下创建一custom文件夹
	
	2. 在custom文件夹下新增my_word.dic文件，内容自己添加：如：流浪地球
	
	3. vi编辑config目录的IKAnalyzer.cfg.xml
	
		<entry key = "ext_dict">custom/my_word.dic</entry>
		
	4. 重启es
	
- 创建静态映射时指定text类型的ik分词器

	1. 设置ik分词器的文档映射
	
		1.1. 先删除之前的db_index
		
		1.2. 创建新的db_index
		
		1.3. 定义ik_smart的映射
		
			post /db_index/_mapping/user
			{
			  "user":{
				"properties":{
				  "name":{"type":"keyword"},
				  "sex":{"type":"integer"},
				  "age":{"type":"integer"},
				  "book":{
					"type":"text",
					"analyzer":"ik_smart",
					"search_analyzer":"ik_smart"
					},
				  "remark":{"type":"text"},
				  "test":{"type":"keyword"}
				}
			  }
			}
			
## SpringBoot集成ElasticSearch

- 引入ElasticSearch依赖pom.xml

```xml
	<!-- 集成ElasticSearch -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-elasticsearch</artifactId>
	</dependency>
```

- 配置application.properties elasticsearch连接文件

```
	#configure elasticsearch
	spring.data.elasticsearch.cluster-name=my-application
	spring.data.elasticsearch.cluster-nodes=10.40.123.215:9300(若有多个节点，用逗号分开即可)
```

- 编写实体类

```java
	package com.ch.sys.biz.system.es.entity;
	import org.springframework.data.annotation.Id;
	import org.springframework.data.elasticsearch.annotations.Document;
	@Document(indexName = "doosan_db", type = "book")
	public class Book {

		@Id
		private String id;	
		private String name;	
		private String message;	
		private String type ;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
```

- 编写service接口(继承于ElasticsearchRepository)

```java
	package com.ch.sys.biz.service.es;
	import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
	import com.ch.sys.biz.system.es.entity.Book;

	public interface BookService extends ElasticsearchRepository<Book, String> {

	}
```

- 编写Controller类

```groovy
	package com.ch.sys.biz.controller.system.es
	import org.elasticsearch.index.query.BoolQueryBuilder
	import org.elasticsearch.index.query.QueryBuilders
	import org.springframework.beans.factory.annotation.Autowired
	import org.springframework.data.domain.PageRequest
	import org.springframework.data.domain.jaxb.SpringDataJaxb.PageRequestDto
	import org.springframework.web.bind.annotation.GetMapping
	import org.springframework.web.bind.annotation.PathVariable
	import org.springframework.web.bind.annotation.PostMapping
	import org.springframework.web.bind.annotation.RequestMapping
	import org.springframework.web.bind.annotation.RestController
	import com.ch.sys.biz.service.es.BookService
	import com.ch.sys.biz.system.es.entity.Book
	import com.github.pagehelper.Page

	@RestController
	@RequestMapping("/es/book")
	class BookController {
		
		@Autowired
		BookService bookService
		
		@PostMapping("/add")
		Book add(Book book) {
			bookService.save(book)
			println "Book has been insert to the ES"
			return book
		}
		
		@PostMapping("/update")
		Book update(Book book) {
			bookService.save(book)
			println "Book has been insert to the ES"
			return book
		}
		
		@PostMapping("/delete/{id}")
		Book delete(@PathVariable String id) {
			Optional<Book> op = bookService.findOne(id)
			bookService.delete(op.get())
			return op.get()
		}
		
		@GetMapping("/search/{id}")
		Optional<Book> getById(@PathVariable String id){
			return bookService.findOne(id)
		}
		
		@GetMapping("/search/{page}/{size}/{q}")
		Page<Book> getByPages(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String q){
			BoolQueryBuilder builder = QueryBuilders.boolQuery()
			builder.must(QueryBuilders.matchQuery("message", q))
			Page<Book> pageList = bookService.search(builder, new PageRequest(page, size))
			return pageList
		}
		
	}
```

