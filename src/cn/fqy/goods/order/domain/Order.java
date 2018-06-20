package cn.fqy.goods.order.domain;

import java.util.List;

import cn.fqy.goods.user.domain.User;

public class Order {
	private String oid;//主键
	private String ordertime;//下单时间
	private double total;//总计费用
	private int status;//状态：1：未付款。 2：已付款未发货。3：已发货未确认。4：确认收货。5：取消订单
	private String address;//收货地址
	private User owner;//所属用户
	private List<OrderItem> orderItemList;//对应的orderItem，即具体的订单条目
	
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
}
