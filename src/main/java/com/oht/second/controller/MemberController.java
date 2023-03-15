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
	
	//메인 페이지(로그인 페이지)
	@RequestMapping("/")
	public String home(){
		return "auth/signin";
	}
	
	//회원가입 페이지
	@GetMapping("/auth/signup")
	public String signupPage() {
		return "auth/signup";
	}
	
	//회원가입 추가
	@PostMapping("/auth/signup")
	public String signup(Member member) {
		logger.info("파라미터 member={}",member);
		return "/auth/signin";
	}
	
	//로그인 실패 페이지
	@GetMapping("/auth/fail")
	public String loginfail(Member member) {
		logger.info("파라미터 member={}",member);
		 return "/auth/fail";
	}
	
	//회원가입 페이지
	@GetMapping("/enroll/page")
	public ModelAndView enrollPageReturn(ModelAndView mv) {
		mv.setViewName("/auth/signup");
		
		return mv;
	}
	
	//회원가입 처리
	@PostMapping("/enroll/enroll")
	public String enrollProcess(Model model, Member member) {
		
		logger.info(member.toString());
		
		memberService.enrollProcess(member);

		model.addAttribute("message", "회원가입이 완료되었습니다");
		model.addAttribute("searchUrl", "/");
		
		return "message";
	}
}
