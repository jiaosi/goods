package cn.fqy.goods.cart.web.servlet;



import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.cart.domain.CartItem;
import cn.fqy.goods.cart.service.CartService;
import cn.fqy.goods.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class CartServlet extends BaseServlet {
	private CartService cartService = new CartService();
	
	/**
	 * 我的购物车情况
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException{
		/*
		 * 1.获得uid
		 */
		User user = (User) req.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		/*
		 * 2.调用service 得到购物车信息
		 */
		List<CartItem> cartItemList = cartService.myCart(uid);
		/*
		 * 3.保存购物车信息,返回至/goods/jsps/cart.jsp
		 */
		req.setAttribute("cartItemList", cartItemList);
		
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 往购物车添加图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.封装数据到cartItem
		 * 从表单获取的数据（bid, quantity）
		 */
		Map map = req.getParameterMap();
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = (User)req.getSession().getAttribute("sessionUser");
		cartItem.setBook(book);
		cartItem.setUser(user);
		/*
		 * 2.service新增书目
		 */
		cartService.addCartItem(cartItem);
		
		return myCart(req, resp);
	}
	
	/**
	 * 批量删除购物车删除条目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获取删除条目id：cartItemIds
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		/*
		 * 调用service对应删除
		 */
		cartService.batchDelete(cartItemIds);
		/*
		 * 返回我的购物车
		 */
		return myCart(req, resp);
	}
	
	/**
	 * 修改购买数量
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获得参数cartItemId,quantity
		 */
		String cartItemId = req.getParameter("cartItemId");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		/*
		 * 调用service修改数量，得到cartItem对象,用于计算小计
		 */
		CartItem cartItem = cartService.updateQuantity(cartItemId, quantity);
		/*
		 * 构造json数据返回到客户端,由ajax解析
		 */
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		
		resp.getWriter().print(sb);
		
		return null;
	}
	
	/**
	 * 加载结算书籍
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获得参数：结算条目的id,以及总价：total
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		double total = Double.parseDouble(req.getParameter("total"));
		/*
		 * 调用service获得cartItemList书目集
		 */
		List<CartItem> cartItemList = cartService.loadCartItems(cartItemIds);
		/*
		 * 保存书目集，转发到/jsps/cart/showitem.jsp
		 */
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemIds);
		
		return "f:/jsps/cart/showitem.jsp";
	}
	
}
