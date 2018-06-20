package cn.fqy.goods.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.fqy.goods.user.domain.User;

/**
 * 登录过滤器
 * @author acer
 *
 */
public class LoginFilter implements Filter {

    public LoginFilter() {
        
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException {
		/*
		 * 从session获得user， 判断是否为空
		 * >为空，保存错误信息。转发到msg.jsp
		 * >不为空，放行
		 */
		/*
		 * 1.获得session
		 */
		HttpServletRequest req = (HttpServletRequest) request;//强转类型
		Object user = req.getSession().getAttribute("sessionUser");
		if(user == null){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "请先登录！");
			req.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
		}else{
			chain.doFilter(request, response);
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException { 
	}

}
