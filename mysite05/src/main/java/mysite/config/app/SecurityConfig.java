package mysite.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mysite.repository.UserRepository;
import mysite.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.formLogin((formLogin) -> {
				formLogin
					.loginPage("/user/login")
					.loginProcessingUrl("/user/auth") //UsernamePasswordAuthenticationFilter가 이 url 감시 
					.usernameParameter("email")
					.passwordParameter("password")
					.defaultSuccessUrl("/")
					.failureUrl("/user/login?result=fail");
			})
			.authorizeHttpRequests((authorizeRequests) -> {
				/* ACL */
				authorizeRequests
				
					.requestMatchers(new RegexRequestMatcher("^/user/update$", null))
					.authenticated()
				
					.anyRequest()
					.permitAll();
			});
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
