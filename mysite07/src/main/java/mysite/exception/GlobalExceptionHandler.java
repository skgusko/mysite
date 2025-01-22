package mysite.exception;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mysite.dto.JsonResult;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public void handler(
			HttpServletRequest request, //accept 보기 위해
			HttpServletResponse response, //직접 응답 해주기 위해 
			Exception e) throws Exception {
		
		// 1. 로깅(logging)
		StringWriter errors = new StringWriter(); // 콘솔 출력값을 프로그램 내에서 다루기 위해 사용 
		e.printStackTrace(new PrintWriter(errors)); //writer쪽에 넣어주면 콘솔이 아닌 outputStream으로 보냄. 메모리 버퍼에 빨때 꽂는 거. PrintWriter은 보조스트림
		log.error(errors.toString());
		
		// 2. 요청 구분
		// 	  json 요청: request header의 accept: application/json (o)
		//    html 요청: request header의 accept: application/json (x) 
		String accept = request.getHeader("accept");
		
		// 3. JSON 응답
		if(accept.matches(".*application/json.*")) {
			
			JsonResult jsonResult = JsonResult.fail(e instanceof NoHandlerFoundException ? "Unknown API URL" : errors.toString());
			String jsonString = new ObjectMapper().writeValueAsString(jsonResult);
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json; charset=utf-8");
			OutputStream os = response.getOutputStream();
			os.write(jsonString.getBytes("utf-8"));
			os.close();
			
			return;
		} 
		// 4. HTML 응답: 사과 페이지(종료)
		// 클라이언트가 잘못된 URL로 요청을 보냈을 때, 즉 매핑된 핸들러(컨트롤러)가 없을 경우 발생
		if (e instanceof NoHandlerFoundException || e instanceof NoResourceFoundException) {
			request
				.getRequestDispatcher("/error/404")
				.forward(request, response);
		} else {
			request.setAttribute("errors", errors.toString());
			request
				.getRequestDispatcher("/error/500")
				.forward(request, response);
		}
	}
}