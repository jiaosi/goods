package cn.fqy.goods.admin.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.fqy.goods.admin.admin.domain.Admin;
import cn.itcast.jdbc.TxQueryRunner;

public class AdminDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查询admin
	 * @param adminname
	 * @param adminpwd
	 * @return
	 * @throws SQLException
	 */
	public Admin find(String adminname, String adminpwd) throws SQLException{
		String sql = "select * from t_admin where adminname=? and adminpwd=?";
		Admin admin = qr.query(sql, new BeanHandler<Admin>(Admin.class), adminname, adminpwd);
		return admin;
	}
}
