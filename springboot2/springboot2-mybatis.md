# Spring Boot2

## SpringBoot集成mybatis

- pom.xml引入lombok框架(自动生成POJO的getter/setter方法)&pagehelper框架(分页工具)

```xml
	<!-- 引入pagehelper 依赖 -->
	<dependency>
		<groupId>com.github.pagehelper</groupId>
		<artifactId>pagehelper-spring-boot-starter</artifactId>
		<version>1.2.3</version>
	</dependency>	
	<!-- lombok -->
	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<optional>true</optional>
	</dependency>
```

- 配置mybatis&pagehelper(application.properties)

```
	#指定mybatis类扫描包路径
	mybatis.type-aliases-package=com.ch.dev.domain.mybatis
	#指定mybatis sql xml文件路径(resources下sql文件夹)
	mybatis.mapper-locations=sql/**/*.xml
	#pagehelper配置
	pagehelper.helper-dialect=mysql
	pagehelper.reasonable=true
	pagehelper.support-methods-arguments=true
	pagehelper.params=count=countSql
```

- 编写实体基类

```java
	package com.ch.dev.dao.entity;
	import java.io.Serializable;
	import java.util.Date;
	import org.springframework.format.annotation.DateTimeFormat;
	import com.fasterxml.jackson.annotation.JsonFormat;
	import com.fasterxml.jackson.annotation.JsonIgnore;
	/**
	 * Mybatis基类
	 * @author 20112004
	 *
	 */
	public abstract class BaseEntity implements Serializable {

		private static final long serialVersionUID = -7279174283256064832L;
		/**
		 * 删除标记 (0 : 正常  1 : 删除  2 : 审核)
		 */
		public static final String DEL_FLAG_NOMAL 	= "0";
		public static final String DEL_FLAG_DELETE 	= "1";
		public static final String DEL_FLAG_AUDIT 	= "2";
		/**
		 * 是否是新纪录(默认false),调用setIsNewRecord()设置新纪录,使用自定义ID	 
		 * 设置为true后强制执行插入语句,ID不会自动生成,需手动导入
		*/
		@JsonIgnore
		protected boolean isNewRecord = false;
		
		public boolean getIsNewRecord() {
			return isNewRecord;
		}
		public void setNewRecord(boolean isNewRecord) {
			this.isNewRecord = isNewRecord;
		}
		
		public BaseEntity(){
			super();
			this.delflag = DEL_FLAG_NOMAL;
		}
		
		public BaseEntity(String id){
			this();
			this.id = id;
		}
		/**
		 * 插入前执行的方法,需要手动调用
		 */
		public void prepareInsert(){
			this.id = null;
			this.createtime = new Date();
			this.rowver = 1;
		}
		/**
		 * 更新前执行的方法,需要手动调用
		 */
		public void prepareUpdate(){
			this.modifytime = new Date();
			this.rowver ++;
		}
		/*唯一标识*/
		protected String id;
		/*创建者*/
		protected String createuserid;
		/*创建时间*/
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
		@DateTimeFormat(pattern="yyyy-MM-dd")
		protected Date createtime;
		/*最后修改人*/
		protected String modifyuserid;
		/*修改时间*/
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
		@DateTimeFormat(pattern="yyyy-MM-dd")
		protected Date modifytime;
		/*行版本号*/
		protected int rowver;
		/*删除标记(0 : 正常  1 : 删除  2 : 审核)*/
		@JsonIgnore
		protected String delflag;

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCreateuserid() {
			return createuserid;
		}
		public void setCreateuserid(String createuserid) {
			this.createuserid = createuserid;
		}
		public Date getCreatetime() {
			return createtime;
		}
		public void setCreatetime(Date createtime) {
			this.createtime = createtime;
		}
		public String getModifyuserid() {
			return modifyuserid;
		}
		public void setModifyuserid(String modifyuserid) {
			this.modifyuserid = modifyuserid;
		}
		public Date getModifytime() {
			return modifytime;
		}
		public void setModifytime(Date modifytime) {
			this.modifytime = modifytime;
		}
		public int getRowver() {
			return rowver;
		}
		public void setRowver(int rowver) {
			this.rowver = rowver;
		}
		public String getDelflag() {
			return delflag;
		}
		public void setDelflag(String delflag) {
			this.delflag = delflag;
		}
	}
```
- 编写mapper基类

```java
	package com.ch.dev.dao.mybatis.base;
	import org.apache.ibatis.annotations.Param;
	import com.ch.dev.dao.entity.BaseEntity;
	import com.github.pagehelper.Page;
	/**
	 * 基本Dao层
	 * @author 20112004
	 *
	 */
	public interface BaseMapper<T extends BaseEntity> {

		/**
		 * 查找全部
		 * @param entity
		 * @return
		 */
		Page<T> findAll(T entity);
		/**
		 * 根据ID查找一条数据
		 * @param id
		 * @return
		 */
		T findById(String id);
		/**
		 * 新增数据
		 * @param entity
		 * @return
		 */
		int insert(T entity);
		/**
		 * 更新数据
		 * @param entity
		 * @return
		 */
		int update(T entity);
		/**
		 * 批量删除数据
		 * @param ids
		 * @return
		 */
		int delete(@Param(value="ids") String[] ids);
		/**
		 * 查询总记录数据
		 * @param entity
		 * @return
		 */
		int recordCnt(T entity);
	}
```

- 编写service基类

```java
	package com.ch.dev.service.mybatis.base;
	import java.util.List;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.dao.DuplicateKeyException;
	import org.springframework.transaction.annotation.Propagation;
	import org.springframework.transaction.annotation.Transactional;
	import com.ch.dev.dao.entity.BaseEntity;
	import com.ch.dev.dao.mybatis.base.BaseMapper;
	import com.ch.dev.system.exception.WebServerException;
	import com.ch.dev.system.results.WebServerResults;
	import com.github.pagehelper.Page;
	import com.github.pagehelper.PageHelper;
	/**
	 * 数据操作Service
	 * @author 20112004
	 *
	 * @param <T>
	 * @param <M>
	 */
	public abstract class BaseService<T extends BaseEntity, M extends BaseMapper<T>> {
		private Logger logger = LoggerFactory.getLogger(BaseService.class);
		@Autowired
		protected M dao;
		/**
		 * 查找全部
		 * @param entity
		 * @return
		 */
		@Transactional(propagation=Propagation.SUPPORTS)
		public List<T> findAll(T entity){
			try{
				return dao.findAll(entity);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
		}
		/**
		 * 根据条件进行查找 - 含分页功能
		 * @param entity
		 * @param order
		 * @param page
		 * @param pageSize
		 * @return
		 */
		@Transactional(propagation=Propagation.SUPPORTS)
		public List<T> findAll(T entity, String order, Integer page, Integer pageSize){
			try{
				if(page == null)
					page = 1;
				if(pageSize == null)
					pageSize = 10;
				Page<T> pageHelper = PageHelper.startPage(page, pageSize, order);
				return dao.findAll(entity);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
		}
		/**
		 * 查询记录条数
		 * @param entity
		 * @return
		 */
		@Transactional(propagation=Propagation.SUPPORTS)
		public int recordCnt(T entity){
			try{
				return dao.recordCnt(entity);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
		}
		/**
		 * 查找单条数据
		 * @param id
		 * @return
		 */
		@Transactional(propagation=Propagation.SUPPORTS)
		public T findById(String id){
			try{
				return dao.findById(id);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
		}
		/**
		 * 保存/修改 数据
		 * @param entity
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public T save(T entity){
			try{
				int i = 0;
				if(entity.getIsNewRecord()){
					entity.prepareInsert();
					i = dao.insert(entity);
				}else{
					entity.prepareUpdate();
					i = dao.update(entity);
				}
				if(i == -1)
					throw new WebServerException(WebServerResults.ERROR);
			}catch(DuplicateKeyException e){
				logger.error(e.getCause().getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR_KEY);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
			return entity;
		}
		/**
		 * 删除数据
		 * @param ids
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public int delete(String[] ids){
			try{
				return dao.delete(ids);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
		}
	}
```

- 编写业务实体类(继承于BaseEntity)

```java
	package com.ch.dev.domain.mybatis;
	import com.ch.dev.dao.entity.BaseEntity;
	import lombok.Data;
	import lombok.EqualsAndHashCode;
	import lombok.ToString;
	@Data
	@ToString(callSuper=true)
	@EqualsAndHashCode(callSuper=true)
	public class Tb_Employee extends BaseEntity {

		private static final long serialVersionUID = 5375541256532181851L;
		private String name;
		private String job;	
	}
```

- 编写mapper业务类(继承BaseMapper),使用mybatis的@Mapper注解注入spring容器,自定义方法进行条件查询

```java
	package com.ch.dev.dao.mybatis.biz;
	import org.apache.ibatis.annotations.Mapper;
	import com.ch.dev.dao.mybatis.base.BaseMapper;
	import com.ch.dev.domain.mybatis.Tb_Employee;
	import com.github.pagehelper.Page;
	@Mapper
	public interface EmployeeDao extends BaseMapper<Tb_Employee> {
		/**
		 * 根据name获取数据条数
		 * @param name
		 * @return
		 */
		int recordCntByTerm(String name);
		/**
		 * 根据name查找全部
		 * @param entity
		 * @return
		 */
		Page<Tb_Employee> findAllByTerm(String name);
	}
```

- 编写service业务类(继承BaseService)，@Service注解注入,实现自定义查询方法

```java
	package com.ch.dev.service.mybatis.biz;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Propagation;
	import org.springframework.transaction.annotation.Transactional;
	import com.ch.dev.dao.mybatis.biz.EmployeeDao;
	import com.ch.dev.domain.mybatis.Tb_Employee;
	import com.ch.dev.service.mybatis.base.BaseService;
	import com.ch.dev.system.exception.WebServerException;
	import com.ch.dev.system.results.WebServerResults;
	import com.github.pagehelper.Page;
	import com.github.pagehelper.PageHelper;
	@Service
	public class EmployeeService extends BaseService<Tb_Employee, EmployeeDao> {
		
		private Logger logger = LoggerFactory.getLogger(EmployeeService.class);
		@Autowired
		private EmployeeDao employeeDao;
		@Transactional(propagation=Propagation.SUPPORTS)
		public int recordCntByTerm(String name){
			try{
				return employeeDao.recordCntByTerm(name);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
		}
		@Transactional(propagation=Propagation.SUPPORTS)
		public Page<Tb_Employee> findAllByTerm(String order, Integer page, Integer pageSize, String name){
			try{
				if(page == null)
					page = 1;
				if(pageSize == null)
					pageSize = 10;
				Page<Tb_Employee> pageHelper = PageHelper.startPage(page, pageSize, order);
				return employeeDao.findAllByTerm(name);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				throw new WebServerException(WebServerResults.ERROR);
			}
		}
	}
```

- 编写Controller层(使用Groovy编写)

```groovy
	package com.ch.dev.controller.biz.employee
	import org.springframework.beans.factory.annotation.Autowired
	import org.springframework.stereotype.Controller
	import org.springframework.web.bind.annotation.GetMapping
	import org.springframework.web.bind.annotation.PathVariable
	import org.springframework.web.bind.annotation.PostMapping
	import org.springframework.web.bind.annotation.RequestMapping
	import org.springframework.web.bind.annotation.ResponseBody
	import com.ch.dev.domain.mybatis.Tb_Employee
	import com.ch.dev.service.mybatis.biz.EmployeeService
	import com.ch.dev.system.results.WebServerResultJson
	import com.github.pagehelper.Page
	@Controller
	@RequestMapping("/biz/employee")
	class EmployeeController {

		final String URL = "/biz/employee"
		@Autowired
		EmployeeService employeeService
		/**
		 * 跳转至清单画面
		 * @return
		 */
		@GetMapping("/list")
		def list(Map map){
			def total = employeeService.recordCntByTerm("");
			map.put("total", total)
			return URL + "/list"
		}
		/**
		 * 跳转至新增画面
		 * @return
		 */
		@GetMapping("/add")
		def add(){
			return URL + "/add"
		}
		/**
		 * 执行保存
		 * @param employee
		 * @return
		 */
		@PostMapping("/save")
		def save(String name, String job, Map map){
			Tb_Employee employee = new Tb_Employee()
			employee.setName(name)
			employee.setJob(job)
			employee.setCreateuserid("20112004")
			employee.setNewRecord(true)
			employeeService.save(employee)
			def total = employeeService.recordCntByTerm("");
			map.put("total", total)
			return URL + "/list"
		}
		/**
		 * 跳转至修改画面
		 * @param id
		 * @param map
		 * @return
		 */
		@GetMapping("/update")
		def update(String id, Map map){
			Tb_Employee employee = employeeService.findById(id)
			println employee.id + ", "  + employee.name + ", " + employee.job
			map.put("employee", employee)
			return URL + "/update"
		}
		/**
		 * 执行更新
		 * @param id
		 * @param name
		 * @param job
		 * @return
		 */
		@PostMapping("/modify")
		def modify(String id, String name, String job, Map map){
			Tb_Employee employee = employeeService.findById(id)
			employee.setId(id)
			employee.setName(name)
			employee.setJob(job)
			employee.setModifyuserid("20112004")
			employee.setNewRecord(false)
			employeeService.save(employee)
			def total = employeeService.recordCntByTerm("");
			map.put("total", total)
			return URL + "/list"
		}
		/**
		 * 逻辑删除
		 * @param id
		 * @return
		 */
		@GetMapping("/move")
		def move(String id, Map map){
			println "id is : $id"
			def ids = id.split(",")
			Integer i = employeeService.delete(ids)
			def total = employeeService.recordCntByTerm("");
			map.put("total", total)
			return URL + "/list"
		}
		/**
		 * 执行删除
		 * @param id
		 * @return
		 */
		@ResponseBody
		@GetMapping("/delete/{id}")
		def delete(@PathVariable("id") String id){
			def ids = id.split(",")
			Integer i = employeeService.delete(ids)
			return WebServerResultJson.success("Deleted $i records!")
		}
		/**
		 * 执行数据获取-全部数据分页
		 * @param employee
		 * @param order
		 * @param page
		 * @param limit
		 * @return
		 */
		@ResponseBody
		@GetMapping("/all")
		def all(String order, Integer page, Integer limit, String name){
			def countByTerm = employeeService.recordCntByTerm(name);
			Page<Tb_Employee> allByTerm = employeeService.findAllByTerm(order, page, limit, name)
			return WebServerResultJson.success(allByTerm, countByTerm)
		}		
	}
```

- 编写页面(Thymeleaf+Bootstrap+jQuery+Vue,分页使用bootstrap-paginator.js)

```html
	<!DOCTYPE html>
	<html lang="en">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
			<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
			<title>Employee List Page</title>
			<link rel="shortcut icon" type="image/x-icon" th:href = "@{/static/images/logoes/way.ico}" media="screen" />
			<link rel = "stylesheet" th:href = "@{/static/css/bootstrap.min.css}" media="screen"/>
			<script type = "text/javascript" th:src = "@{/static/js/bootstrap.min.js}" charset="UTF-8"></script>
			<script type = "text/javascript" th:src = "@{/static/js/jquery-1.11.3.min.js}" charset="UTF-8"></script>
			<script type = "text/javascript" th:src = "@{/static/js/bootstrap-table.min.js}" charset="UTF-8"></script>
			<script type = "text/javascript" th:src = "@{/static/js/locale/bootstrap-table-zh-CN.min.js}" charset="UTF-8"></script>
			<script type = "text/javascript" th:src = "@{/static/js/bootstrap-paginator.js}" charset="UTF-8"></script>
			<script type = "text/javascript" th:src = "@{/static/js/vue.js}" charset="UTF-8"></script>
		</head>
		<body>
			<div class="container-fluid" id = "contentdiv"> 
				<div class = "row">
					<div class = "col-sm-12">
						<h3>Employee List</h3>
					</div> 
				</div>
				<hr>
				<div class = "row">
					<div class = "col-sm-6 col-sm-offset-6 text-right">
						<div class="input-group">
							<input type = "text" placeholder = "name..." v-model = "name" class = "form-control input-sm"/>
							<span class="input-group-btn">
								<button class="btn btn-success btn-sm" type="button" @click = "doSearch">Search</button>
								&nbsp;&nbsp;<a th:href = "@{~/biz/employee/add}" class = "btn btn-primary btn-sm">&nbsp;&nbsp;Add&nbsp;&nbsp;</a>
							</span>
						</div>
					</div> 
				</div>
				<br>
				<div class = "row">
					<div class = "col-sm-12">
						<table class = "table table-bordered table-hover table-responsive" id = "list">
							<thead>
								<tr>
									<th width = "10%">#ID</th>
									<th width = "40%">Name</th>
									<th width = "40%">Job</th>
									<th width = "10%">Action</th>
								</tr>
							</thead>
							<tbody>
								<tr v-for = "data in datas">
									<td>{{data.id}}</td>
									<td>{{data.name}}</td>
									<td>{{data.job}}</td>
									<!-- vue拼接超链接方式 -->
									<td>
										<a :href = "'/biz/employee/update?id='+data.id" class = "btn btn-link btn-xs">Edit</a>&nbsp;&nbsp;
										<a :href = "'/biz/employee/move?id='+data.id" class = "btn btn-link btn-xs">Delete</a>
									</td>						
								</tr>
							</tbody>
						</table>
						<nav class="text-right">
							<ul class="pagination"></ul>
						</nav>					
					</div> 
				</div>
			</div>
			<script>
				var vm = new Vue({
					el:"#contentdiv",
					data:{
						datas:[],
						name:"",
						total:[[${total}]]	//获取后台记录总数
					},
					methods:{
						doSearch:function(){
							getData(1, this.name);
						}
					},
					created:function(){
						getData(1,this.name);					
					}
				});
				$(function(){
					$('.pagination').bootstrapPaginator({
						bootstrapMajorVersion: 3, 				//对应bootstrap版本
						size: 'small', 							//分页大小
						currentPage: 1, 						//当前页
						numberOfPages: 10, 						//显示的页数
						totalPages: Math.ceil(vm.total / 10), 	// 总页数
						/**
						 * 分页点击事件
						 * @param event [jquery对象]
						 * @param originalEvent [dom原生对象]
						 * @param type [按钮类型]
						 * @param page [点击按钮对应的页码]
						 */
						onPageClicked: function (event, originalEvent, type, page) {
							getData(page,vm.name);
						}
					})
				})
				/**
				*获取数据
				*/
				function getData(page,name){
					$.ajax({
						url:"/biz/employee/all",
						type:"get",
						data:{order:"name", page:page, limit:10, name:name},
						dataType:"json",
						success:function(r){
							if(r.status == 200){
								vm.total = r.number
								//重新设置总页数
								$('.pagination').bootstrapPaginator("setOptions", {
									currentPage: page, 						//当前页
									totalPages: Math.ceil(vm.total / 10), 	// 总页数
								})
								vm.datas = r.data
							}
						},
						error:function(r){
							alert(r.message)
						}
					})
				}
			</script>
		</body>
	</html>
```