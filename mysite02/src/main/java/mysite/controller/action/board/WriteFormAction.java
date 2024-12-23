package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;

public class WriteFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String gNo = request.getParameter("g_no");
		String oNo = request.getParameter("o_no");
		String depth = request.getParameter("depth");
		
		request.setAttribute("g_no", gNo);
		request.setAttribute("o_no", oNo);
		request.setAttribute("depth", depth);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/write.jsp"); 
		rd.forward(request, response);
	}

}
