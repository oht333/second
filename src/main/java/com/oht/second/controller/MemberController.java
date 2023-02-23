package com.oht.second.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oht.second.model.MemberService;
import com.oht.second.vo.Member;

@Controller
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/")
	public String home(){
		return "auth/signin";// template> home.html 으로 보내줌
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
	
	@GetMapping("/auth/success")
	public String loginSuccess() {
		System.out.println("로그인 성공");
		 return "/auth/success";
	}
	
	@GetMapping("/auth/fail")
	public String loginfail(Member member) {
		logger.info("파라미터 member={}",member);
		System.out.println("로그인 실패");
		 return "/auth/fail";
	}
	
	@GetMapping("/enroll/page")
	public ModelAndView enrollPageReturn(ModelAndView mv) {
		mv.setViewName("/auth/signup");
		
		return mv;
	}

//	@PostMapping("/enroll/enroll")
//	public ModelAndView enrollProcess(ModelAndView mv, Member member) {
//		
//		memberService.enrollProcess(member);
//		mv.setViewName("/auth/signin");
//		
//		return mv;
//	}
	
	@PostMapping("/enroll/enroll")
	public String enrollProcess(Model model, Member member) {
		
		logger.info(member.toString());
		
		memberService.enrollProcess(member);

		model.addAttribute("message", "회원가입이 완료되었습니다");
		model.addAttribute("searchUrl", "/auth/signin");
		
		return "message";
	}
}
