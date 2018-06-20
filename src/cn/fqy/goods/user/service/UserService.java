package cn.fqy.goods.user.service;



import java.sql.SQLException;

import cn.fqy.goods.user.dao.UserDao;
import cn.fqy.goods.user.domain.User;
import cn.itcast.commons.CommonUtils;

/**
 * 用户模块业务层
 */
/*
 * 错误及注意：
 * 1.RuntimeException:JVM运行时异常
 * 2.类加载器：prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
 * 3.参数替换MessageFormat.format()
 */
public class UserService {
	private UserDao userDao = new UserDao();
	
	/*
	 * ajax验证用户是否注册
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	/*
	 * ajax验证邮箱是否注册
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 注册
	 */
	public void regist(User user){
		/*
		 * 补齐参数
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		/*
		 * 向数据库添加用户
		 */
		userDao.add(user);
//		/*
//		 * 发送邮件
//		 */
//		/*
//		 * 使用properties读取邮件配置信息
//		 */
//		Properties prop = new Properties();
//		try {
//			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
//		} catch (IOException e1) {
//			throw new RuntimeException(e1);
//		}
//		/*
//		 * 创建session对象
//		 */
//		String host = prop.getProperty("host");//邮件服务器
//		String name = prop.getProperty("username");//邮箱登录名
//		String pass = prop.getProperty("password");//邮箱登陆密码
//		Session session = MailUtils.createSession(host, name, pass);
//		/*
//		 * mail对象
//		 */
//		String from = prop.getProperty("from");//发送人
//		String to = user.getEmail();//发送对象
//		String subject = prop.getProperty("subject");//主题
//		//MessageFormat.format():第一个参数“{数字}”,用第"数字"个参数替换
//		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationCode());//内容
//		Mail mail = new Mail(from, to, subject, content);
//		
//		try {
//			//发送邮件
//			MailUtils.send(session, mail);
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
		
	}
	
	/*
	 * 登录功能
	 */
	public User login(User user){
		try {
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
	
	/*
	 *修改密码
	 */
	public void updatePassword(String uid, String oldPassword, String newPassword)
		throws UserException{
		/*
		 * 1.根据uid和旧密码查询用户
		 */
		try {
			boolean bool = userDao.findByUidAndPassword(uid, oldPassword);
			if(!bool){
				throw new UserException("老密码错误！");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		/*
		 * 2.修改密码
		 */
		userDao.updatePassword(uid, newPassword);
	}

}
