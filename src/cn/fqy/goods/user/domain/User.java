package cn.fqy.goods.user.domain;

/**
 * 用户模块实体层
 */
/*
 * 这是一个bean
 * 属性来源：
 * 	1.t_user表中的字段名
 * 	2.User表单下需要的表单数据
 */
public class User {
	//对应数据库
	private String uid;//主键
	private String loginname;//登录名
	private String loginpass;//登录密码
	private String email;//电子邮箱
	private boolean status;//激活状态，true已激活
	private String activationCode;//激活码，唯一值
	
	//注册表单
	private String reloginpass;//确认密码
	private String verifyCode;//验证码
	
	//修改密码表单
	private String newloginpass;//新密码
	
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getNewloginpass() {
		return newloginpass;
	}
	public void setNewloginpass(String newloginpass) {
		this.newloginpass = newloginpass;
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the loginname
	 */
	public String getLoginname() {
		return loginname;
	}
	/**
	 * @param loginname the loginname to set
	 */
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	/**
	 * @return the loginpass
	 */
	public String getLoginpass() {
		return loginpass;
	}
	/**
	 * @param loginpass the loginpass to set
	 */
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * @return the activationCode
	 */
	public String getActivationCode() {
		return activationCode;
	}
	/**
	 * @param activationCode the activationCode to set
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
}
