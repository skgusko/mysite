package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ModifyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		
		BoardVo boardVo = new BoardDao().findById(id);
		request.setAttribute("boardVo", boardVo);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/modify.jsp"); 
		rd.forward(request, response);
	}

}
