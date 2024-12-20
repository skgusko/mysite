package mysite.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import java.io.IOException;

//@WebFilter("/**") //web.xml에서 추가할 것임
public class EncodingFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	
	private String encoding;

	public void init(FilterConfig fConfig) throws ServletException {
		encoding = fConfig.getInitParameter("encoding");
		if (encoding == null) {
			encoding = "utf-8";
		}
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/* request */
		System.out.println("EncodingFilter.doFilter() called: request processing");
		
		request.setCharacterEncoding(encoding);
		
		chain.doFilter(request, response); // 서블릿 실행하고 옴
		
		/* response */
		System.out.println("EncodingFilter.doFilter() called: response processing");
	}

	public void destroy() {
	}

}
