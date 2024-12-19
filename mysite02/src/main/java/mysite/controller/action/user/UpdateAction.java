package mysite.controller.action.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.UserDao;
import mysite.vo.UserVo;

public class UpdateAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String gender = request.getParameter("gender");
		
		UserVo vo = new UserVo();
		vo.setName(name);
		vo.setEmail(email);
		vo.setPassword(password);
		vo.setGender(gender);
		
		System.out.println("UpdateAction : " + vo);
		
		if ("".equals(vo.getPassword())) { // 패스워드 포함 변경 x
			new UserDao().updateNameAndGender(vo);
		} else { 
			new UserDao().updateNameAndPasswordAndGender(vo);
		}
		
		response.sendRedirect(request.getContextPath() + "/user?a=updateform");
	}

}
