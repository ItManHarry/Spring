<html>
	<title>Freemarker to list the Users</title>
	<meta charset = "utf-8">
	<body>
		<h3>Users List</h3>
		<table>
			<tr>
				<th>Name</th>
				<th>Age</th>
			</tr>
			<#list list as users>
				<tr>
					<td>${users.name}</td>
					<td>${users.age}</td>
				</tr>
			</#list>
		</table>
	</body>
</html>