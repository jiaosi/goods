package cn.fqy.goods.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.order.domain.Order;
import cn.fqy.goods.order.domain.OrderItem;
import cn.fqy.goods.pager.Expression;
import cn.fqy.goods.pager.PageBean;
import cn.fqy.goods.pager.PageConstants;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通用查询得到pageBean
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Order> findByCriteria(List<Expression> exprList, int pc) throws SQLException{
		/*
		 * 1.得到ps分页大小
		 * 2.得到tr总记录数
		 * 3.得到orderList查询的书
		 * 4.创建pageBean返回
		 */
		/*
		 * 1.ps分页大小：
		 */
		int ps = PageConstants.ORDER_PAGE_SIZE;
		/*
		 * 2.生成where子句
		 */
		StringBuilder sb = new StringBuilder(" where 1 = 1");
		List<Object> params = new ArrayList<Object>();
		for(Expression expr:exprList){
			sb.append(" and "+expr.getName()+" "+expr.getOperator()+" ");
			if(!expr.getValue().equals("is null")){
				sb.append("?");
				params.add(expr.getValue());
			}
		}
		/*
		 * 3.查询总记录数
		 */
		String sql = "select count(*) from t_order"+sb;
		Number number = qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		/*
		 * 4.得到orderlist
		 */
		sql = "select * from t_order"+sb+" order by ordertime desc limit ?,?";
		params.add((pc-1)*ps);
		params.add(ps);
		List<Order> beanList = qr.query(sql, 
				new BeanListHandler<Order>(Order.class), params.toArray());
		//虽然获得所有订单，但是没有具体的订单条目
		//循环遍历订单，加载其订单条目
		for(Order order : beanList){
			loadOrderItem(order);
		}
		/*
		 * 5.设置pageBean
		 */
		PageBean<Order> pb = new PageBean<Order>();
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
	}
	/*
	 * 加载指定order的订单条目
	 */
	private void loadOrderItem(Order order) throws SQLException {
		/*
		 *1.执行sql语句
		 *2.得到orderItemList
		 *3.设置给order
		 */
		String sql = "select * from t_orderitem where oid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}
	/*
	 * 将多个map映射为多个orderItem
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String, Object> map : mapList){
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}
	/*
	 * 将一个map映射为一个orderItem
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
	/**
	 * 按用户id查询订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid", "=", uid));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 查询所有
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findAll(int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		return findByCriteria(exprList, pc);
	}
	/**
	 * 按状态查询
	 * @param status
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByStatus(int status, int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status", "=", status+""));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 生成订单(包括了订单条目)
	 * @param order
	 * @throws SQLException 
	 */
	public void add(Order order) throws SQLException{
		/*
		 * 1.sql语句添加订单
		 */
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(), order.getOrdertime(),order.getTotal(), 
						order.getStatus(), order.getAddress(), order.getOwner().getUid()};
		qr.update(sql, params);
		/*
		 * 2.根据订单id添加条目
		 * 条目数来自order
		 */
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();//订单条目数
		Object[][] objs = new Object[len][];//一个是订单条目数，另一个是条目具体参数
		for(int i = 0; i < len; i++){
			OrderItem orderItem = order.getOrderItemList().get(i);
			objs[i] = new Object[]{orderItem.getOrderItemId(), orderItem.getQuantity(),
					orderItem.getSubtotal(), orderItem.getBook().getBid(), 
					orderItem.getBook().getBname(), orderItem.getBook().getCurrPrice(),
					orderItem.getBook().getImage_b(), orderItem.getOrder().getOid()};
		}
		qr.batch(sql, objs);
	}
	
	/**
	 * 加载订单详情
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public Order load(String oid) throws SQLException{
		String sql = "select * from t_order where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
		loadOrderItem(order);
		return order;
	}
	
	/**
	 * 查询指定订单状态
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public int findStatus(String oid) throws SQLException{
		String sql = "select status from t_order where oid=?";
		Number number = qr.query(sql, new ScalarHandler(), oid);
		return number.intValue();
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String oid, int status) throws SQLException{
		String sql = "update t_order set status=? where oid=?";
		qr.update(sql, status, oid);
	}
}
