package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int currentPage = 1;
		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		request.setAttribute("currentPage", currentPage);
		
		Long id = Long.parseLong(request.getParameter("id"));
		
		BoardDao dao = new BoardDao();
		
		dao.updateViews(id);
		
		BoardVo vo = dao.findById(id);
		request.setAttribute("vo", vo);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/view.jsp"); //이 JSP로 넘길거야
		rd.forward(request, response);
	}
}