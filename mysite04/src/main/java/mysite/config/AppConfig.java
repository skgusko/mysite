package mysite.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import mysite.config.app.DBConfig;
import mysite.config.app.MyBatisConfig;

@Configuration 					// 이 클래스는 설정 클래스임
@EnableAspectJAutoProxy 		// AOP 기능 활성화
@EnableTransactionManagement	// 트랜잭션 관리 활성화
@Import({DBConfig.class, MyBatisConfig.class}) // 데이터베이스와 MyBatis 설정 포함
@ComponentScan(basePackages= {"mysite.service", "mysite.repository", "mysite.aspect"}) // 지정된 패키지에서 Bean을 자동으로 스캔하고 등록
public class AppConfig {

}
