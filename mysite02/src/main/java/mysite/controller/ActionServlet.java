package mysite.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/ActionServlet")
public abstract class ActionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	// factoryMethod
	protected abstract Action getAction(String actionName);
	
	// operation
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
//		String actionName = Optional.ofNullable(request.getParameter("a")).orElse("");
		Optional<String> optionalActionName = Optional.ofNullable(request.getParameter("a"));
		
//		if (actionName == null) {
//			actionName = "";
//		}
		
		
//		Action action = getAction(optionalActionName.isEmpty() ? "" : optionalActionName.get());
		Action action = getAction(optionalActionName.orElse(""));
		
		action.execute(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public static interface Action {
		void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	}

}
