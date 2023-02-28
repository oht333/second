package com.oht.second.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oht.second.vo.Member;
import com.oht.second.vo.PageInfo;
import com.oht.second.vo.Pagination;
import com.oht.second.vo.Board;
import com.oht.second.model.BoardService;

@Controller
public class BoardController {

	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private BoardService boardService;

//	@GetMapping("/board/list") //url주소 끝자리에 /list를 붙여서 이동
//	public String boardList(Model model) {
//
//		ArrayList<Board> list = boardService.boardList();
//		
//		model.addAttribute("list", list);
//		
//		return "boardList";
//	}	
	
	@GetMapping("/board/list") //url주소 끝자리에 /list를 붙여서 이동
	public String boardList(@RequestParam(value="currentPage", defaultValue="1") int currentPage, Model model) {

		int selectCount = boardService.selectTotalCount();
		
		PageInfo p = Pagination.getPageInfo(selectCount, currentPage, 10, 10);
		
		ArrayList<Board> list = boardService.boardList();
		
		model.addAttribute("list", list);
		model.addAttribute("p", p);
		
		return "boardList";
	}	
	
	
	
	
	
//	@GetMapping("/board/{boardNo}")
//	public ModelAndView detailBoard(@PathVariable("boardNo") int boardNo, ModelAndView mv) {
//
//		Board detailBoard = boardService.detailBoard(boardNo);
//		
//		mv.addObject("detailBoard", detailBoard);
//		mv.setViewName("board/detail");
//		
//		System.out.println(mv);
//		
//		return mv;	
//	}	

	@GetMapping("/board/detailBoard")
	public ModelAndView detailBoard(ModelAndView mv, Board board) {

		Board detailBoard = boardService.detailBoard(board);
		
		mv.addObject("detailBoard", detailBoard);
		mv.setViewName("board/detail");
		
		return mv;	
	}		
	
	
	@GetMapping("/board/write")	//w.html에서 '글쓰기'칸(writeBoard.html로 링크되어 있는것)을 <a href="/board/write">로 연결시킴
	public String write() {
		
		return "writeBoard";
	}
	
//	@PostMapping("/writeBoard") /*값을 받아올 html 파일 매핑*/
//	public ModelAndView writeBoard(Board board, HttpSession session, ModelAndView mv, Member member) {
//				
//		board.setMemId(member.getMemId());
//		
//		int bd = boardService.writeBoard(board);
//		
//		mv.setViewName("redirect:/board/list");
//		return mv;
//	}

	@PostMapping("/board/write")
	public String boardWrite(Member member, Model model, Board board) {

		board.setMemId(member.getMemId());

		int result = boardService.boardWrite(board);
		
		model.addAttribute("message", "글 작성이 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}
	
	@GetMapping("/deleteBoard") 
	public String deleteBoard(Board board, Model model) { 
		boardService.deleteBoard(board); 

		model.addAttribute("message", "글 삭제가 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}
	
	
	@GetMapping("/updateBoardPage") 
	public ModelAndView updateBoardPage(Board board, ModelAndView mv) { 
		Board detailBoard = boardService.detailBoard(board);
		mv.addObject("detailBoard", detailBoard);
		mv.setViewName("/boardEdit");
		 
		return mv; 
	}
	
	@PostMapping("/editBoard") 
	public String editBoard(Board board, ModelAndView mv, Model model) { 
		  
		int res = boardService.editBoard(board);
		
		model.addAttribute("message", "글 수정 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}	
}
