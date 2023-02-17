package com.oht.second.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oht.second.model.BoardService;
import com.oht.second.vo.Board;

@Controller
public class BoardController {

	//DI - 생성자에 의한 종속객체 주입
	@Autowired
	private BoardService boardService;
	
//	@Autowired
//	private MemberService memberService;
	
	
	@GetMapping("/list") //url주소 끝자리에 /list를 붙여서 이동
	public ModelAndView boardList(ModelAndView mv) {
		
		ArrayList<Board> list = boardService.boardList();
		
		mv.addObject("list", list);  //받아온 list값을 list key에 설정. 나중에 html문서에서 출력하고 싶은게 있으면 ${list.title} 이렇게 기재하면 됨
		mv.setViewName("boardList"); //boardList.html로 페이지를 셋팅
		return mv;
	}		
}
