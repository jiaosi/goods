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
    
    <title>My JSP 'top.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <h2 style="{text-align: center;}">网上书城购书系统</h2>
    <div style="{font-size: 10pt; line-height: 10px;}">
    	<!-- 根据用户是否登录显示不同链接 -->
    	<c:choose>
    		<c:when test="${empty sessionScope.sessionUser }">
    			<a href="<c:url value='/jsps/user/login.jsp' />" target="_parent">登录</a> |&nbsp;
    			<a href="<c:url value='/jsps/user/regist.jsp' />" target="_parent">注册</a>
    		</c:when>
    		<c:otherwise>
    			会员：${sessionScope.sessionUser.loginname }&nbsp;&nbsp;|&nbsp;&nbsp;
    			<a href="<c:url value='/CartServlet?method=myCart' />" target="body">我的购物车</a>&nbsp;&nbsp;|&nbsp;&nbsp;
    			<a href="<c:url value='/OrderServlet?method=myOrders' />" target="body">我的订单</a>&nbsp;&nbsp;|&nbsp;&nbsp;
    			<a href="<c:url value='/jsps/user/pwd.jsp' />" target="body">修改密码</a>&nbsp;&nbsp;|&nbsp;&nbsp;
    			<a href="<c:url value='/UserServlet?method=quit' />" target="_parent">退出</a>&nbsp;&nbsp;|&nbsp;&nbsp;
    			<a href="<c:url value='#' />" target="_top">联系我们</a>
    		</c:otherwise>
    	</c:choose>
    </div>
  </body>
</html>
