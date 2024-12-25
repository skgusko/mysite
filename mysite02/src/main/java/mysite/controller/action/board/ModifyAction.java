package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ModifyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int currentPage = 1;
		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		request.setAttribute("currentPage", currentPage);
		
		Long id = Long.parseLong(request.getParameter("id"));
		String title = request.getParameter("title"); 
		String contents = request.getParameter("content");
		
		BoardVo vo = new BoardVo();
		vo.setId(id);
		vo.setTitle(title);
		vo.setContents(contents);
		
		new BoardDao().modify(vo);
		
		response.sendRedirect("/mysite02/board?a=view&id=" + id);
	}

}
