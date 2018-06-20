package cn.fqy.goods.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.book.service.BookService;
import cn.fqy.goods.category.domain.Category;
import cn.itcast.commons.CommonUtils;

public class AdminAddBookServlet extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 上传三部曲
		 */
		//1.建立工厂
		FileItemFactory factory = new DiskFileItemFactory();
		//2.构建解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(80*1024);//设置最大上传文件大小为80kb
		//3.解析request获取List<FileItem>(所有数据)
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			//图片大小超出所给上限，保存错误信息，返回
			error("上传的文件超过80kb", request, response);
			return;
		}
		/*
		 * 4.将List<FileItem>映射为map，封装到book
		 */
		//4.1封装一般表单数据
		Map<String, Object> map = new HashMap<String, Object>();
		for(FileItem fileItem : fileItemList){
			if(fileItem.isFormField()){//是普通数据项
				map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
			}
		}
		Book book = CommonUtils.toBean(map, Book.class);//封装大部分表单数据
		Category category = CommonUtils.toBean(map, Category.class);//关联cid
		book.setCategory(category);
		/*
		 * 4.2把上传的图片保存
		 * >获取图片名，截取之
		 * >使用uuid编码，防止重复
		 * >校验扩展名，只能是.jpg
		 * >校验尺寸
		 * >指定图片保存路径，使用servletContext#getRealPath()
		 * >设置给book对象
		 */
		FileItem fileItem = fileItemList.get(1);//获取的是大图
		String filename = fileItem.getName();
		int index = filename.lastIndexOf("\\");
		if(index != -1){
			filename = filename.substring(index + 1); 
		}
		//加上uuid前缀防止重复
		filename = CommonUtils.uuid()+"_"+filename;
		//校验是否为jpg格式
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("你的图片格式不是jpg格式！", request, response);
			return;
		}
		/*
		 * 校验尺寸首先要保存图片，
		 * 然后new一个image对象
		 */
		//获得要保存的真实路径
		String savePath = this.getServletContext().getRealPath("/book_img");
		//创建目标文件对象
		File destFile = new File(savePath, filename);
		//把上传的图片写入创建的文件对象
		try {
			fileItem.write(destFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//创建imageicon对象
		ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
		//得到image对象
		Image image = icon.getImage();
		//校验图片尺寸
		if(image.getWidth(null) > 350 || image.getHeight(null) >350){
			error("你的图片尺寸超过350*350!", request, response);
			destFile.delete();//不符合要求的图片应该给删掉
			return;
		}
		//将其设置给book对象
		book.setImage_w("book_img/"+filename);
		
		
		
		
		
		
		/*
		 * 小图片
		 */
		fileItem = fileItemList.get(2);//获取的是小图
		filename = fileItem.getName();
		index = filename.lastIndexOf("\\");
		if(index != -1){
			filename = filename.substring(index + 1); 
		}
		//加上uuid前缀防止重复
		filename = CommonUtils.uuid()+"_"+filename;
		//校验是否为jpg格式
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("你的图片格式不是jpg格式！", request, response);
			return;
		}
		/*
		 * 校验尺寸首先要保存图片，
		 * 然后new一个image对象
		 */
		//获得要保存的真实路径
		savePath = this.getServletContext().getRealPath("/book_img");
		//创建目标文件对象
		destFile = new File(savePath, filename);
		//把上传的图片写入创建的文件对象
		try {
			fileItem.write(destFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//创建imageicon对象
		icon = new ImageIcon(destFile.getAbsolutePath());
		//得到image对象
		image = icon.getImage();
		//校验图片尺寸
		if(image.getWidth(null) > 350 || image.getHeight(null) >350){
			error("你的图片尺寸超过350*350!", request, response);
			destFile.delete();//不符合要求的图片应该给删掉
			return;
		}
		//将其设置给book对象
		book.setImage_b("book_img/"+filename);
		
		//调用service完成添加
		book.setBid(CommonUtils.uuid());
		BookService bookService = new BookService();
		bookService.add(book);
		
		//保存成功信息
		request.setAttribute("msg", "图书添加成功！");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
	}
	
	private void error(String msg, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
	}

}
