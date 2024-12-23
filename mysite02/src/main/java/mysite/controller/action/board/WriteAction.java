package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");

//		new BoardDao().write(title, content);
//		HttpSession session = request.getSession(true);
//		session.getAttribute("authUser");
		
		//새글쓰기
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(content);
		vo.setHit(0);
		vo.setgNo(1);
		vo.setoNo(1);
		vo.setDepth(0);
		
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");

		if (authUser != null) {
		    Long id = authUser.getId();
		    vo.setUserId(id);

		    System.out.println("User ID: " + id);
		} else {
			System.out.println("세션에 없음");
			
			request.setAttribute("result", "fail");
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/write.jsp");
			rd.forward(request, response);
		}
		
		new BoardDao().write(vo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
