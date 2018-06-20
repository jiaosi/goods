<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'list.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<c:url value='/jquery/jquery-1.4.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
	
	
<script type="text/javascript">
$(function(){
	showTotal();
	
	/*
	* 全选按钮操作
	*/
	$("#selectAll").click(function(){
		//获得全选按钮checked属性值
		var bool = $(this).attr("checked");
		setItemCheckbox(bool);
		setJieSuan(bool);
		
		//更改全选之后应该重新计算总计
		showTotal();
	});
	
	/*
	* 每个复选框
	* 通过复选框选中个数与总个数之间分情况比较，讨论各种情形。
	*/
	$(":checkbox[name=checkboxBtn]").click(function(){
		var select = $(":checkbox[name=checkboxBtn][checked=true]").length;//被选中的个数
		var all = $(":checkbox[name=checkboxBtn]").length;//所有复选框个数
		if(select == all){//全部选中
			$("#selectAll").attr("checked", true);
			setJieSuan(true);
		}else if(select == 0){//全部取消
			$("#selectAll").attr("checked", false);
			setJieSuan(false);
		}else{//其他情况
			$("#selectAll").attr("checked", false);
			setJieSuan(true);
		}
		showTotal();//不管怎样都应计算总价
	});
	
	/*
	* 点击jian进行减操作
	*/
	$(".jian").click(function() {
		//截取前32位，获得cartItemId
		var id = $(this).attr("id").substring(0, 32);
		//获取数量
		var quantity = $("#" + id + "Quantity").val();
		//一直点击jian会出现小于一，此时提醒是否为删除
		if (quantity == 1) {
			if (confirm("是否要删除这本书？")) {
				location("/goods/CartServlet?method=batchDelete&cartItemIds="+ id);
			}
		} else {
				//ajax进行修改数量
				sendUpdateQuantity(id, quantity - 1);
				showTotal();
	}
	});

		/*
		 * 点击jia进行加操作
		 */
		$(".jia").click(function() {
			//截取前32位，获得cartItemId
			var id = $(this).attr("id").substring(0, 32);
			//获取数量
			var quantity = $("#" + id + "Quantity").val();
			//
			sendUpdateQuantity(id, Number(quantity) + 1);//注意，此处用Number是因为有+号，可能被识别为字符串连接
			showTotal();
		});
		

});

	/*
	 * 计算总计费用
	 */
	function showTotal() {
		var total = 0;
		//选取复选框选中的条目
		$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
			//获取id前缀
			var id = $(this).val();
			//获取总计id
			var text = $("#" + id + "Subtotal").text();
			total += Number(text);
		});

		$("#total").text(round(total, 2));//将目标小数保留指定位数，round方法为四舍五入。此方法在/js/round.js
	}

	/*
	 * 设置复选框条目是否已勾选
	 */
	function setItemCheckbox(bool) {
		//根据bool值设置checked
		$(":checkbox[name=checkboxBtn]").attr("checked", bool);
	}

	/*
	 * 设置结算按钮
	 */
	function setJieSuan(bool) {
		if (bool) {
			$("#jiesuan").removeClass("kill").addClass("jiesuan");
			$("#jiesuan").unbind("click");//取消当前元素所有click事件
		} else {//没有勾选
			//变为灰色
			$("#jiesuan").removeClass("jiesuan").addClass("kill");
			//使点击无效
			$("#jiesuan").click(function() {
				return false;
			});
		}
	}

	/*
	 * 批量删除
	 */
	function batchDelete() {
		//1.创建数组存放被选中删除的对象
		//2.location指向目标方向。

		var cartItemArray = new Array();
		$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
			cartItemArray.push($(this).val());
		});

		location("/goods/CartServlet?method=batchDelete&cartItemIds="
				+ cartItemArray);//字符串数组可以不用toString，js会自动连接
	}

	/*
	 * ajax修改数量
	 */
	function sendUpdateQuantity(id, quantity) {
		$.ajax({
			async : false,
			cache : false,
			url : "/goods/CartServlet",
			data : {
				method : "updateQuantity",
				cartItemId : id,
				quantity : quantity
			},
			type : "post",
			dataType : "json",
			success : function(result) {
				$("#" + id + "Quantity").val(result.quantity);
				$("#" + id + "Subtotal").text(result.subtotal);
			}
		});
	}
	
	/*
	* 结算提交。包含表单
	*/
	function jiesuan(){
		//1.新建数组保存勾选书目cartItemIds
		var cartItemIds = new Array();
		//2.遍历选取项，添加进数组
		$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
			cartItemIds.push($(this).val());
		});
		//3.给提交参数赋值
		$("#cartItemIds").val(cartItemIds);
		$("#hiddenTotal").val($("#total").text());//给总计赋值，一并传参，后面页面需要此参数
		//4.提交表单
		$("#jiesuanForm").submit();
	};
</script>
  </head>
  
  <body>
<c:choose>
	<c:when test="${empty cartItemList }">
	<table width="95%" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
				<img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
			</td>
			<td>
				<span class="spanEmpty">您的购物车中暂时没有商品</span>
			</td>
		</tr>
	</table> 
	</c:when>
	
	<c:otherwise>
	<table width="95%" align="center" cellpadding="0" cellspacing="0">
	<tr align="center" bgcolor="#efeae5">
		<td align="left" width="50px">
			<input type="checkbox" id="selectAll" checked="checked"/><label for="selectAll">全选</label>
		</td>
		<td colspan="2">商品名称</td>
		<td>单价</td>
		<td>数量</td>
		<td>小计</td>
		<td>操作</td>
	</tr>

<c:forEach items="${cartItemList }" var="cartItem">
	<tr align="center">
		<td align="left">
			<input value="${cartItem.cartItemId }" type="checkbox" name="checkboxBtn" checked="checked"/>
		</td>
		<td align="left" width="70px">
			<a class="linkImage" href="<c:url value='/jsps/book/desc.jsp'/>"><img border="0" width="54" align="top" src="<c:url value='${cartItem.book.image_b }'/>"/></a>
		</td>
		<td align="left" width="400px">
		    <a href="<c:url value='/jsps/book/desc.jsp'/>"><span>${cartItem.book.bname }</span></a>
		</td>
		<td><span>&yen;<span class="currPrice" id="CurrPrice">${cartItem.book.currPrice }</span></span></td>
		<td>
			<a class="jian" id="${cartItem.cartItemId }Jian"></a><input class="quantity" readonly="readonly" id="${cartItem.cartItemId }Quantity" type="text" value="${cartItem.quantity }"/>
			<a class="jia" id="${cartItem.cartItemId }Jia"></a>
		</td>
		<td width="100px">
			<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId }Subtotal">${cartItem.subtotal }</span></span>
		</td>
		<td>
			<a href="<c:url value='/CartServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId }'/>">删除</a>
		</td>
	</tr>
</c:forEach>


	<tr>
		<td colspan="4" class="tdBatchDelete">
			<a href="javascript:batchDelete();">批量删除</a>
		</td>
		<td colspan="3" align="right" class="tdTotal">
			<span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
		</td>
	</tr>
	<tr>
		<td colspan="7" align="right">
			<a href="javascript:jiesuan()" id="jiesuan" class="jiesuan"></a>
		</td>
	</tr>
</table>
	<form id="jiesuanForm" action="<c:url value='/CartServlet'/>" method="post">
		<input type="hidden" name="cartItemIds" id="cartItemIds"/>
		<input type="hidden" name="total" id="hiddenTotal"/>
		<input type="hidden" name="method" value="loadCartItems"/>
	</form>

	</c:otherwise>
</c:choose>


  </body>
</html>
