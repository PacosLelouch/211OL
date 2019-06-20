<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%--@ page import="java_code.IpAddrGetter" --%>
<%@ page import="javaCode.UrlRepository" %>
<%
	//String ipAddr;
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits logout.");*/
	if(request.getSession().getAttribute("isLogin") != null){
		request.getSession().removeAttribute("isLogin");
	}
	if(request.getSession().getAttribute("username") != null){
		request.getSession().removeAttribute("username");
	}
	System.out.println("Check logout:" + (String)request.getSession().getAttribute("isLogin"));
	response.sendRedirect(UrlRepository.getHome());
%><!DOCTYPE HTML>
<html>
<head>
	<link rel="shortcut icon" href="<%=UrlRepository.getImagesUrl("favicon.ico") %>" />
    <link rel="stylesheet" type="text/css" href="<%=UrlRepository.getStylesUrl("style.css") %>" />
	<title>211OL: logout</title>
	<style>
	</style>
</head>
<body>
</body>
</html>
