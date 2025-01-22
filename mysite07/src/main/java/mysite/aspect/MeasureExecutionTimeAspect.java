package mysite.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class MeasureExecutionTimeAspect {
	@Around("execution(* *.repository.*.*(..)) || execution(* *.service.*.*(..)) || execution(* *.controller.*.*(..))")
	public Object adviceAround(ProceedingJoinPoint pjp) throws Throwable { //메서드 정보들 다 담겨옴 
		/* before */
		StopWatch sw = new StopWatch();
		sw.start();
		
		Object result = pjp.proceed(); 
		
		/* after */
		sw.stop();
		long totalTime = sw.getTotalTimeMillis();
		String className = pjp.getTarget().getClass().getName(); //target은 메서드
		String methodName = pjp.getSignature().getName();
		String taskName = className + "." + methodName;
		log.info("[Execution Time][" + taskName + "]" + totalTime + "ms");
		
		return result;
	}
}
