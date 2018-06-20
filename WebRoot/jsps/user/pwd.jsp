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
    
    <title>修改密码</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/pwd.css'/>">
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.4.js'/>" ></script>
	<script type="text/javascript" src="<c:url value='/jsps/js/user/pwd.js'/>" ></script>
  </head>

  <body>
    <div class="divMain">
    	<div class="divTitle"><span>修改密码</span></div>
    	<div class="divBody">
    		<form action="<c:url value='/UserServlet' />" method="post" id="updatePassswordForm">
    			<input type="hidden" name="method" value="updatePassword"/>
    			<table class="tableForm">
    				<tr>
    					<td>原密码：</td><td><input class="inputPsw" type="password" name="loginpass" id="password" value="${user.loginpass }"/></td>
    					<td><label class="classlabel" id="passwordError">${msg }</label></td>
    				</tr>
    				<tr>
    					<td>新密码：</td><td><input class="inputPsw" type="password" name="newloginpass" id="newPassword" value="${user.newloginpass }"/></td>
    					<td><label class="classlabel" id="newPasswordError"></label></td>
    				</tr>
    				<tr>
    					<td>确认密码：</td><td><input class="inputPsw" type="password" name="reloginpass" id="reNewPassword" value="${user.reloginpass }"/></td>
    					<td><label class="classlabel" id="reNewPasswordError"></label></td>
    				</tr>
    				<tr>
    					<td>验证码：</td><td><input type="text" name="verifyCode" id="verifyCode" /></td>
    					<td><label class="classlabel" id="verifyCodeError"></label></td>
    				</tr>
    				<tr>
    					<td></td><td><img id="verifyCodeImage" src="<c:url value='/VerifyCodeServlet' />" /><a href="javascript:_gh()">换一张</a></td>
    				</tr>
    				<tr>
    					<td><input type="image" src="<c:url value='/images/updatePassword.png' />" /></td>
    				</tr>
    			</table>
    		</form>
    	</div>
    </div>
  </body>
</html>
