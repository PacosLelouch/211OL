<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.HtmlEncode" %>
<%!
	String home = "index.jsp";
	String ipAddr;
	String name;
	String email;
%>
<%
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits check_login.");*/
	name = (String)session.getAttribute("regUsername");
	email = (String)session.getAttribute("regEmail");
	if(name != null && email != null){
		session.removeAttribute("regUsername");
		session.removeAttribute("regEmail");
	} else {
		response.sendError(403, "Invalid visit. Need to register.");
	}
%>
<!DOCTYPE html>
<meta charset="UTF-8">
<head>
	<link rel="shortcut icon" href="../img/favicon.ico" />
    <link rel="stylesheet" type="text/css" href="../styles/style.css" />
	<title>211OL: checkRegister</title>
</head>
<body>
    <p>
        Successfully registered with
        <br />Name: <%=HtmlEncode.encode(name) %>
        <br />Email: <%=HtmlEncode.encode(email) %>
        <br />You can't change your password. Please remind it in your heart.
    </p>
    <a href="<%=home %>"><input type="button" class="btn" value="Return" /></a>
</body>
