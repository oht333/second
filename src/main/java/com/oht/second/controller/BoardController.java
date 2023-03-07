package com.oht.second.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.oht.second.vo.Member;
import com.oht.second.vo.PageInfo;
import com.oht.second.vo.Pagination;
import com.oht.second.vo.Attach;
import com.oht.second.vo.Board;
import com.oht.second.model.BoardService;

@Controller
public class BoardController {
	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private BoardService boardService;
	
	
	@GetMapping("/board/list") 
	public String boardList(Model model, @RequestParam(defaultValue = "1") int currentPage) {		

		int listCount = boardService.findAllCnt();
		
		PageInfo paging = Pagination.getPageInfo(currentPage, listCount);
				
		ArrayList<Board> list = boardService.boardList(paging);

		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		
		return "board/list";
	}		
	
	@GetMapping("/board/detail/{boardNo}")
	public String detailBoard(@PathVariable("boardNo") int boardNo, Model model) {

		Board detailBoard = boardService.detailBoard(boardNo);
		
		model.addAttribute("detailBoard", detailBoard);
		
		return "board/detail";	
	}	
	
	@GetMapping("/board/write")	
	public String write() {
		
		return "board/write";
	}

	
	//파일첨부 진행중
	
	@Transactional
	@PostMapping("/board/write")
	public String boardWrite(@AuthenticationPrincipal Member member, 
								@RequestParam(value="file") MultipartFile file, 
								Model model, Board board) throws IOException {

		board.setMemId(member.getMemId());

		int result = boardService.boardWrite(board);
		
		Attach attach = storeFile(file, board);
		
		int attachResult = boardService.insertAttach(attach);
		
		
		model.addAttribute("message", "글 작성이 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}	
	
	@GetMapping("board/delete/{boardNo}") 
	public String deleteBoard(@PathVariable("boardNo") int boardNo, Model model) { 
		boardService.deleteBoard(boardNo); 

		model.addAttribute("message", "글 삭제가 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}		
	
	@GetMapping("/board/update/{boardNo}") 
	public String updateBoardPage(@PathVariable("boardNo") int boardNo, Model model) { 
		
		Board detailBoard = boardService.detailBoard(boardNo);

		model.addAttribute("detailBoard", detailBoard);
		 
		return "/boardEdit"; 
	}		
	
	@PostMapping("/board/update") 
	public String editBoard(Board board, Model model) { 
		  
		int res = boardService.editBoard(board);
		
		model.addAttribute("message", "글 수정 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}	
	
	
	public Attach storeFile(MultipartFile multipartFile, Board board) throws IOException {
		if(multipartFile.isEmpty()) {
		
			return null;
		}
		
		Attach attach = new Attach();
		
		String realFolder = "c:/study/upload/";
		
		String originalName = multipartFile.getOriginalFilename();
		// 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
		// 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로
		
		String extension = StringUtils.getFilenameExtension(originalName);
		
		String genId = UUID.randomUUID().toString();
		
		String saveFilename = genId + "." + extension;
		
		attach.setOriginalName(originalName);
		attach.setSaveName(saveFilename);
//		attach.setStatus("Y");
		attach.setBoardNo(board.getBoardNo());
		attach.setMemId(board.getMemId());
		
		String savePath = realFolder + saveFilename;

		attach.setPath(savePath.toString());
		
		multipartFile.transferTo(new File(realFolder + "/" + genId));

		
		return attach;
	}
	
}
