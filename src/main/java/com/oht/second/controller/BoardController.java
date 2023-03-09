package com.oht.second.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
		Attach attach = new Attach();
		
		attach = boardService.detailAttach(boardNo);
		
		model.addAttribute("detailBoard", detailBoard);
		model.addAttribute("attach", attach);
		
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

		int result = 0;
		
		if(!file.isEmpty()) {
			
		result = boardService.boardWrite(board);
		
		int boardNo = boardMapper.selectLastInsertId();
		board.setBoardNo(boardNo);
		Attach attach = storeFile(file, board);
		
		int attachResult = boardService.insertAttach(attach);
		
		} else {
			result = boardService.boardWrite(board);			
		}
		
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
		
		String extension = originalName.substring(originalName.lastIndexOf(".")+1); //확장자만 출력(.png)
		
		String saveName = originalName + "." + extension;
		
//		String saveFilename = genId + "." + extension;
		
		attach.setOriginalName(originalName);
		attach.setSaveName(saveName);
		attach.setBoardNo(board.getBoardNo());
		
//		String savePath = realFolder + originalName;
		
		multipartFile.transferTo(new File(realFolder + "/" + saveName));
	
		
		return attach;
	}
	
	@GetMapping("/attach/download/{fileNo}")
	public ResponseEntity<Object> downloadAttach(@PathVariable("fileNo") int fileNo){
		Attach attach = boardService.selectAttach(fileNo);
		
		String path = "c:/study/upload/" + attach.getSaveName();
		
		try {
			Path filePath = Paths.get(path);
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));	//InputStream을 통해서 resource를 불러온다
			
			File file = new File(path);
			
			HttpHeaders headers = new HttpHeaders();	//Header라는 부분을 통해 전송을 한다
			headers.setContentDisposition(ContentDisposition.builder("attach").filename(file.getName()).build());
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);	//값을 돌려보낼때 쓰는것    httpstatus가 성공적으로 반환이 되었다    OK가 되면 값을 200으로 돌려준다
		} catch(Exception e) {
			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
		}
	}
	
}
