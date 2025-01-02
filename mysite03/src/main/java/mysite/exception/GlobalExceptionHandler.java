package mysite.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public String handler(Exception e) {
		//1. 로깅(loggin)
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors)); //writer쪽에 넣어주면 콘솔이 아닌 outputStream으로 보냄. 메모리 버퍼에 빨때 꽂는 거. PrintWriter은 보조스트림
		System.out.println(errors.toString());
		
		//2. 사과 페이지(종료)
		return "errors/exception";
	}
}
