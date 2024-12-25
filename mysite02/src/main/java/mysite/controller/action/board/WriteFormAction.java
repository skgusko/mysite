package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class WriteFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String idParam = request.getParameter("id"); //답글일 경우에만 id를 파라미터로 받음 (부모글 id)
		
		if (idParam != null) { //답글인 경우 
			Long parentId = Long.parseLong(idParam);
			BoardVo vo = new BoardDao().findForReply(parentId);
			
			request.setAttribute("vo", vo);
//			request.setAttribute("g_no", vo.getgNo());
//			request.setAttribute("o_no", vo.getoNo());
//			request.setAttribute("depth", vo.getDepth());
//			request.setAttribute("title", vo.getTitle());
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/write.jsp"); 
		rd.forward(request, response);
	}

}
