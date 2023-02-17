package com.oht.second.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.oht.second.model.MemberService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
//		super.configure(http);
		
		http.authorizeHttpRequests() // 이 주소경로로 요청이 들어오면
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**, /thymeleaf/**")
			.authenticated() // 인증이 필요하다.
			.anyRequest() //그 외의 요청들은
			.permitAll() // 모두 허용한다
			.and() //그리고
			.formLogin() // 로그인(인증)이 필요한 요청이 들어오면
			.loginPage("/auth/signin") // 로그인페이지 auth/signin 으로 이동시키고
			.loginProcessingUrl("/auth/signin") // auth/signin 이라는 POST요청을 실행시킨다.
			.defaultSuccessUrl("/"); // 인증이 정상적으로 완료되면 /로 이동한다
	}  //("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**")경로로 들어온 모든 요청은 인증이 필요하고 나머지는 허용한다 
	
	
	
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .antMatchers("/", "/login","/service","/resources/**","/create").permitAll()
//                .antMatchers("/admin").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and()
//           .formLogin()
//                .permitAll()
//                .and()
//            .logout()
//                .permitAll();
//    }
//	
//	@Bean
//	public PasswordEncoder passwordEncoder(){
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}
}
