<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,user-scalable=no,initial-scale=1,maximum-scale=1,minimum-scale=1">
<title>科荣AIO</title>
<script type=text/javascript>
<%
	String user = (String) request.getAttribute("user");
	if (user != null)
		out.println("window.localStorage.setItem('user', JSON.stringify("+ user + "))");
	String wxstate = (String) request.getSession().getAttribute("wxstate");
	wxstate = wxstate == null ? "" : wxstate;
	out.println("window.sessionStorage.setItem('wxstate', '" + wxstate+ "')");
%>	
</script>
<style lang=scss rel=stylesheet/scss>
#app {
	width: 100%;
	height: 100%;
}
</style>

<!-- 打包时更新以下代码  -->
<link href=<%=request.getContextPath()%>/mobile//static/css/app.c57ded6e74c2c14bedb9bca2b0d4fc34.css
	rel=stylesheet>
</head>
<body>
	<div id=app></div>
	<script type=text/javascript
		src=<%=request.getContextPath()%>/mobile//static/js/manifest.de4b9073eba3d67d0ffc.js></script>
	<script type=text/javascript
		src=<%=request.getContextPath()%>/mobile//static/js/vendor.dd2def72b703427b04c8.js></script>
	<script type=text/javascript
		src=<%=request.getContextPath()%>/mobile//static/js/app.2a15d183bdca2c51d34d.js></script>
</body>
</html>
