<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/regist.css' />" >
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/regist.css' />" >
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.4.js' />"></script>
	<script type="text/javascript" src="<c:url value='/jsps/js/user/login.js' />"></script>

<script type="text/javascript">
	$(function()
	{
		/*Cookie是Map,  Map<String(cookie名称)，Cookie(cookie本身)>*/
		var loginname = window.decodeURI("${cookie.loginname.value}");
		if ("${requestScope.form.loginname}") {//不为空，说明有登录行为
			loginname = "${requestScope.form.loginname}";
		}
		$("#loginname").val(loginname);
	});
</script>

  </head>
  
  <style type="text/css">
  #divMain{
	margin-left: 240px;
}
  </style>
  
  <body>
    <div id="divMain">
  		<div id="divTitle">
  			<span id="spanTitle">用户登录</span>
  		</div>
  		<div id="divBody">
  		<form action="<c:url value='UserServlet' />" method="post" id="loginForm">
  		<input type="hidden" name="method" value="login" />
  			<table id="tableForm">
  				<tr>
  					<td class="tdText">用户名：</td>
  					<td class="tdInput">
  						<input class="inputclass" type="text" name="loginname" id="loginname" />
  					</td>
  					<td class="tdError">
  						<label class="errorClass" id="loginnameError">${msg}</label>
  					</td>
  				</tr>
  				<tr>
  					<td class="tdText">登录密码：</td>
  					<td class="tdInput">
  						<input class="inputclass" type="password" name="loginpass" id="loginpass" value="${form.loginpass}" />
  					</td>
  					<td class="tdError">
  						<label class="errorClass" id="loginpassError">${errors.loginpass}</label>
  					</td>
  				</tr>
  				<tr>
  					<td class="tdText">图形验证码：</td>
  					<td class="tdInput">
  						<input class="inputclass" type="text" name="verifyCode" id="verifyCode"  value="${form.verifyCode}"/>
  					</td>
  					<td class="tdError">
  						<label class="errorClass" id="verifyCodeError">${errors.verifyCode}</label>
  					</td>
  				</tr>
  				<tr>
  					<td class="tdText"></td>
  					<td class="tdInput">
  						<div id="divVerifyCode"><img id="imgVerifyCode" src="<c:url value='/VerifyCodeServlet' />" /></div>
  					</td>
  					<td class="tdError">
  						<a href="javascript:_hyz()">换一张</a>
  					</td>
  				</tr>
  				<tr>
  					<td class="tdText"></td>
  					<td class="tdInput">
  						<input type="image" src="<c:url value='/images/bt_login.png' />" />
  					</td>
  					<td class="tdError">
  					
  					</td>
  				</tr>
  			</table>
  		</form>
  		</div>
  	</div>
  </body>
</html>
