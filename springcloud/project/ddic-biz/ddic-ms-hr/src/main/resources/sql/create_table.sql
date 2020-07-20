--组织表
create table tb_company(
	id int primary key auto_increment,
	name varchar(200),
	code varchar(20),	
	createuserid varchar(50),
	createtime date,
	modifyuserid varchar(50),
	modifytime date,
	rowver int,
	delflag char
);
--部门信息表
create table tb_department(
	id int primary key auto_increment,
	companyid int,
	name varchar(20),
	code varchar(20),	
	parentid int,
	createuserid varchar(50),
	createtime date,
	modifyuserid varchar(50),
	modifytime date,
	rowver int,
	delflag char
);
--雇员表
create table tb_employee(
	id int primary key auto_increment,
	name varchar(20),
	code varchar(20),	
	gender int,		
	email varchar(100),	
	mobile varchar(20),	
	address varchar(200),	
	job varchar(50),
	departmentid int,
	createuserid varchar(50),
	createtime date,
	modifyuserid varchar(50),
	modifytime date,
	rowver int,
	delflag char
);
--用户表
create table tb_user(
	id int primary key auto_increment,
	name varchar(20),
	code varchar(20),	
	pwd varchar(30),	
	employeeid int,
	createuserid varchar(50),
	createtime date,
	modifyuserid varchar(50),
	modifytime date,
	rowver int,
	delflag char
);