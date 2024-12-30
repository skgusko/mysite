package mysite.security;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. Handler 종류 확인 (보안처리 대상이 아닌 경우)
		if (!(handler instanceof HandlerMethod)) { 
			// DefaultServletRequestHandler 타입인 경우 
			// DefaultServletHandler가 처리하는 경우(정적자원, /assets/**, mapping이 안 되어 있는 URL)
			return true; // 바로 요청 넘겨줌 
		}
		
		// 2. casting (Object handler로 받았으므로 캐스팅 필요)
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		// 3. Handler에서 @Auth 가져오기 (특정 핸들러 '메서드'에 붙은 @Auth라는 커스텀 어노테이션을 가져오는 코드)
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. Handler Method에서 @Auth가 없으면 '클래스(타입)'에 @Auth 가져오기  (어노테이션 Target 중 TYPE)
		if (auth == null) {
			auth = handlerMethod.getBeanType().getAnnotation(Auth.class);
		}
		
		// 5. @Auth 어노테이션이 없는 경우 (Target이 METHOD, TYPE인 어노테이션이 둘 다 없는 경우)
		if (auth == null) {  
			return true; 
		}

		// 6. @Auth가 붙어 있기 때문에 인증(Authentication) 여부 확인 
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		if (authUser == null) { //로그인 안 되어있는 경우 (인증되지 않은 유저의 접근)
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		// 7. ADMIN 요청에 대한 권한 확인
		String requiredRole = auth.role(); // 어노테이션에 정의된 권한
		String userRole = authUser.getRole(); // 세션에 저장된 유저의 role

		if ("ADMIN".equals(requiredRole)) {
		    if (!"ADMIN".equals(userRole)) { // USER인 경우
		        response.sendRedirect(request.getContextPath());
		        return false;
		    }
		}
		
		// 8. @Auth가 붙어있고 인증도 된 경우 
		return true;
	}

}
