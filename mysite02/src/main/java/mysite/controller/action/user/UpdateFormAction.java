package mysite.controller.action.user;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.controller.ActionServlet.Action;
import mysite.dao.UserDao;
import mysite.vo.UserVo;

public class UpdateFormAction implements Action {

	// Access Control
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session == null) { //로그인 안 하고 들어온 경우
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		//////////////////////////////////////////////////////////////
		
		// 회원정보 수정 (과제)
//		UserVo vo = new UserDao().findById(authUser.getId()); //pw 가져올필요 없음
//		request.setAttribute("vo", vo); //jsp에 보내기
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/updateform.jsp");
		rd.forward(request, response);

	}
	
	

}
