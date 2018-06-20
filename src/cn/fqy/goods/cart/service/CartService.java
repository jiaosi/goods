package cn.fqy.goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.fqy.goods.cart.dao.CartDao;
import cn.fqy.goods.cart.domain.CartItem;
import cn.itcast.commons.CommonUtils;

public class CartService {
	private CartDao cartDao = new CartDao();
	
	/**
	 * 我的购物车
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid){
		try {
			return cartDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加书籍条目
	 * @param cartItem
	 */
	public void addCartItem(CartItem cartItem){
		/*
		 * 查询书籍是否已经加入购物车
		 * 	如果没有，则添加书籍
		 * 	如果有，那么修改数量
		 */
		try{
			CartItem _cartItem = cartDao.findByBidAndUid(//来自数据库中的条目
					cartItem.getBook().getBid(), cartItem.getUser().getUid());
			/*
			 * 条目为空，说明没有这本书，可以添加
			 */
			if(_cartItem == null){
				cartItem.setCartItemId(CommonUtils.uuid());//新建书籍条目id
				cartDao.addCartItem(cartItem);
			}else{
				/*
				 * 已存在这本书，那么修改数量
				 */
				int quantity = _cartItem.getQuantity() + cartItem.getQuantity();
				cartDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 批量删除
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds){
		try {
			cartDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改购买数量
	 * @param cartItemId
	 * @param quantity
	 * @return
	 */
	public CartItem updateQuantity(String cartItemId, int quantity){
		try {
			cartDao.updateQuantity(cartItemId, quantity);
			return cartDao.findByCartItemId(cartItemId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询准备结算的书目
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
