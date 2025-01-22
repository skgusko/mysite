package mysite.config;

import java.io.IOException;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.repository.UserRepository;
import mysite.security.UserDetailsServiceImpl;

@SpringBootConfiguration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

	//필터 타는 놈이 아닌 경우 여기서 설정
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
		return webSecurity -> webSecurity.httpFirewall(new DefaultHttpFirewall());
    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())  
				// csrf 없으면 post로 들어올 때 forbidden 에러 떠서 disable 해놓음 
				// 아니면 csrf값 같이 hidden으로 넘겨줘서 해결 가능 
			.formLogin((formLogin) -> {
				formLogin
					.loginPage("/user/login")
					.loginProcessingUrl("/user/auth") //UsernamePasswordAuthenticationFilter가 이 url 감시 
					.usernameParameter("email")
					.passwordParameter("password")
					.defaultSuccessUrl("/")
//					.failureUrl("/user/login?result=fail");
					.failureHandler(new AuthenticationFailureHandler() {

						@Override
						public void onAuthenticationFailure(
								HttpServletRequest request, 
								HttpServletResponse response,
								AuthenticationException exception) throws IOException, ServletException {
							request.setAttribute("email", request.getParameter("email"));
							request.setAttribute("result", "fail");
							request
								.getRequestDispatcher("/user/login")
								.forward(request, response);
						}
						
					});
			})
			.logout(logout -> {
				logout
					.logoutUrl("/user/logout")
					.logoutSuccessUrl("/");
			})
			.authorizeHttpRequests((authorizeRequests) -> {
				/* ACL */
				authorizeRequests
					.requestMatchers(new RegexRequestMatcher("^/admin/?.*$", null))
					//.authenticated() //우리 서비스의 사용자인지를 확인 
					.hasAnyRole("ADMIN") //사용자이지만, role이 ADMIN인지 USER인지 
				
					.requestMatchers(new RegexRequestMatcher("^/user/update$", null))
					.hasAnyRole("ADMIN", "USER")
					
					.requestMatchers(new RegexRequestMatcher("^/board/?(write|modify|delete|reply)$", null))
					.hasAnyRole("ADMIN", "USER") //.authenticated() 로 대체 가능 
					
					.anyRequest()
					.permitAll();
			});
			/*
			.exceptionHandling(exceptionHandling -> { // USER가 /admin 요청하면 403 에러 뜸 
//				exceptionHandling.accessDeniedPage("/error/403.jsp");
				exceptionHandling.accessDeniedHandler(new AccessDeniedHandler() {

					@Override
					public void handle(
							HttpServletRequest request, 
							HttpServletResponse response,
							AccessDeniedException accessDeniedException) throws IOException, ServletException {
						response.sendRedirect(request.getContextPath());
					}
				});
			});
			*/
		return http.build();
    }
	
	//db에 가서 인증을 하는 매니저를 사용할 예정
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncode) { // 빈 만들면 Usernamepassword~ 필터에 주입됨 
		//db에서 authenticate를 해주는 manager 생성 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    	
    	authenticationProvider.setUserDetailsService(userDetailsService);
    	authenticationProvider.setPasswordEncoder(passwordEncode);
    	
    	return new ProviderManager(authenticationProvider);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4); // 4 ~ 31
	}
	
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) { //username 가지고 UserDetails 리턴해줌 
		return new UserDetailsServiceImpl(userRepository); //
	}
}
