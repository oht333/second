package com.oht.second.configuration;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.oht.second.model.MemberService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
	
	@Autowired
	MemberService memberService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
				.csrf().disable(); //csrf 비활성화가 필요할때 사용
		
		http
				.authorizeRequests() // 이 주소경로로 요청이 들어오면
				.antMatchers("/", "/login/**", "/board/**")
				.authenticated() // 인증이 필요하다.
				.anyRequest() //그 외의 요청들은
				.permitAll() // 모두 허용한다
			.and() //그리고
				.formLogin() // 로그인(인증)이 필요한 요청이 들어오면
				.loginPage("/auth/signin") // 로그인페이지 auth/signin 으로 이동시키고
				.loginProcessingUrl("/login/signin") // auth/signin 이라는 POST요청을 실행시킨다.
				.usernameParameter("memId")
				.passwordParameter("memPwd")
//				.defaultSuccessUrl("/list") // 인증이 정상적으로 완료되면 /auth/success로 이동한다
//				.failureUrl("/auth/fail")
//				.permitAll()
        		.successHandler(new AuthenticationSuccessHandler( ) {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						response.sendRedirect("/board/list");
					}
        		})
        		.failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						response.sendRedirect("/auth/fail");
					}
        		})
        		.permitAll()				
			.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/auth/signin")
				.invalidateHttpSession(true) // 세션 clear
			.and()
				.exceptionHandling() // 에러 처리
				.accessDeniedPage("/error");
			
	} 

	  
//	   PasswordEncoder를 Bean으로 등록
	  
//	  @Bean
//	  public BCryptPasswordEncoder bCryptPasswordEncoder() {
//	    return new BCryptPasswordEncoder();
//	  }
	
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(new BCryptPasswordEncoder());
    }
	  
}
