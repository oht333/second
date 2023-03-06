package com.oht.second.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String boardList(Model model, @RequestParam(defaultValue = "1") int currentPage) {		

		int listCount = boardService.findAllCnt();
		
		PageInfo paging = Pagination.getPageInfo(currentPage, listCount);
				
		ArrayList<Board> list = boardService.boardList(paging);

		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		
		return "boardList";
	}	
	
	
//	@GetMapping("/board/detail/{boardNo}")
//	public String detailBoard(@PathVariable("boardNo") int boardNo, Model model) {
//
//		Board detailBoard = boardService.detailBoard(boardNo);
//		
//		model.addAttribute("detailBoard", detailBoard);
//		
//		return "board/detail";	
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

	@PostMapping("/board/write")
	public String boardWrite(@AuthenticationPrincipal Member member, Model model, Board board) {

		board.setMemId(member.getMemId());

		int result = boardService.boardWrite(board);
		
		model.addAttribute("message", "글 작성이 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}
	
//	@GetMapping("board/delete") 
//	public String deleteBoard(Board board, Model model) { 
//		boardService.deleteBoard(board); 
//
//		model.addAttribute("message", "글 삭제가 완료되었습니다.");
//		model.addAttribute("searchUrl", "/board/list");
//		
//		return "message";
//	}	
	
	
	@GetMapping("board/delete/{boardNo}") 
	public String deleteBoard(@PathVariable("boardNo") int boardNo, Model model) { 
		boardService.deleteBoard(boardNo); 

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
	
//	@GetMapping("/board/{boardNo}") 
//	public String updateBoardPage(@PathVariable("boardNo") int boardNo, Model model) { 
//		
//		Board detailBoard = boardService.detailBoard(boardNo);
//
//		model.addAttribute("detailBoard", detailBoard);
//		 
//		return "/boardEdit"; 
//	}	
	
//	@GetMapping("/updateBoardPage") 
//	public String updateBoardPage(Board board, Model model) { 
//		Board detailBoard = boardService.detailBoard(board);
//
//		model.addAttribute("detailBoard", detailBoard);
//		 
//		return "/boardEdit"; 
//	}
	
	
	
	@PostMapping("/editBoard") 
	public String editBoard(Board board, Model model) { 
		  
		int res = boardService.editBoard(board);
		
		model.addAttribute("message", "글 수정 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}	
}
