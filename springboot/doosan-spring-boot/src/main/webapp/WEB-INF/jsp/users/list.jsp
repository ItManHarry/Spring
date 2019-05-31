<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>User List View</title>
</head>
<body>
	<h1>User list viewed in jsp page</h1>
	<table>
		<tr>
			<th>Name</th>
			<th>Age</th>
		</tr>
		<c:forEach items="${list}" var="user">
			<tr>
				<td>${user.name}</td>
				<td>${user.age}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>