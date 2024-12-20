package mysite.controller;

import jakarta.servlet.ServletException;
import mysite.controller.action.main.MainAction;

// @WebServlet({"/main", ""})
public class MainServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException { // 서블릿의 LifeCycle 중 init 부분을 오버라이드 
		String config = getServletConfig().getInitParameter("config");
		System.out.println("MainController.init() called: " + config);
		super.init();
	}

	@Override
	protected Action getAction(String actionName) {
		return new MainAction();
	}
}
