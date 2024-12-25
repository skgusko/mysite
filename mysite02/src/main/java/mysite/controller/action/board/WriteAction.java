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
		
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("authUser") == null) {
		    // 세션이 없거나 회원가입하지 않은 경우 
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
			rd.forward(request, response);
			
		    return;
		}

		UserVo authUser = (UserVo)session.getAttribute("authUser");
	    Long userId = authUser.getId();
	    
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(content);
		vo.setHit(0);
		vo.setUserId(userId);
		
		BoardDao dao = new BoardDao();
		
		if (request.getParameter("g_no") == null) { //새글쓰기인 경우
			int newgNo = dao.findgNo();
			vo.setgNo(newgNo);
			vo.setoNo(1);
			vo.setDepth(0);
		} else {
			// 답글 
			int gNo = Integer.parseInt(request.getParameter("g_no")); //부모글의 g_no (게시글 그룹 번호)
			int oNo = Integer.parseInt(request.getParameter("o_no")); //부모글의 o_no (그룹 내 정렬 순서)
			int depth = Integer.parseInt(request.getParameter("depth")); //부모글의 depth
			
			// 기존 글의 o_no 값 증가
	        dao.updateOrderNo(gNo, oNo, depth);
	        
			vo.setgNo(gNo);
			vo.setoNo(oNo + 1);
			vo.setDepth(depth + 1);
		}
		
		dao.write(vo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
