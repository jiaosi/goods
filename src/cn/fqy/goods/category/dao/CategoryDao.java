package cn.fqy.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.fqy.goods.category.domain.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * 分类持久层
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/*
	 * 把一个map映射到一个category
	 * 此表为双向自身关联
	 * 也就是分为一级分类和二级分类进行讨论
	 */
	private Category toCategory(Map<String, Object> map){
		/*
		 * map(cid, cname, pid, desc, oderby)
		 * categoty(cid, cname, parent, desc)
		 */
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String)map.get("pid");
		if(pid != null){//为空说明是一级分类，不需要parent
			/*
			 * pid不为空，说明是二级分类，其中parent属性应当有值，也就是此二级分类对应的父分类parent??????
			 * 也就是一级分类category中parent为null，二级分裂category中parent不为null(又对应其父分类)
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		
		return category;
	}
	/*
	 * 把多个map映射为多个category
	 */
	private List<Category> toCategoryList(List<Map<String, Object>> mapList){
		List<Category> categoryList = new ArrayList<Category>();
		for(Map<String, Object> map:mapList){
			Category category = toCategory(map);
			categoryList.add(category);
		}
		
		return categoryList;
	}
	
	/**
	 * 返回所有分类
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException{
		/*
		 * 1.查询所有一级分类
		 */
		String sql = "select * from t_category where pid is null order by orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		
		List<Category> parents = toCategoryList(mapList);//所有的一级分类
		
		/*
		 * 2.循环遍历一级分类，加载其子分类
		 */
		for(Category parent : parents){
			//查找所有子分类
			List<Category> children = findByParent(parent.getCid());
			//父类添加子分类
			parent.setChildren(children);
		}
		
		return parents;
	}
	
	/*
	 * 查询所有子分类
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		String sql = "select * from t_category where pid = ?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), pid);
		
		return toCategoryList(mapList);
	}
	
	/**
	 * 添加分类
	 * @param category
	 * @throws SQLException
	 */
	public void add(Category category) throws SQLException{
		/*
		 * 可以添加一级或二级分类
		 */
		String sql ="insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
		String pid = null;//一级分类pid为null
		if(category.getParent() != null){//此时为二级分类
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCid(), category.getCname(), pid, category.getDesc()};
		qr.update(sql, params);
	}
	
	/**
	 * 查询一级分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParent() throws SQLException{
		String sql = "select * from t_category where pid is null order by orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		return toCategoryList(mapList);
	}
	
	/**
	 * 加载分类
	 * 即可加载一级分类，也可以加载二级分类
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public Category load(String cid) throws SQLException{
		String sql = "select * from t_category where cid=?";
		return toCategory(qr.query(sql, new MapHandler(), cid));
	}
	/**
	 * 修改分类
	 * 即可修改一级分类，也可修改二级分类
	 * @param category
	 * @throws SQLException
	 */
	public void edit(Category category) throws SQLException{
		String sql = "update t_category set cname=?, pid=?, `desc`=? where cid=?";
		String pid = null;
		if(category.getParent() != null){//否则可能出现空指针异常
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(), pid, category.getDesc(), category.getCid()};
		qr.update(sql, params);
	}
	/**
	 * 根据父分类查询子分类个数
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	public int findChildCountByParent(String pid) throws SQLException{
		String sql = "select count(*) from t_category where pid=?";
		Number cnt = qr.query(sql, new ScalarHandler(), pid);
		return cnt == null ? 0 : cnt.intValue();
	}
	/**
	 * 删除分类
	 * @param cid
	 * @throws SQLException
	 */
	public void delete(String cid) throws SQLException{
		String sql = "delete from t_category where cid=?";
		qr.update(sql, cid);
	}
	
}
