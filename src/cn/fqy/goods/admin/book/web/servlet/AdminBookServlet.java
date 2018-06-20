package cn.fqy.goods.admin.book.web.servlet;



import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.book.service.BookService;
import cn.fqy.goods.category.domain.Category;
import cn.fqy.goods.category.service.CategoryService;
import cn.fqy.goods.pager.PageBean;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	/**
	 * 所有图书分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategoryAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.从service获得所有分类
		 * 2.保存到request，转发到left.jsp
		 */
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	
	
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
		/*
		 * 1.获取pid，得到book对象，保存之
		 */
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		/*
		 * 2.获得所有一级分类，保存之
		 */
		req.setAttribute("parents", categoryService.findParent());
		/*
		 * 3.获得相应一级分类下的二级分类，保存之。
		 */
		String pid = book.getCategory().getParent().getCid();
		req.setAttribute("children", categoryService.findChildren(pid));
		/*
		 * 4.转发到desc.jsp显示
		 */
		return "f:/adminjsps/admin/book/desc.jsp";
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
		
		return "f:/adminjsps/admin/book/list.jsp";
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
			
			return "f:/adminjsps/admin/book/list.jsp";
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
			
			return "f:/adminjsps/admin/book/list.jsp";
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
			
			return "f:/adminjsps/admin/book/list.jsp";
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
			
			return "f:/adminjsps/admin/book/list.jsp";
		}
	/**
	 * 添加图书第一步
	 * 页面显示所有一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获取所有一级分类
		 * 转发到add.jsp显示
		 */
		List<Category> parents = categoryService.findParent();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	/**
	 * ajax查询二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.获取pid
		 * 2.查询pid对应的子分类
		 * 3.将List<Category>转换为json格式
		 * 4.发送到客户端
		 */
		String pid = req.getParameter("pid");
		List<Category> children = categoryService.findChildren(pid);
		String json = tojson(children);
		resp.getWriter().print(json);
		return null;
	}
	
	private String tojson(Category category){
		/*
		 * 将数据转为形如：
		 * {"cid":"4552255","cname":"分类名称"}
		 */
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	/**
	 * 将list转为json数组格式，形如：
	 * [{"cid":"4552255","cname":"分类名称"}, {"cid":"4552255","cname":"分类名称"}]
	 * @param category
	 * @return
	 */
	private String tojson(List<Category> category){
		StringBuilder sb = new StringBuilder("[");
		for(int i = 0; i < category.size(); i++){
			sb.append(tojson(category.get(i)));
			if(i < category.size() - 1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * 修改图书信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.封装表单数据
		 */
		Map map = req.getParameterMap();
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		/*
		 * 2.service修改数据
		 */
		bookService.edit(book);
		/*
		 * 3.返回成功信息到msg.jsp
		 */
		req.setAttribute("msg", "修改图书成功！");
		return "f:/adminjsps/msg.jsp";
	}
	/**
	 * 删除图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String bid = req.getParameter("bid");
		/*
		 * 加载book对象，删除其图片
		 */
		Book book = bookService.load(bid);
		String savePath = this.getServletContext().getRealPath("/");
		new File(savePath, book.getImage_b()).delete();
		new File(savePath, book.getImage_w()).delete();
		
		bookService.delete(bid);
		/*
		 * 保存成功信息，转发到msg.jsp
		 */
		req.setAttribute("msg", "删除图书成功！");
		return "f:/adminjsps/msg.jsp";
	}
	
}
