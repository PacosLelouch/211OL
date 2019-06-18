<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java_code.db_user_info, java_code.Response, java.util.*, java_code.md5Encode"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>Hello World.</p>
	<%
		db_user_info test = new db_user_info();
		/* Response res = test.add_new_user("111", "testing", "fefweg"); */
		/* Response res = test.show_user_info("111"); */
		Response res = test.check_login("111", "test1ing");
	%>
	<%= res.getMsg() %>
	<%= res.getData() %>
	
</body>
</html>