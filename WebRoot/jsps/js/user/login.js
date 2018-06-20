$(function(){
	/*
	 * 1.获取错误信息label框，遍历之
	 */
	$(".errorClass").each(function(){
		showError($(this));
	});
	
	/*
	 * 2.提交按钮颜色变化
	 */
	$("#submitBtn").hover(
			function(){
				$(this).attr("src","/goods/images/regist1.jpg");
			},
			function(){
				$(this).attr("src","/goods/images/regist2.jpg");
			}
	);
	/*
	 * 3.输入框得到焦点
	 */
	$(".inputClass").focus(function(){
		var labelId = $(this).attr("id")+"Error";
		$("#"+labelId).text("");//将提示框内容清空
		showError($("#"+labelId));
	});
	/*
	 * 4.输入框失去焦点
	 */
	$(".inputClass").blur(function(){
		var id = $(this).attr("id");//获得输入框id
		var funName = "validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";
		eval(funName);//ִ括号内为可执行js
	});
	/*
	 * 5.表单提交验证所有
	 */
	$("#loginForm").submit(function(){
		var boolean = true;
		$(".inputClass").each(function(){
			var id = $(this).attr("id");
			boolean = eval("validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()");
		});
		return boolean;
	});
});
/*
 * 登录名验证方法
 */
function validateLoginname(){
	var id = "loginname";
	var value = $("#"+id).val();
	/*
	 * 1.非空验证
	 */
	if(!value){
		$("#"+id+"Error").text("用户名不能为空！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		return false;
	}
	/*
	 * 2.长度验证
	 */
	if(value.length < 3 || value.length > 20){
		$("#"+id+"Error").text("用户名长度应在3到20之间！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		false;
	}
	/*
	 * 3.用户是否注册验证
	 */
//	$.ajax({
//		url:"/goods/UserServlet",
//		data:{method:"ajaxValidateLoginname", loginname:value},
//		Type:"POST",
//		dataType:"json",
//		async:false,
//		cache:false,
//		success:function(result){
//			
//			if(!result){
//				$("#"+id+"Error").text("用户名已注册！");//获得提示框对象，设置内容
//				showError($("#"+id+"Error"));
//			}
//		}
//	});
	return true;
}
/*
 * 登录密码验证方法
 */
function validateLoginpass(){
	var id = "loginpass";
	var value = $("#"+id).val();
	/*
	 * 1.非空验证
	 */
	if(!value){
		$("#"+id+"Error").text("密码不能为空！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		return false;
	}
	/*
	 * 2.长度验证
	 */
	if(value.length < 3 || value.length > 20){
		$("#"+id+"Error").text("密码长度应在3到20之间！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		false;
	}
	
	return true;
}
/*
 * 确认密码验证
 */
function validateReloginpass(){
	var id = "reloginpass";
	var value = $("#"+id).val();
	/*
	 * 1.非空验证
	 */
	if(!value){
		$("#"+id+"Error").text("密码不能为空！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		return false;
	}
	/*
	 * 2。是否和上一次密码输入相同
	 */
	if(value != $("#loginpass").val()){
		$("#"+id+"Error").text("两次密码输入不一致！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		false;
	}
	return true;
}
/*
 * 邮箱验证
 */
function validateEmail(){
	var id = "email";
	var value = $("#"+id).val();
	/*
	 * 1.非空验证
	 */
	if(!value){
		$("#"+id+"Error").text("邮箱不能为空！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		return false;
	}
	/*
	 * 2.格式验证
	 */
	if(!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)){
		$("#"+id+"Error").text("email格式错误！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		false;
	}
	/*
	 * 3.用户是否注册验证
	 */
	return true;
}
/*
 * 注册码验证方法
 */
function validateVerifyCode(){
	var id = "verifyCode";
	var value = $("#"+id).val();
	/*
	 * 1.非空验证
	 */
	if(!value){
		$("#"+id+"Error").text("验证码不能为空！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		return false;
	}
	/*
	 * 2.长度是否正确为4
	 */
	if(value.length != 4){
		$("#"+id+"Error").text("验证码错误！");//获得提示框对象，设置内容
		showError($("#"+id+"Error"));//显示提示信息
		return false;
	}
}

function showError(ele){
	/*
	 * 显示错误信息
	 */
	var text = ele.text();
	if(!text){
		ele.css("display","none");
	}else{
		ele.css("display","");
	}
}

function _hyz(){
	/*
	 *换一张验证码
	 * 1.获取<img>的src属性，修改其值
	 * 2.用毫秒传参可防重复new Date().getTime()
	 */
	$("#imgVerifyCode").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());
}









