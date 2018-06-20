$(function(){
	/*
	 * 1.密码长度验证
	 */
	$("#password").blur(function(){
		var value = $("#password").val();
		var length = value.length;
		if(length>20 || length<3){
			$("#passwordError").text("密码长度应在3-20之间！");
		}
	});
	$("#password").focus(function(){
		$("#passwordError").text("");
	});
	
	/*
	 * 2.两次密码输入是否一致
	 */
	$("#reNewPassword").blur(function(){
		var newPassword = $("#newPassword").val();
		var reNewPassword = $("#reNewPassword").val();
		if(newPassword != reNewPassword){
			$("#reNewPasswordError").text("两次密码输入不一致");;
		}
	});
	$("#reNewPassword").focus(function(){
		$("#reNewPasswordError").text("");
	});
	
	/*
	 * 验证码长度验证
	 */
	$("#verifyCode").blur(function(){
		var length = $(this).val().length;
		if(length != 4){
			$("#verifyCodeError").text("验证码长度不正确");
		}
	});
	$("#verifyCode").focus(function(){
		$("#verifyCodeError").text("");
	});
	
	/*
	 * 提交时全部检验
	 */
	$("#updatePassswordForm").submit(function(){
		var password = $("#password").val();
		var newPassword = $("#newPassword").val();
		var reNewPassword = $("#reNewPassword").val();
		var verifyCode = $("#verifyCode").val();
		
		if(password == "" || newPassword == "" || reNewPassword == "" || verifyCode == ""){
			return false;
		}
		$(".classlabel").each(function(){
			
		});
	});
	
});
/*
 * 换一张验证码
 */
function _gh(){
	$("#verifyCodeImage").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());
	
}



