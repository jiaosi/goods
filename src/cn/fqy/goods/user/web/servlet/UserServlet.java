package cn.fqy.goods.user.web.servlet;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.fqy.goods.user.domain.User;
import cn.fqy.goods.user.service.UserException;
import cn.fqy.goods.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

/**
 * 用户模块WEB层
 * 
 * 注意及问题：
 * 1.req.getParameterMap()可以将页面中input中name 和 value获取
 * 2.转发加 f: 
 * 3.有私有方法
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	
	/*
	 * ajax验证用户是否注册
	 */
	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException,IOException, SQLException{
		String loginname = req.getParameter("loginname");
		boolean b = userService.ajaxValidateLoginname(loginname);
		//发送到客户端
		resp.getWriter().print(b);//ajax中有个success的参数为result
		return null;
	}
	/*
	 * ajax验证邮箱是否注册
	 */
	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException,IOException, SQLException{
		String email = req.getParameter("email");
		boolean b = userService.ajaxValidateEmail(email);
		resp.getWriter().print(b);
		return null;
	}
	/*
	 * ajax验证验证码是否正确
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException{
		//1.获取用户输入的验证码
		String verifyCode = req.getParameter("verifyCode");
		//2.获得正确的验证码(在创建验证码的时候设置了这个属性)
		String vCode = (String)req.getSession().getAttribute("vCode");
		//3.判断验证码是否正确
		boolean b = verifyCode.equalsIgnoreCase(vCode);
		//4.发送到客户端
		resp.getWriter().print(b);
		return null;
	}
	/*
	 * 注册
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException,IOException{
		
		
		/*
		 * 1.得到表单数据保存到bean中(User )(注意导包)
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		
		/*
		 * 2.校验之，如果失败，保存错误信息返回到msg.jsp
		 */
		Map<String,String> errors = validateRegist(formUser, req.getSession());
		if(errors.size()>0){
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";//注册失败，返回注册页
		}
		/*
		 * 3.service完成业务(将注册信息写入数据库，是在上一步成功的基础上)
		 */
		userService.regist(formUser);
		/*
		 * 4.返回成功信息至msg.jsp（c标签）
		 */
		req.setAttribute("code", "success");
		req.setAttribute("msg", "成功");
		return "f:/jsps/msg.jsp";//转发
		
	}
	/*
	 * 用于上一个方法regist中第二步
	 * 服务器端验证注册信息
	 * Map保存注册的错误信息
	 */
	private Map<String, String> validateRegist(User formUser, HttpSession session){
		Map<String,String> errors = new HashMap<String,String>();
		
		/*
		 * 验证登录名
		 */
		String loginname = formUser.getLoginname();
		if(loginname==null || loginname.trim().isEmpty()){
			errors.put("loginname", "用户名不能为空");
		}else if(loginname.length()<3 || loginname.length()>20 ){
			errors.put("loginname", "用户名长度应在3-20之间");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "此用户名已存在");
		}
		
		/*
		 * 验证密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass==null || loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空");
		}else if(loginpass.length()<3 || loginpass.length()>20 ){
			errors.put("loginpass", "密码长度应在3-20之间");
		}
		
		/*
		 * 验证确认密码
		 */
		String reloginpass = formUser.getReloginpass();
		if(!reloginpass.equals(loginpass)){
			errors.put("reloginpass", "两次密码输入不一致");
		}
		
		/*
		 * 验证Email
		 */
		String email = formUser.getEmail();
		if(email==null || email.trim().isEmpty()){
			errors.put("email", "邮箱不能为空");
		}else if(!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
			errors.put("email", "邮箱格式不正确");
		}else if(!userService.ajaxValidateEmail(email)){
			errors.put("email", "此邮箱已被注册");
		}
		
		/*
		 * 验证验证码
		 * 需要添加HttpSession 参数
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String)session.getAttribute("vCode");
		if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode", "验证码不正确");
		}
		
		return errors;
	}
	/*
	 * 激活码链接
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException{
		System.out.println("activation()..");
		return null;
	}
	
	/*
	 * 登录
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws UnsupportedEncodingException, ServletException, IOException{
		/*
		 * 1.封装表单数据
		 * 2.校验数据
		 * 3.用户是否存在，若不存才：
		 * 	*保存错误信息
		 * 	*保存登录信息，为了回显
		 * 	*返回login.jsp
		 * 4.若存在，判断是否激活，若未激活：
		 * 	*保存错误信息
		 * 	*保存登录信息，为了回显
		 * 	*返回login.jsp
		 * 5.登录成功：
		 * 	*保存session
		 * 	*将数据存入cookie（注意转码）
		 * 	*回到index.jsp
		 */
		//封装登录数据
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		
		//校验数据
		Map<String, String> errors = validateLogin(formUser, req.getSession());
		if(errors.size() > 0){
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";//登录失败，返回注册页
		}
		//判断用户是否存在
		User user = userService.login(formUser);
		if(user == null){//用户不存在
			req.setAttribute("msg", "登录名或密码错误！");
			req.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}else{
			if(!user.isStatus()){//状态未激活
				req.setAttribute("msg", "用户未激活！");
				req.setAttribute("form", formUser);
				return "f:/jsps/user/login.jsp";
			}else{
				//登录成功
				req.getSession().setAttribute("sessionUser", user);//注意，此处为数据库得到的user信息
				String loginname = URLEncoder.encode(user.getLoginname(),"utf-8");
				Cookie cookie = new Cookie("loginname", loginname);
				cookie.setMaxAge(60*60*24*10);//十天
				resp.addCookie(cookie);
				return "r:/index.jsp";//重定向
			}
		}
		
	}
	
	/*
	 * 验证登录，具体操作自行补充
	 */
	private Map<String, String> validateLogin(User formUser, HttpSession session){
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 验证验证码
		 * 需要添加HttpSession 参数
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String)session.getAttribute("vCode");
		if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode", "验证码不正确");
		}
		return errors;
	}
	
	/*
	 * 修改密码
	 */
	public String updatePassword(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException{
		/*
		 * 封装数据
		 * 获取session的user
		 * service修改密码
		 */
		/*
		 * 1.封装数据
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
//		/*
//		 * 2.校验数据
//		 */
//		Map<String, String> errors = validateUpdatePassword(formUser, req.getSession());
//		if(errors.size() != 0){
//			return "f:/jsps/user/pwd.jsp";
//		}
		/*
		 * 3.从session获得user
		 */
		User user = (User)req.getSession().getAttribute("sessionUser");
		if(user == null){//未登录
			req.setAttribute("msg", "未登录");
			return "f:/jsps/user/login.jsp";
		}
		/*
		 * 3.从session获取用户，并将其和userForm传给service
		 */
		try {
			userService.updatePassword(user.getUid(), formUser.getLoginpass(), 
					formUser.getNewloginpass());//注意此处uid来自session的user
			req.setAttribute("msg", "修改成功！");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			// TODO Auto-generated catch block
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("user", formUser);//回显
			return "f:/jsps/user/pwd.jsp";
		}
		
		
	}
	/*
	 * 验证修改密码，具体操作自行补充
	 */
	private Map<String, String> validateUpdatePassword(User formUser, HttpSession session){
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 验证原密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass==null || loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空");
		}else if(loginpass.length()<3 || loginpass.length()>20 ){
			errors.put("loginpass", "密码长度应在3-20之间");
		}
		
		
		return errors;
	}
	
	/*
	 * 退出
	 */
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
}












