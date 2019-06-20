<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%@ page import="java.util.*" %>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.DBController" %>
<%@ page import="javaCode.Response" %>
<%@ page import="javaCode.HtmlEncode" %>
<%@ page import="javaCode.UrlRepository" %>
<%!
	String user, pwd, email;
	boolean validRegister;
	Response addResult;
%>
<jsp:useBean id="db" class="javaCode.DBController" scope="application" />
<%
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits register.");*/
	validRegister = true;
	if(request.getMethod().equalsIgnoreCase("post")){
		user = request.getParameter("username");
		pwd = request.getParameter("password");
		email = request.getParameter("email");
		if(user != null && user.length() >= 5 && user.length() <= 20 &&
				pwd != null && pwd.length() >= 5 && pwd.length() <= 30 &&
				email.length() <= 100){
			addResult = db.addNewUser(user, pwd, email);
			if((boolean)addResult.getStatus() == false){
				validRegister = false;
			} else{
				session.setAttribute("regUsername", user);
				session.setAttribute("regEmail", email);
				response.sendRedirect("checkRegister.jsp");
			}
		} else{
			validRegister = false;
		}
	}
%>
<!DOCTYPE html>
<head>
	<link rel="shortcut icon" href="<%=UrlRepository.getImagesUrl("favicon.ico") %>" />
    <link rel="stylesheet" type="text/css" href="<%=UrlRepository.getStylesUrl("style.css") %>" />
	<title>211OL: register</title>
	<style>
	</style>
</head>
<body>
    <h1>Register</h1>
    <form method="post">
        <%-- {% csrf_token %} --%>
        <p>
            <input type="text" class="textboxreg" name="username" placeholder="username(length from 5 to 20)" <%=user == null ? "" : "value=\"" + HtmlEncode.encode(user) + "\"" %> />
        </p>
        <p>
            <input type="password" class="textboxreg" name="password" placeholder="password(length from 5 to 30)" />
        </p>
        <p>
            <input type="email" class="textboxreg" name="email" placeholder="email(length not longer than 100)" <%=email == null ? "" : "value=\"" + HtmlEncode.encode(email) + "\"" %>/>
        </p>
        <p>
            <input type="submit" class="btn" value="Register" />
        </p>

    </form>
    <a href="<%=UrlRepository.getHome() %>"><input type="button" class="btn" value="Return" /></a>
    <% if(validRegister == false){ %>
    <%-- {% if valid_register == False %} --%>
    <p><strong>Invalid information. Please register again.</strong></p>
    <% } %>
    <%-- {% endif %} --%>
</body>
