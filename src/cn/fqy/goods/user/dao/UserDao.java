package cn.fqy.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.fqy.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

/**
 *用户模块持久层
 */
/*
 * 出错：
 * 1.count(1)优化于count(*)
 * 2.表名写成数据库名
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/*
	 * ajax验证用户是否注册
	 * true为未注册
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "select count(1) from t_user where loginname=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), loginname);
		return number.intValue()==0;
	}
	
	/**
	 * ajax验证邮箱是否注册
	 * true为未注册
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "select count(1) from t_user where email=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), email);
		return number.intValue()==0;
	}
	
	/*
	 * 添加用户
	 */
	public void add(User user){
		String sql = "insert into t_user value(?,?,?,?,?,?) ";
		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),user.isStatus(),user.getActivationCode()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 登录，查询用户
	 */
	public User findByLoginnameAndLoginpass(String loginname, String loginpass) throws SQLException{
		
		String sql = "select * from t_user where loginname=? and loginpass=?";
		return qr.query(sql, new BeanHandler(User.class), loginname, loginpass);
	}
	
	/*
	 * 根据uid和password查询用户
	 * true为查找到
	 */
	public boolean findByUidAndPassword(String uid, String password) throws SQLException{
		String sql = "select count(1) from t_user where uid=? and loginpass=?";
		Number number = qr.query(sql, new ScalarHandler(), uid, password);
		return number.intValue() > 0;
	}
	
	/*
	 * 修改密码
	 */
	public void updatePassword(String uid, String password){
		String sql ="update t_user set loginpass=? where uid=?";
		try {
			qr.update(sql, password, uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
}













