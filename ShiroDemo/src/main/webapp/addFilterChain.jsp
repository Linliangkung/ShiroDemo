<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加FilterChain</title>
</head>
<body>
	<h1>添加FilterChain</h1><br>
	<form action="${pageContext.request.contextPath}/filterchain/add" method="post">
		url:<input name="chainName" type="text"/><br/>
		拦截器:<input name="chainDefinition" type="text"/><br/>
		<input type="submit" value="添加"/>
	</form>
</body>
</html>