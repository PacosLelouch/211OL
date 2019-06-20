<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%@ page import="java.util.*" %>
<%@ page import="javaCode.HtmlEncode" %>
<%@ page import="javaCode.UrlRepository" %>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits playing.");*/
	String name = (String)request.getSession().getAttribute("username");
	String direction = "None";
	String size = "3";
	if(name == null || request.getSession().getAttribute("isLogin") == null){
		response.sendError(403, "Invalid visit.");
	} else{
		direction = "None";
		System.out.println("Playing, request method:" + request.getMethod());
		if(request.getMethod().equalsIgnoreCase("get")){
			size = request.getParameter("size");
			if(size == null){
				response.sendError(403, "Invalid size.");
			}
		}
	}
%><!DOCTYPE html>
<head>
	<link rel="shortcut icon" href="<%=UrlRepository.getImagesUrl("favicon.ico") %>" />
    <link rel="stylesheet" type="text/css" href="<%=UrlRepository.getStylesUrl("style.css") %>" />
	<title>211OL: playing</title>
	<style>
	</style>
</head>
<body>
    <h1>
        <%=HtmlEncode.encode(name) %>:<span id="size"><%=size %></span>x<%=size %><span id="game-over"></span>
    </h1>
    <p>
        Score: <span id="score">0</span>
        <br />
        Last Move: <span id="last-move">None</span>
    </p>
    <canvas id="game-board"></canvas>
    <!-- <p id="test">
        {{ game.to_json }}
    </p> -->
    <br />
    <%-- 
    <div id="game-ctrl">
        <br />
        <input type="button" id="btn-up" class="btndir" value="up" />
        <br />
        <input type="button" id="btn-left" class="btndir" value="left" />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" id="btn-right" class="btndir" value="right" />
        <br />
        <input type="button" id="btn-down" class="btndir" value="down" />
        <br />
    </div>
    --%>
    <br />
    <div id="submit-score" hidden>
        <input type="button" id="btn-submit" class="btn" value="Submit" />
        <%--
        <form action="{% url 'Game_2048:submit_score' %}" method="post">
            {% csrf_token %}
            <input type="hidden" name="size" value="{{ size }}" />
            <input type="hidden" name="name" value="{{ name }}" />
            <input type="hidden" name="score" value="{{ game.board.score }}" />
            <input width="100" height="100" type="submit" name="submit_score" value="Submit Score" />
        </form>
        --%>
    </div>
    <br />
    <a href="<%=UrlRepository.getHome() %>"><input type="button" class="btn" value="Return" /></a>
    <p><span id="submit-msg"></span></p>
</body>
<script src="2048.js">
</script>
<%--
<script>
        $('#btn-up').click(function () {
            button_click("up");
        })
        $('#btn-down').click(function () {
            button_click("down");
        })
        $('#btn-left').click(function () {
            button_click("left");
        })
        $('#btn-right').click(function () {
            button_click("right");
        })
        $('#btn-submit').onclick(function () {
            $.ajax({
                url: "{% url 'Game_2048:submit_score' %}",
                type: "POST",
                //contentType: 'application/json', 
                //dataType: 'json',
                async: true,
                data: {
                    'size': parseInt("{{size}}"),
                    'score': parseInt($('#score').text()),
                },
                complete: function (data) {
                    try {
                        $('#submit-msg').text(data.responseText);
                        $('#btn-submit').hide();
                    }
                    catch (exception) {
                        $('#submit-msg').text(exception);
                    }
                },
            })
        })
</script>
 --%>