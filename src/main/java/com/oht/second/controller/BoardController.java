package com.oht.second.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.oht.second.vo.Member;
import com.oht.second.vo.PageInfo;
import com.oht.second.vo.Pagination;

import lombok.RequiredArgsConstructor;

import com.oht.second.vo.Attach;
import com.oht.second.vo.Board;
import com.oht.second.mapper.BoardMapper;
import com.oht.second.model.BoardService;


@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	private final BoardService boardService;
	private final BoardMapper boardMapper;
	
	@GetMapping("/board/list") 
	public String boardList(Model model, @RequestParam(defaultValue = "1") int currentPage,
							String category, String keyword) {		

		int listCount = boardService.findAllCnt(category, keyword);
		
		PageInfo paging = Pagination.getPageInfo(currentPage, listCount);
		//getPageInfo = new PageInfo(currentPage, listCount, pageLimit, maxPage, startPage, endPage, boardLimit);
				
		ArrayList<Board> list = boardService.boardList(paging, category, keyword);

	    if(category!=null && !category.equals("") && keyword!=null) {
	    	model.addAttribute("category", category);
	    	model.addAttribute("keyword", keyword);
	    }
		
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		
		return "board/list";
	}		
	
	@GetMapping("/board/detail/{boardNo}")
	public String detailBoard(@PathVariable("boardNo") int boardNo, Model model) {

		//????????????
		Board detailBoard = boardService.detailBoard(boardNo);
		
		//?????? ?????? 
		Attach attach = new Attach();
		if(detailBoard.getAttach().equals("Y")) {
			attach = boardService.detailAttach(boardNo);
		}
		
		boardService.updateView(boardNo);
		
		model.addAttribute("detailBoard", detailBoard);
		model.addAttribute("attach", attach);
		
		return "board/detail";	
	}	
	
	@GetMapping("/board/write")	
	public String write() {
		
		return "board/write";
	}

	
	//???????????? ?????????
	
	@Transactional
	@PostMapping("/board/write")
	public String writeBoard(@AuthenticationPrincipal Member member, 
								@RequestParam(value="file") MultipartFile file, 
								Model model, Board board) throws IOException {

		board.setMemId(member.getMemId());

		int result = 0;
		
		if(!file.isEmpty()) {	
		
			board.setAttach("Y");
			result = boardService.writeBoard(board);
			
			int boardNo = boardMapper.selectLastInsertId();
			board.setBoardNo(boardNo);
			Attach attach = storeFile(file, board);
			
			int attachResult = boardService.insertAttach(attach);
		
		} else {
			result = boardService.writeBoard(board);
		}
		
		model.addAttribute("message", "??? ????????? ?????????????????????.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}	
	
	@GetMapping("board/delete/{boardNo}") 
	public String deleteBoard(@PathVariable("boardNo") int boardNo, Model model) { 
		boardService.deleteBoard(boardNo); 

		model.addAttribute("message", "??? ????????? ?????????????????????.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}		
	
	@GetMapping("/board/update/{boardNo}") 
	public String updateBoardPage(@PathVariable("boardNo") int boardNo, Model model) { 
		
		Board detailBoard = boardService.detailBoard(boardNo);

		model.addAttribute("detailBoard", detailBoard);
		 
		return "/board/update"; 
	}		
	
	@PostMapping("/board/update") 
	public String editBoard(Board board, Model model) { 
		  
		int res = boardService.editBoard(board);
		
		model.addAttribute("message", "??? ?????? ?????????????????????.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}	
	
	
	public Attach storeFile(MultipartFile multipartFile, Board board) throws IOException {
		if(multipartFile.isEmpty()) {
		
			return null;
		}
		
		Attach attach = new Attach();
		
		String originalName = multipartFile.getOriginalFilename();
		// ???????????? ???????????? ????????? -> ?????? ???????????? ???????????? ?????????
		String extension = originalName.substring(originalName.lastIndexOf(".")+1); //???????????? ??????(.png)
		
		String saveName = UUID.randomUUID().toString() + "." + extension;	
		
		attach.setOriginalName(originalName);
		attach.setBoardNo(board.getBoardNo());
		attach.setSaveName(saveName);
		log.info("saveName = " + attach.getSaveName());		
		
		//???????????? (toAbsolutePath : ?????? ????????? ?????? ????????? ????????????)
		Path uploadPath = Paths.get("c:/study/upload/").toAbsolutePath().normalize();
		
		//(resolve : ???????????? ???????????? ????????????)
		Path targetLocation = uploadPath.resolve(attach.getSaveName());
		//???????????? + ?????????
		
		attach.setPath(uploadPath.toString());
		
		attach.setSaveName(attach.getPath() + '\\' + attach.getSaveName());
		log.info("saveName = " + attach.getSaveName());
		
		multipartFile.transferTo(targetLocation.toFile());
		//multipartfile??? ????????? ????????? ????????????, transferto??? ????????? ????????? ??? ??? ????????????.
		
		return attach;
	}

	
	@GetMapping("/attach/download/{fileNo}")
	public ResponseEntity<Object> downloadAttach(@PathVariable("fileNo") int fileNo){
		Attach attach = boardService.selectAttach(fileNo);		
		
		try {
			Path filePath = Paths.get(attach.getSaveName());
			//c:\study\\upload\08a8bb09-5556-490c-a2c0-6711ed77ee4f.png 
			
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));

			File file = new File(attach.getSaveName());
			
			HttpHeaders headers = new HttpHeaders();	

			//???????????? ????????? ?????????????????? ????????? ????????? ???????????? ?????? ??????????????? ??????
			//(URLEncoder??? ????????? ??????????????? '+'??? ?????????)
			//String encodingName = String.format("%s", URLEncoder.encode(attach.getOriginalName(), "UTF-8"));

			String encodingName = String.format("%s", UriUtils.encode(attach.getOriginalName(), "UTF-8"));
			
			//Content-Disposition??? ??????
			headers.setContentDisposition(ContentDisposition.builder("attach").filename(encodingName).build());
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK); //????????????
		} catch(Exception e) {
			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
		}
	}
	
}
