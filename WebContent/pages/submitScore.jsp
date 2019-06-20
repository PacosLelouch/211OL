<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%@ page import="java.util.*" %>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.DBController" %>
<%@ page import="javaCode.Response" %>
<%!
	String name, score, extra;
	String size;
	Response addResult;
%>
<jsp:useBean id="db" class="javaCode.DBController" scope="application" />
<%
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits submitScore.");*/
	name = (String)session.getAttribute("username");
	if(name == null){
		response.sendError(403, "Invalid user.");
	} else{
		try{
			if(request.getMethod().equalsIgnoreCase("post")){
				size = request.getParameter("size");
				score = request.getParameter("score");
				System.out.println("size:" + size +",score:" + score);
				addResult = db.addPlayRecord(name, Integer.parseInt(size), Integer.parseInt(score));
				if((Boolean)addResult.getStatus() == false){
					response.sendError(403, (String)addResult.getMsg());
				} else{
					extra = (String)addResult.getMsg();
				}
			} else{
				response.sendError(403, "Invalid submission.");
			}
		} catch(Exception e){
			e.printStackTrace();
			response.sendError(403, "Invalid size or score.");
		}
	}
%>submit size:<%=size %> score:<%=score %> <%=extra %>