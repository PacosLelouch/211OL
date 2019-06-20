<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.DBController" %>
<%@ page import="javaCode.Response" %>
<%@ page import="javaCode.HtmlEncode" %>
<%!
	String home = "index.jsp";
	String user, postmsg, content;
	int msgPgno = 0;
	int msgPgcnt = 50;
	boolean guest = true;
	HashMap<String, Object> info = new HashMap<String, Object>();
	Response addResult;
	int[] levelList;
	ArrayList<HashMap<String, Object>> msg = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> hs = new ArrayList<HashMap<String, Object>>();
%>
<jsp:useBean id="db" class="javaCode.DBController" scope="application" />
<%
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits messageBoard.");*/
	user = (String)session.getAttribute("username");
	if(user == null || session.getAttribute("isLogin") == null){
		guest = true;
	} else{
		guest = false;
	}
	postmsg = "";
	if(request.getMethod().equalsIgnoreCase("post")){
		content = request.getParameter("content");
		if(content == null || content.length() < 1 || content.length() > 300){
			postmsg = "Invalid content";
		} else{
			addResult = db.addMessage(user, content);
			Boolean status = (Boolean)addResult.getStatus();
			if(status != null && status == false){
				response.sendError(403, (String)addResult.getMsg());
			}
			response.sendRedirect("messageBoard.jsp");
		}
	} else if(!request.getMethod().equalsIgnoreCase("get")){
		response.sendError(403, "Invalid visit");
	} else{
		String msgPgnoStr = request.getParameter("msgPgno");
		String msgPgcntStr = request.getParameter("msgPgcnt");
		if(msgPgnoStr != null && msgPgcntStr != null && msgPgnoStr.length() > 0 && msgPgcntStr.length() > 0){
			try{
				msgPgno = Integer.parseInt(msgPgnoStr);
				msgPgcnt = Integer.parseInt(msgPgcntStr);
			} catch(Exception e){
				e.printStackTrace();
				msgPgno = 0;
				msgPgcnt = 50;
			}
		}
		msg = (ArrayList<HashMap<String, Object>>)db.showMessage(msgPgno, msgPgcnt).getData();
		hs = new ArrayList<HashMap<String, Object>>();
		levelList = new int[]{3, 4, 5, 6, 7, 8, 9, 2};
		for(int level: levelList){
			HashMap<String, Object> eachHs = new HashMap<String, Object>();
			eachHs.put("level", new Integer(level));
			ArrayList<HashMap<String, Object>> hss = (ArrayList<HashMap<String, Object>>)db.showScoreBoard(level, 0, 50).getData();
			eachHs.put("hss", hss);
			hs.add(eachHs);
		}
	}
%><!DOCTYPE html>
<head>
	<link rel="shortcut icon" href="../img/favicon.ico" />
    <link rel="stylesheet" type="text/css" href="../styles/style.css" />
	<title>211OL: messageBoard</title>
	<style>
	</style>
</head>
<body>
    <p>
        <a href="index.jsp"><input type="button" class="btn" value="Return" /></a>
    </p>
    <h1>Score Board</h1>
    <div class="descriptioncenter">level</div>
    <div class="selectlayout">
        <select id="levelselection" list="levellist" class="select">
            <% for(int level: levelList){ %>
            <%-- {% for level in levellist %} --%>
            <option value="<%=level %>" <%=level == 3?"selected":"" %>><%=level %></option>
            <% } %>
            <%-- {% endfor %} --%>
        </select>
    </div>
    <br />
    <div class="scoreboardlayout">
        <div class="description">Top 50</div>
        <% for(HashMap<String, Object> hsMap: hs){ 
        	Integer level = (Integer)hsMap.get("level");
        	ArrayList<HashMap<String, Object>> hss = 
        			(ArrayList<HashMap<String, Object>>)hsMap.get("hss"); %>
        <%-- {% for level, hss in hs.items %} --%>
        <table id="levels<%=level %>" class="table" hidden>
            <tr><th>no.</th><th>name</th><th>score</th><th>time</th></tr>
            <% for(int num = 0; num < hss.size(); ++num){
            	HashMap<String, Object> hssMap = hss.get(num); %>
            <%-- {% for number, name, score, time in hss %} --%>
            <tr>
                <td align="left"><%=num + 1 %></td>
                <td align="left"><%=HtmlEncode.encode((String)hssMap.get("name")) %></td>
                <td align="left"><%=(String)hssMap.get("score") %></td>
                <td align="left" class="timecolumn"><%=(Timestamp)hssMap.get("playtime") %></td>
            </tr>
            <% } %>
            <%-- {% endfor %} --%>
        </table>
        <% } %>
        <%-- {% endfor %} --%>
    </div>
    <h1>Contents</h1>
    <div id="contentboard" class="contentlayout">
    	<% if(guest){ %>
        <%-- {% if guest %} --%>
        <p><strong>Guests are forbidden to submit a content.</strong></p>
        <% } else{ %>
        <%-- {% else %} --%>
        <form action="" method="POST">
            <%-- {% csrf_token %} --%>
            <div class="name"><%=HtmlEncode.encode(user) %>:</div>
            <!--div class="description">(write a content)</div-->
            <textarea id="content_id" class="contentbox" name="content" placeholder="Write a content...(length from 1 to 300)" maxlength="300"></textarea>
            <br />
            <div id="content_count" class="description">300 left</div>
            <input type="submit" class="btn" value="Submit" />
            <br /><br />
        </form>
        <% } %>
        <%-- {% endif %} --%>
    </div>
    <p><%=postmsg %></p>
    <br />
    <div class="messagelayout">
    	<br />
    	<%--<form action="" method="get">
    		<input type="hidden" name="msgPgno" value="<%=Math.max(msgPgno - 1, 0) %>" />
    		<input type="hidden" name="msgPgcnt" value="<%=msgPgcnt %>" />
    		<input type="submit" class="btn" id="previous" value="previous" />
        </form>
        <form action="" method="get">
    		<input type="hidden" name="msgPgno" value="<%=msgPgno + 1 %>" />
    		<input type="hidden" name="msgPgcnt" value="<%=msgPgcnt %>" />
    		<input type="submit" class="btn" id="next" value="next" />
        </form>--%>
        <a href="messageBoard.jsp?msgPgno=<%=Math.max(msgPgno - 1, 0) %>&msgPgcnt=<%=msgPgcnt %>"><input type="submit" class="btn" id="previous" value="previous" /></a>
        <a href="messageBoard.jsp?msgPgno=<%=msgPgno + 1 %>&msgPgcnt=<%=msgPgcnt %>"><input type="submit" class="btn" id="next" value="next" /></a>
    	<br />
        <div class="description">Each page 50</div>
        <table id="msgs" class="table">
            <!--tr><th>name</th><th>message</th><th class="timecolumn">time</th></tr-->
            <% for(HashMap<String, Object> msgMap: msg){ %>
            <%-- {% for msgname, msgtxt, msgtime in msg %} --%>
            <tr>
                <td align="center" rowspan="2"><img class="photo" src="../../../images/<%=(Integer)msgMap.get("uid") %>_photo.jpg" onerror="javascript:this.src='../../../images/defaultGray.jpg'"></td>
            	<td align="center"><%=HtmlEncode.encode((String)msgMap.get("name")) %></td>
            </tr>
            <tr>
            	<td align="center" class="timecolumn"><%=(Timestamp)msgMap.get("msg_time") %></td>
            </tr>
            <tr><td align="left" colspan="2" class="contentdisplay"><pre><%=HtmlEncode.encode((String)msgMap.get("msg_text")) %></pre></td></tr>
            <% } %>
            <%-- {% endfor %} --%>
        </table>
    </div>
    <p id="test"></p>
</body>
<script>
    window.onload = function () {
    	var level0Selection = document.getElementById("levelselection");
        var levelList = [3, 4, 5, 6, 7, 8, 9, 2];
        var level0;
    	for (var i = 0; i < level0Selection.length; i++){
    		if(level0Selection.options[i].selected) {
    			level0 = level0Selection.options[i].text;
    		}
    	}
    	console.log("levels" + level0.toString());
        document.getElementById("levels" + level0.toString()).removeAttribute("hidden");
        level0Selection.addEventListener("change", function () {
            console.log(levelList);//test
            for (var level in levelList) {
                document.getElementById("levels" + levelList[level].toString()).setAttribute("hidden", "true");
                console.log('hide #levels' + levelList[level].toString());//test
            }
            var level;
            for (var i = 0; i < level0Selection.length; i++){
        		if(level0Selection.options[i].selected) {
        			level = level0Selection.options[i].text;
        		}
        	}
            document.getElementById("levels" + level.toString()).removeAttribute("hidden");
            console.log('show #levels' + level.toString());//test
        })
        document.getElementById("content_id").addEventListener("input", function () {
        	console.log("content_id, event");
            document.getElementById("content_count").textContent =
            		(300 - document.getElementById("content_id").value.length).toString() + ' left';
        })
    }
</script>