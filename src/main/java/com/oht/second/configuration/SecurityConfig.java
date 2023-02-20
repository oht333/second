package com.oht.second.configuration;



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

	//PasswordEncoder 객체를 만들어서 Bean으로 등록시킴. 
	//PasswordEncoder : 평문 패스워드를 암호화 처리 해줌.
//   @Bean
//   BCryptPasswordEncoder passwordEncoder() 	
//   {
//      BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
//      //밑에 세 줄은 테스트
//      System.out.println("memId->" + enc.encode("memId"));
//      System.out.println("memPwd->" + enc.encode("memPwd"));
//       return enc;
//   }
	
	
	@Autowired
	MemberService memberService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable(); //csrf 비활성화가 필요할때 사용
		
		http
				.authorizeRequests() // 이 주소경로로 요청이 들어오면
				.antMatchers("/", "/login/**", "/board/**", "/enroll/**", "/thymeleaf/**")
				.authenticated() // 인증이 필요하다.
				.anyRequest() //그 외의 요청들은
				.permitAll() // 모두 허용한다
			.and() //그리고
				.formLogin() // 로그인(인증)이 필요한 요청이 들어오면
				.loginPage("/auth/signin") // 로그인페이지 auth/signin 으로 이동시키고
//				.loginProcessingUrl("/auth/signin") // auth/signin 이라는 POST요청을 실행시킨다.
//				.usernameParameter("memId")
//				.passwordParameter("memPwd")
				.defaultSuccessUrl("/list") // 인증이 정상적으로 완료되면 /auth/success로 이동한다
				.failureUrl("/auth/fail")
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

	

	
}
