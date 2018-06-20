package cn.fqy.goods.admin.order.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fqy.goods.order.domain.Order;
import cn.fqy.goods.order.service.OrderService;
import cn.fqy.goods.pager.PageBean;
import cn.fqy.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
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
	 * 查询所有订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
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
			PageBean<Order> pb = orderService.findAll(pc);
			/*
			 * 4.设置pageBean中的url
			 */
			pb.setUrl(url);
			/*
			 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
			 */
			req.setAttribute("pb", pb);
			
			return "f:/adminjsps/admin/order/list.jsp";
		}
	
	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
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
			 * 3.从链接获得status,将参数传递给service 得到pageBean
			 */
			int status = Integer.parseInt(req.getParameter("status"));
			PageBean<Order> pb = orderService.findByStatus(status, pc);
			/*
			 * 4.设置pageBean中的url
			 */
			pb.setUrl(url);
			/*
			 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
			 */
			req.setAttribute("pb", pb);
			
			return "f:/adminjsps/admin/order/list.jsp";
		}
	/**
	 * 显示订单详细信息
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
		
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	/**
	 * 取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	/**
	 * 发货
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		/*
		 * 查询状态
		 */
		int status = orderService.findStatus(oid);
		if(status != 2){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，你的订单不能发货！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);//修改订单状态为3，表示已发货
		req.setAttribute("code", "success");
		req.setAttribute("msg", "你的订单已发货！注意查看物流信息。");
		return "f:/adminjsps/msg.jsp";
	}
	/**
	 * 取消
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
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//修改订单状态为5，表示已取消
		req.setAttribute("code", "success");
		req.setAttribute("msg", "你的订单已取消！");
		return "f:/adminjsps/msg.jsp";
	}
}
