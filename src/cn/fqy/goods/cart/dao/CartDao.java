package cn.fqy.goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.cart.domain.CartItem;
import cn.fqy.goods.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CartDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 把一个map映射为cartItem
	 * @param map
	 * @return
	 */
	private CartItem toCartItem(Map<String, Object> map){
		if(map == null || map.size() == 0) return null;
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
	
	/**
	 * 将多个map映射为多个cartItem
	 * @param mapList
	 * @return
	 */
	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList){
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for(Map<String, Object> map:mapList){
			CartItem cartItem = toCartItem(map);
			cartItemList.add(cartItem);
		}
		return cartItemList;
	}
	
	/**
	 * 根据用户id查询购物车条目及书籍信息
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		String sql ="select * from t_cartitem c, t_book b where c.bid=b.bid and uid=? order by c.orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), uid);
		return toCartItemList(mapList);
	}
	
	/**
	 * 查询购物车书籍条目是否已存在
	 * @param bid
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByBidAndUid(String bid, String uid) throws SQLException{
		String sql = "select * from t_cartitem where bid=? and uid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), bid, uid);
		return toCartItem(map);
	}
	
	/**
	 * 修改订购书籍数量
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId, int quantity) throws SQLException{
		String sql = "update t_cartitem set quantity=? where cartItemId=?";
		qr.update(sql, quantity, cartItemId);
	}
	
	/**
	 * 新增书籍订购条目
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem) throws SQLException{
		String sql = "insert into t_cartitem(cartItemId, quantity, bid, uid) values(?,?,?,?)";
		qr.update(sql, cartItem.getCartItemId(), cartItem.getQuantity(), 
				cartItem.getBook().getBid(), cartItem.getUser().getUid());
	}
	
	/**
	 * 批量删除的where子句ex:where cartItemId in(?,?,?);
	 */
	private String toWhereSql(int len){
		StringBuilder sb = new StringBuilder(" cartItemId in(");
		for(int i = 0; i < len; i++){
			sb.append("?");
			if(i < len - 1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 批量删除和删除
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelete(String cartItemIds) throws SQLException{
		Object[] cartItemIdsArray = cartItemIds.split(",");
		int len = cartItemIdsArray.length;
		String sql = "delete from t_cartitem where"+toWhereSql(len);
		qr.update(sql, cartItemIdsArray);//注意必须为object类型，如果为string类型则不会分割。
	}
	
	/**
	 * 按条目id查询书籍条目
	 * @param cartItemId
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "select * from t_cartitem c, t_book b where c.bid=b.bid and c.cartItemId=?";
		Map map = qr.query(sql, new MapHandler(), cartItemId);
		return toCartItem(map);
	}
	
	/**
	 * 根据选中条目的id查询最终准备结算的书目。
	 * @param cartItemId
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		Object[] cartItemArray = cartItemIds.split(",");
		int len = cartItemArray.length;
		String sql = "select * from t_cartitem c, t_book b where c.bid=b.bid and"+toWhereSql(len);
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), cartItemArray);
		return toCartItemList(mapList);
	}
}
