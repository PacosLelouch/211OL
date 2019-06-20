<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.DBController" %>
<%!
	String home = "index.jsp";
	String ipAddr;
	String username;
	Boolean isLogin;
%>
<jsp:useBean id="db" class="javaCode.DBController" scope="application" />
<%
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits checkLogin.");*/
	isLogin = (Boolean)session.getAttribute("isLogin");
	if(isLogin != null){
		username = (String)session.getAttribute("username");
		db.addLoginRecord(username);
		response.sendRedirect(home);
	} else {
		response.sendError(403, "Invalid visit. Need to login.");
	}
%><!DOCTYPE HTML>
<html>
<head>
	<link rel="shortcut icon" href="../img/favicon.ico" />
    <link rel="stylesheet" type="text/css" href="../styles/style.css" />
	<title>211OL: checkLogin</title>
	<style>
	</style>
</head>
<body>
</body>
</html>
