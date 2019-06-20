<%@ page language="java" import="java.util.*,java.sql.*" 
         contentType="text/html; charset=utf-8"
%>
<%@ page import="java.io.*, java.util.*, java.sql.*, org.apache.commons.io.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%--@ page import="javaCode.IpAddrGetter" --%>
<%@ page import="javaCode.DBController" %>
<%@ page import="javaCode.HtmlEncode" %>
<%@ page import="javaCode.UrlRepository" %>
<jsp:useBean id="db" class="javaCode.DBController" scope="application" />
<%
	int uploadStatus = 0;
	HashMap<String, Object> info = new HashMap<String, Object>();
	ArrayList<java.util.Date> loginRecord = new ArrayList<java.util.Date>();
	ArrayList<HashMap<String, Object>> playRecord = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> hs = new ArrayList<HashMap<String, Object>>();
	String folderPath = "/images", fileDir = "", fileName = "";
	request.setCharacterEncoding("utf-8");/*
	ipAddr = IpAddrGetter.get(request);
	System.out.println("IP Address:" + ipAddr + " visits personal.");*/
	String user = (String)request.getSession().getAttribute("username");
	Boolean isLogin = (Boolean)request.getSession().getAttribute("isLogin");
	if(user == null || isLogin == null || isLogin != true){
		response.sendError(403, "Invalid visit.");
	} else{
		info = (HashMap<String, Object>)db.showUserInfo(user).getData();
		loginRecord = (ArrayList<java.util.Date>)db.showLoginRecord(user, 0, 10).getData();
		playRecord = (ArrayList<HashMap<String, Object>>)db.showPlayerRecord(user, 0, 10).getData();
		hs = (ArrayList<HashMap<String, Object>>)db.showPersonalHighScore(user).getData();
		System.out.println("Why bug if no output?");
		/*photo upload*/
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);//是否用multipart提交的? 
		if (isMultipart && request.getMethod().equalsIgnoreCase("post")) { 
			DiskFileItem dfi = null;
			FileItemFactory factory = new DiskFileItemFactory(); 
			//factory.setSizeThreshold(yourMaxMemorySize); //设置使用的内存最大值 
			//factory.setRepository(yourTempDirectory);    //设置文件临时目录 
			ServletFileUpload upload = new ServletFileUpload(factory); 
			upload.setSizeMax(5 * 1024 * 1024);  //允许的最大文件尺寸5MB
			List items = upload.parseRequest(request); 
			for (int i = 0; i < items.size(); i++) { 
				FileItem fi = (FileItem) items.get(i); 
				if (fi.isFormField()) {//如果是表单字段 
					//out.print(fi.getFieldName()+":"+fi.getString("utf-8")); 
				} else {//如果是文件
					dfi = (DiskFileItem) fi; 
				} //if 
			} //for
			if (!dfi.getName().trim().equals("") && dfi.getName().substring(dfi.getName().length() - 4).equals(".jpg")) {//getName()返回文件名称或空串 
				try {
					//out.print("文件被上传到服务上的实际位置："); 
					fileName = (String)info.get("uid").toString() + "_photo.jpg";
					fileDir = application.getRealPath(folderPath) + System.getProperty("file.separator") + fileName; 
					System.out.println("File Directory:" + fileDir);
					File newFile = new File(fileDir);
					//out.print(newFile.getAbsolutePath() + "<br/>"); 
					dfi.write(newFile); 
					//String href = folderPath + System.getProperty("file.separator") + fileName;
					//out.print("<a href='" + href + "'>" + fileName + "</a>");
					uploadStatus = 1;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					uploadStatus = 2;
				}
			} else{
				uploadStatus = 2;
			}//if 
		} //if 
	}
%><!DOCTYPE html>
<head>
	<link rel="shortcut icon" href="<%=UrlRepository.getImagesUrl("favicon.ico") %>" />
    <link rel="stylesheet" type="text/css" href="<%=UrlRepository.getStylesUrl("style.css") %>" />
	<title>211OL: personal</title>
	<style>
	</style>
</head>
<body>
    <br />
    <a href="<%=UrlRepository.getHome() %>"><input type="button" class="btn" value="Return" /></a>
    <br />
    <!--p>Here is <strong>{{ name }}</strong>'s information.</p-->
    <h1>Your Information</h1>
    <div class="personalinfolayout">
        <%-- TODO photo --%>
		<div id="photo-title">Photo</div>
        <img class="photo" src="<%=UrlRepository.getImagesUrl("") %><%=(String)info.get("uid") %>_photo.jpg" onerror="javascript:this.src='<%=UrlRepository.getImagesUrl("defaultGray.jpg") %>'">
        <form action="" encType="multipart/form-data" method="post" > 
			<input type="file" name="selPicture" style="width: 330px; height: 23px; font-size: 16px;"><br />
			<input type="submit" name="upload" id="upload" value="upload" class="btn"> 
		</form> 
		<div id="photo-upload-msg"><%=uploadStatus == 1 ? "upload success" : (uploadStatus == 2 ? "upload fail" : "") %></div>
        <table id="loginrcs" class="table">
            <%-- {% for uid, name, email, regtime in info %} --%>
            <tr><td>uid</td><td align="left"><%=(String)info.get("uid") %></td></tr>
            <tr><td>name</td><td align="left"><%=HtmlEncode.encode((String)info.get("name")) %></td></tr>
            <tr><td>email</td><td align="left"><%=HtmlEncode.encode((String)info.get("email")) %></td></tr>
            <tr><td>register time</td><td align="left" class="timecolumn"><%=(String)info.get("register_time") %></td></tr>
            <%-- } --%>
            <%-- {% endfor %} --%>
        </table>
    </div>
    <h1>Highest Score</h1>
    <div class="personalhighscorelayout">
        <table id="highscorercs" class="table">
            <tr><th>rank</th><th>score</th><th>time</th></tr>
            <% for(int i = 0; i < hs.size(); ++i){
            	HashMap<String, Object> eachHs = hs.get(i); %>
            <%-- {% for rank, score, time in hs %} --%>
            <tr>
                <td align="left"><%=(String)eachHs.get("level") %></td>
                <td align="left"><%=(String)eachHs.get("score") %></td>
                <td align="left" class="timecolumn"><%=(Timestamp)eachHs.get("playtime") %></td>
            </tr>
            <% } %>
            <%-- {% endfor %} --%>
        </table>
    </div>
    <h1>Login Record</h1>
    <div class="personalloginlayout">
        <div class="description">Latest 10</div>
        <table id="loginrcs" class="table">
            <!--tr><th>time</th><tr-->
            <% for(int num = 0; num < loginRecord.size(); ++num){ %>
            <%-- {% for num, time in loginrecord %} --%>
            <tr>
                <td align="left"><%=num + 1 %></td>
                <td align="left" class="timecolumn"><%=loginRecord.get(num).toString() %></td>
            </tr>
            <% } %>
            <%-- {% endfor %} --%>
        </table>
    </div>
    <h1>Play Record</h1>
    <div class="personalplayrecordlayout">
        <div class="description">Latest 10</div>
        <table id="playrcs" class="table">
            <tr><th>rank</th><th>score</th><th>time</th></tr>
            <% for(HashMap<String, Object> eachPrc: playRecord){ %>
            <%-- {% for rank, score, playtime in playrecord %} --%>
            <tr>
                <td align="left"><%=(String)eachPrc.get("level") %></td>
                <td align="left"><%=(String)eachPrc.get("score") %></td>
                <td align="left" class="timecolumn"><%=(Timestamp)eachPrc.get("playtime") %></td>
            </tr>
            <% } %>
            <%-- {% endfor %} --%>
        </table>
    </div>
    <br />
</body>