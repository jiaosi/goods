package cn.fqy.goods.admin.category.web.servlet;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fqy.goods.book.service.BookService;
import cn.fqy.goods.category.domain.Category;
import cn.fqy.goods.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService= new CategoryService();
	private BookService bookService = new BookService();
	
	/**
	 * 查询所有分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		req.setAttribute("parent", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 封装表单数据，调用service添加，返回list.jsp
		 */
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(req, resp);
	}
	/**
	 * 添加二级分类准备
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.获取cid
		 * 2.获得所有一级分类
		 * 3.保存cid,一级分类,转发到add2.jsp
		 */
		String cid = req.getParameter("cid");
		List<Category> parents = categoryService.findParent();
		req.setAttribute("cid", cid);
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	/**
	 * 添加二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * child->cid, cname, pid, desc
		 * 1.封装表单数据，由于是二级分类pid需要手动映射
		 * 2.调用service添加二级分类
		 * 3.调用findAll，返回list.jsp显示
		 */
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);//设置了cname, desc
		child.setCid(CommonUtils.uuid());//设置了cid
		/*
		 * 手动为child添加parent项（pid）
		 */
		Category parent = new Category();
		String pid = req.getParameter("pid");
		parent.setCid(pid);
		child.setParent(parent);//设置了pid
		/*
		 * 调用service添加二级分类
		 */
		categoryService.add(child);
		
		return findAll(req, resp);
	}
	/**
	 * 修改一级分类准备
	 * 为了在edit.jsp上显示要修改的分类信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获得cid以此获得相应的category，保存转发到edit.jsp
		 */
		String cid = req.getParameter("cid");
		Category parent = categoryService.load(cid);
		req.setAttribute("parent", parent);
		
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	/**
	 * 修改一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 修改一级分类：
		 * 	1.封装表单数据
		 * 	2.完善数据项，由于是一级分类，pid项的确为null
		 * 	3.调用service修改
		 * 	4.返回到findAll()
		 */
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		categoryService.edit(category);
		return findAll(req, resp);
	}
	/**
	 * 修改二级分类准备
	 * 有表单回显
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 从链接获得cid，
		 * 1.查询cid对应的二级分类
		 * 2。查询所有一级分类，为了表单显示
		 * 3.保存对应的二级分类，以及所有一级分类,还有一级分类cid，为了表单下拉框选中
		 * 4.转发到edit2.jsp
		 */
		String cid = req.getParameter("cid");
		Category child = categoryService.load(cid);
		List<Category> parents = categoryService.findParent();//加载所有父分类
		
		req.setAttribute("child", child);
		req.setAttribute("parents", parents);
		String parentCid = req.getParameter("parentCid");
		req.setAttribute("parentCid", parentCid);
		
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	/**
	 * 修改二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.封装表单数据
		 * 2.设置pid
		 * 3.service完成修改
		 * 4.转发到list.jsp  return findAll();
		 */
		/*
		 * 1.封装表单数据
		 */
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		/*
		 * 2.获得pid，并设置给二级分类
		 */
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);//给二级分类设置一级分类
		/*
		 * 3.调用service完成修改
		 */
		categoryService.edit(child);
		/*
		 * 4.返回到list.jsp
		 */
		return findAll(req, resp);
	}
	/**
	 * 删除一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获得pid(为一级分类的cid)，判断是否含有子分类
		 * >如果有：保存错误信息，返回msg.jsp
		 * >没有子分类：service执行删除，返回list.jsp
		 */
		String cid = req.getParameter("cid");
		int cnt = categoryService.findChildrenCountByParent(cid);
		if(cnt > 0){
			req.setAttribute("msg", "此分类下含有二级分类，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}else{
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	/**
	 * 删除二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 获得cid，判断此二级分类下是否存在图书
		 * >如果存在：保存错误信息，转发到msg.jsp
		 * >不存在：删除此二级分类，返回list.jsp
		 */
		String cid = req.getParameter("cid");
		int cnt = bookService.findBookCountByCategory(cid);
		if(cnt > 0){
			req.setAttribute("msg", "此分类下存在图书，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}else{
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	
}
