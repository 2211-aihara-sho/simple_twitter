package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;

//ログインフィルターで追記するコード
@WebFilter(urlPatterns = {"/edit", "/setting" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse)response;
		HttpServletRequest req = (HttpServletRequest)request;
		List<String> errorMessages = new ArrayList<String>();

		User user = (User) req.getSession().getAttribute("loginUser");

		if (user != null) {
			chain.doFilter(request, response);
		} else {
			errorMessages.add("ログインしてください");
			if (errorMessages.size() != 0) {
				/*昔の処理のため2025年5月現在は、研修との差異が発生しているためコメントアウト*/
/*				request.setAttribute("errorMessages", errorMessages);
				request.getRequestDispatcher("/login.jsp").forward(request, res);*/
				HttpSession session = req.getSession();
				session.setAttribute("errorMessages", errorMessages);
				res.sendRedirect("./login");
			}
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}