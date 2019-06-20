<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.DBController" %>
<%@ page import="javaCode.UrlRepository" %>
<jsp:useBean id="db" class="javaCode.DBController" scope="application" />
<%
	//String ipAddr = "";
	String username = "";
	Boolean isLogin = (Boolean)request.getSession().getAttribute("isLogin");
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits checkLogin.");*/
	if(isLogin != null){
		username = (String)request.getSession().getAttribute("username");
		db.addLoginRecord(username);
		response.sendRedirect(UrlRepository.getHome());
	} else {
		response.sendError(403, "Invalid visit. Need to login.");
	}
%><!DOCTYPE HTML>
<html>
<head>
	<link rel="shortcut icon" href="<%=UrlRepository.getImagesUrl("favicon.ico") %>" />
    <link rel="stylesheet" type="text/css" href="<%=UrlRepository.getStylesUrl("style.css") %>" />
	<title>211OL: checkLogin</title>
	<style>
	</style>
</head>
<body>
</body>
</html>
