package cn.fqy.goods.book.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.book.service.BookService;
import cn.fqy.goods.pager.PageBean;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	
	/**
	 * 按bid查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		
		return "f:/jsps/book/desc.jsp";
	}
	
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
	
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
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
		 * 3.获取cid,将参数传递给service 得到pageBean
		 */
		String cid = req.getParameter("cid");
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		/*
		 * 4.设置pageBean中的url
		 */
		pb.setUrl(url);
		/*
		 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
		 */
		req.setAttribute("pb", pb);
		
		return "f:/jsps/book/list.jsp";
	}
	
	public String findByPress(HttpServletRequest req, HttpServletResponse resp)
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
			 * 3.获取cid,将参数传递给service 得到pageBean
			 */
			String press = req.getParameter("press");
			PageBean<Book> pb = bookService.findByPress(press, pc);
			/*
			 * 4.设置pageBean中的url
			 */
			pb.setUrl(url);
			/*
			 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
			 */
			req.setAttribute("pb", pb);
			
			return "f:/jsps/book/list.jsp";
		}
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp)
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
			 * 3.获取cid,将参数传递给service 得到pageBean
			 */
			String author = req.getParameter("author");
			PageBean<Book> pb = bookService.findByAuthor(author, pc);
			/*
			 * 4.设置pageBean中的url
			 */
			pb.setUrl(url);
			/*
			 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
			 */
			req.setAttribute("pb", pb);
			
			return "f:/jsps/book/list.jsp";
		}
	public String findByBname(HttpServletRequest req, HttpServletResponse resp)
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
			 * 3.获取cid,将参数传递给service 得到pageBean
			 */
			String bname = req.getParameter("bname");
			PageBean<Book> pb = bookService.findByBname(bname, pc);
			/*
			 * 4.设置pageBean中的url
			 */
			pb.setUrl(url);
			/*
			 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
			 */
			req.setAttribute("pb", pb);
			
			return "f:/jsps/book/list.jsp";
		}
	public String findByCombination(HttpServletRequest req, HttpServletResponse resp)
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
			 * 3.获取cid,将参数传递给service 得到pageBean
			 */
			Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);
			PageBean<Book> pb = bookService.findByCombination(criteria, pc);
			/*
			 * 4.设置pageBean中的url
			 */
			pb.setUrl(url);
			/*
			 * 5.保存pageBean返回至/goods/jsps/book/list.jsp页面
			 */
			req.setAttribute("pb", pb);
			
			return "f:/jsps/book/list.jsp";
		}
}
