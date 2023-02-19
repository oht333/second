package com.oht.second.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {
	
	@RequestMapping("/")
	public String home(){
		return "home";// template> home.html 으로 보내줌
	}
	
	@GetMapping("/auth/signin")
	public String signinPage() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupPage() {
		return "auth/signup";
	}
	
	//회원가입 기능
	@PostMapping("/auth/signup")
	public String signup() {
		System.out.println("signup 실행됨");
		return "/auth/signin"; // 회원가입이 완료되면 로그인페이지 이동
	}
}
