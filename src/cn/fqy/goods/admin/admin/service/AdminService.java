package cn.fqy.goods.admin.admin.service;

import java.sql.SQLException;

import cn.fqy.goods.admin.admin.dao.AdminDao;
import cn.fqy.goods.admin.admin.domain.Admin;

public class AdminService {
	private AdminDao adminDao = new AdminDao();
	
	/**
	 * 查询admin
	 * @param admin
	 * @return
	 */
	public Admin login(Admin admin){
		try {
			return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
