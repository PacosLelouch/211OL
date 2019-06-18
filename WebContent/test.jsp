<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="javaCode.DBController,javaCode.Response,java.util.*,javaCode.MD5Encode"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>Hello World.</p>
	<%
		DBController test = new DBController();
			/* Response res = test.add_new_user("111", "testing", "fefweg"); */
			/* Response res = test.show_user_info("111"); */
			Response res = test.check_login("111", "testing");
	%>
	<%= res.getMsg() %>
	<%= res.getData() %>
	
</body>
</html>