package cn.fqy.goods.order.web.servlet;



import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fqy.goods.cart.domain.CartItem;
import cn.fqy.goods.cart.service.CartService;
import cn.fqy.goods.order.domain.Order;
import cn.fqy.goods.order.domain.OrderItem;
import cn.fqy.goods.order.service.OrderService;
import cn.fqy.goods.pager.PageBean;
import cn.fqy.goods.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	private CartService cartService = new CartService();
	
	/**
	 * 获得当前页数pc
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		int pc = 1;
		String param = req.getParameter("pc");
		if(param != null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		return pc;
	}
	
	/**
	 * 获取url，来自分页导航条的url
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		/*
		 * 比如完整路径为：http://localhost:8080/goods/jsps/list.jsp?method=findByCategory&cid=1&pc=3
		 * 则req.getRequestURL：/goods/jsps/list.jsp
		 * req.getQueryString:method=findByCategory&cid=1&pc=3
		 */
		String url = req.getRequestURL()+"?"+req.getQueryString();
		int index = url.lastIndexOf("&pc=");//应该是获得&前index
		if(index != -1){//说明存在&pc=字符
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * 我的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
			/*
			 * 1.获取pc，如果没有默认为1
			 */
			int pc = getPc(req);
			/*
			 * 2.获取url
			 */
			String url = getUrl(req);
			/*
			 * 3.从session获取uid,将参数传递给service 得到pageBean
			 */
			User user = (User)req.getSession().getAttribute("sessionUser");
			String uid = user.getUid();
			PageBean<Order> pb = orderService.myOrders(uid, pc);
			/*
			 * 4.设置pageBean中的url
			 */
			pb.setUrl(url);
			/*
			 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
			 */
			req.setAttribute("pb", pb);
			
			return "f:/jsps/order/list.jsp";
		}
	/**
	 * 生成订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String createOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获得order,orderItem的具体数据
		 * order->oid, ordertime, total, status, address, owner, orderItem
		 * orderItem->orderItemId, quantitty, subtotal, book, order
		 */
		/*
		 * 1.获得购物车条目，以此获得quantity, subtotal, book
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartService.loadCartItems(cartItemIds);
		/*
		 * 2.写入order相关参数
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid());//设置oid，订单编号
		order.setOrdertime(String.format("%tF %<tT", new Date()));//设置订购日期，格式化日期
		/*
		 * 从购物车条目获得subtotal再相加
		 */
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList){
			total = total.add(new BigDecimal(cartItem.getSubtotal()+""));
		}
		order.setTotal(total.doubleValue());//设置总计
		order.setStatus(1);//设置状态，1表示未付款
		/*
		 * address来自表单
		 */
		String address = req.getParameter("address");
		order.setAddress(address);//设置收货地址
		User user = (User)req.getSession().getAttribute("sessionUser");
		order.setOwner(user);//设置所属用户
		/*
		 * orderItemList参数的写入
		 */
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(CartItem cartItem : cartItemList){
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());//设置订单条目id，即主键
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);//设置订单包含条目
		
		/*
		 * 3.调用service写入订单
		 */
		orderService.createOrder(order);
		//清空购物车
		cartService.batchDelete(cartItemIds);
		/*
		 * 4.保存order,转发到/jsps/order/ordersucc.jsp
		 */
		req.setAttribute("order", order);
		return "/jsps/order/ordersucc.jsp";
	}
	/**
	 * 加载订单
	 * 需要oid来查询对应订单，btn决定操作方式支付或取消等
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");//从页面传过来的按钮类型，对应不同的操作的方式比如立即支付，取消订单
		req.setAttribute("btn", btn);
		
		return "f:/jsps/order/desc.jsp";
	}
	
	/**
	 * 取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		/*
		 * 查询状态
		 */
		int status = orderService.findStatus(oid);
		if(status != 1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，你的订单不能取消！");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//修改订单状态为5，表示已取消
		req.setAttribute("code", "success");
		req.setAttribute("msg", "你的订单已取消！");
		return "f:/jsps/msg.jsp";
	}
	/**
	 * 确认收货
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		/*
		 * 查询状态
		 */
		int status = orderService.findStatus(oid);
		if(status != 3){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，你的订单不能确定收货！");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 4);//修改订单状态为4，表示已收货
		req.setAttribute("code", "success");
		req.setAttribute("msg", "你的订单已收货！");
		return "f:/jsps/msg.jsp";
	}
	/**
	 * 准备支付
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String paymentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		req.setAttribute("order", orderService.load(req.getParameter("oid")));
		return "f:/jsps/order/pay.jsp";
	}
	
}
