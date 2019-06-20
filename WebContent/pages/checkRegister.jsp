<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.HtmlEncode" %>
<%@ page import="javaCode.UrlRepository" %>
<%
	//String ipAddr = "";
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits check_login.");*/
	String name = (String)request.getSession().getAttribute("regUsername");
	String email = (String)request.getSession().getAttribute("regEmail");
	if(name != null && email != null){
		request.getSession().removeAttribute("regUsername");
		request.getSession().removeAttribute("regEmail");
	} else {
		response.sendError(403, "Invalid visit. Need to register.");
	}
%>
<!DOCTYPE html>
<meta charset="UTF-8">
<head>
	<link rel="shortcut icon" href="<%=UrlRepository.getImagesUrl("favicon.ico") %>" />
    <link rel="stylesheet" type="text/css" href="<%=UrlRepository.getStylesUrl("style.css") %>" />
	<title>211OL: checkRegister</title>
</head>
<body>
    <p>
        Successfully registered with
        <br />Name: <%=HtmlEncode.encode(name) %>
        <br />Email: <%=HtmlEncode.encode(email) %>
        <br />You can't change your password. Please remind it in your heart.
    </p>
    <a href="<%=UrlRepository.getHome() %>"><input type="button" class="btn" value="Return" /></a>
</body>
