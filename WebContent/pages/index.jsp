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
	String username, password;
	int level;
	boolean valid = true;
	boolean isLogin = false;
	ArrayList<Integer> levelList = new ArrayList<Integer>();
%>
<jsp:useBean id="db" class="javaCode.DBController" scope="application" />
<%
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits index.");*/
	System.out.println("Username:" + (String)session.getAttribute("username"));
	System.out.println("isLogin:" + (Boolean)session.getAttribute("isLogin"));
	if(session.getAttribute("isLogin") != null){
		isLogin = true;
		username = (String)session.getAttribute("username");
		if(username == null){
			response.sendError(403, "Invalid user.");
		}
		levelList = (ArrayList<Integer>)db.showLevel(username).getData();
		valid = true;
	} else{
		isLogin = false;
		if(request.getMethod().equalsIgnoreCase("post")){
			username = request.getParameter("username");
			password = request.getParameter("password");
			System.out.println("Index post username:" + username);
			System.out.println("Index post password:" + password);
			if(username != null && username.length() >= 5 && username.length() <= 20 &&
					password != null && password.length() >= 5 && password.length() <= 30){
				Response checkResult = db.checkLogin(username, password);
				if(checkResult.getStatus()){
					session.setAttribute("username", username);
					session.setAttribute("isLogin", true);
					response.sendRedirect("checkLogin.jsp");
				} else{
					valid = false;
				}
			} else{
				valid = false;
			}
		}
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
    <h1>211OL</h1>
	<% if(isLogin == true){ %>
    <h2>Welcome, <%=HtmlEncode.encode(username) %>. </h2>
    <h2><a href="personal.jsp">Check your personal information.</a></h2>
    <a href="logout.jsp"><input type="button" class="btn" value="Logout" /></a>
    <form action="playing.jsp" method="get">
        <p>
            <div class="descriptioncenter">Map Size:</div>
            <div class="selectlayout">
            <select id="rank" name="size" list="ranklist" class="select" value="3">
                <% for(Integer level: levelList){ %>
                <option value="<%=level %>"><%=level %></option>
                <% }//end for %>
            </select>
            </div>
            <!--input type="number" class="textbox" name="size" value="4" /-->
        </p>
        <input type="submit" class="btn" value="Play" />
    </form>
    <% } else{ %>
    <h2>Login</h2>
    <form action="" method="post">
        <!--{% csrf_token %}%-->
        <p>
            <input type="text" class="textbox" name="username" placeholder="username" <%=username == null ? "" : "value=\"" + HtmlEncode.encode(username) + "\"" %>/>
        </p>
        <p>
            <input type="password" class="textbox" name="password" placeholder="password" />
        </p>

        <!--
            <input type="checkbox" name="box" value="1" /> DEBUG: 10s overtime
        -->
        <p>
            <input type="submit" class="btn" value="Login" />
        </p>
        <!-- <input type="button" id="btu-1" value="Ajax-1" /> -->

    </form>
	<% }//end if %>
    <p>
    <a href="register.jsp"><input type="button" class="btn" value="Register" /></a>
    </p>
    <p>
    <a href="messageBoard.jsp"><input type="button" class="btn" value="Board" /></a>
    </p>
    <% if(valid == false){ %>
    Invalid user or password.
    <% }//end if %>
</body>

