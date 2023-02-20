package com.oht.second.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {
	
//	@RequestMapping("/")
//	public String home(){
//		return "home";// template> home.html 으로 보내줌
//	}
	
	// 로그인 페이지로 이동
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	// 회원가입 페이지로 이동
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	//회원가입 기능
	@PostMapping("/auth/signup")
	public String signup() {
		System.out.println("signup 실행됨");
		return "/auth/signin"; // 회원가입이 완료되면 로그인페이지 이동
	}
	
	@GetMapping("/auth/success")
	public String loginSuccess() {
		System.out.println("로그인 성공");
		 return "/auth/success";
	}
	
	@GetMapping("/auth/fail")
	public String loginfail() {
		System.out.println("로그인 실패");
		 return "/auth/fail";
	}
}
