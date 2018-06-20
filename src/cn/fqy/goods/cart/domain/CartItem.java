package cn.fqy.goods.cart.domain;

import java.math.BigDecimal;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.user.domain.User;

public class CartItem {
	private String cartItemId;//主键
	private int quantity;//数量
	private Book book;//购物车中条目对应的书
	private User user;//购物车所属用户
	
	/**
	 * 获得小计。书籍数量与价格的乘积
	 * @return
	 */
	public double getSubtotal(){
		/*
		 * bigDecimal不会有误差
		 * 而且必须用String类型的构造器
		 */
		BigDecimal d1 = new BigDecimal(book.getCurrPrice()+"");
		BigDecimal d2 = new BigDecimal(quantity+"");
		BigDecimal d3 = d1.multiply(d2);//乘
		return d3.doubleValue();
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
