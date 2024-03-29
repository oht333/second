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
import com.oht.second.vo.ResponseDto;

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

		ArrayList<Board> list = boardService.boardList(paging, category, keyword);

		if(category!=null && keyword!=null && !category.equals("")) {
			model.addAttribute("category", category);
			model.addAttribute("keyword", keyword);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		
		return "board/list";
	}

	@GetMapping("/board/{boardNo}")
	public String readBoard(@PathVariable("boardNo") int boardNo, Model model) {

		//상세보기
		Board detailBoard = boardService.detailBoard(boardNo);
		
		//파일 첨부 
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

	@Transactional
	@PostMapping("/board/write")
	public String writeBoard(@AuthenticationPrincipal Member member, 
								@RequestParam(value="file") MultipartFile file, 
								Model model, Board board) throws IOException {

		board.setMemId(member.getMemId());
		
		if(!file.isEmpty()) {	
		
			board.setAttach("Y");
			boardService.writeBoard(board);			
			int boardNo = boardMapper.selectLastInsertId();
			board.setBoardNo(boardNo);
			
			Attach attach = storeFile(file, board);
			
			boardService.insertAttach(attach);

		} else {
			boardService.writeBoard(board);
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
		
		if(detailBoard.getAttach() != null && detailBoard.getAttach().equals("Y")) {
			Attach attach = boardService.selectAttach(boardNo);
			model.addAttribute("attach", attach);
		}
		model.addAttribute("detailBoard", detailBoard);
		 
		return "/board/update"; 
	}		
	
	@PostMapping("/board/update") 
	public String editBoard(Board board, Model model) { 
		  
		boardService.editBoard(board);
		
		model.addAttribute("message", "글 수정 완료되었습니다.");
		model.addAttribute("searchUrl", "/board/list");
		
		return "message";
	}	
	
	
	public Attach storeFile(MultipartFile multipartFile, Board board) throws IOException {

		if(multipartFile.isEmpty()) {		
			return null;
		}
		
		Attach attach = new Attach();
		
		String originalName = multipartFile.getOriginalFilename();
		// 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
		String extension = originalName.substring(originalName.lastIndexOf(".")+1); //확장자만 출력(.png)
		
		String saveName = UUID.randomUUID().toString() + "." + extension;	
		
		attach.setOriginalName(originalName);
		attach.setBoardNo(board.getBoardNo());
		attach.setStatus("Y");
		attach.setSaveName(saveName);
		log.info("saveName = " + attach.getSaveName());		
		
		//파일경로 (toAbsolutePath : 상대 경로를 절대 경로로 변환한다)
		Path uploadPath = Paths.get("c:/study/upload/").toAbsolutePath().normalize();
		
		//(resolve : 두경로를 조합할때 사용한다)
		Path targetLocation = uploadPath.resolve(attach.getSaveName());
		//파일경로 + 파일명
		
		attach.setPath(uploadPath.toString());
		
		attach.setSaveName(attach.getPath() + '\\' + attach.getSaveName());
		log.info("saveName = " + attach.getSaveName());
		
		multipartFile.transferTo(targetLocation.toFile());
		//multipartfile을 이용해 파일을 전송하고, transferto를 이용해 저장을 할 수 있습니다.

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

			//한글명의 파일을 다운로드하면 깨지기 때문에 인코딩을 거친 파일명으로 설정
			//(URLEncoder로 작성시 띄어쓰기가 '+'로 인식함) : URL에서는 공백문자가 허용되지 않기 때문에 '%20' 이나 '+'로 인코딩된다.
			//String encodingName = String.format("%s", URLEncoder.encode(attach.getOriginalName(), "UTF-8"));

			String encodingName = String.format("%s", UriUtils.encode(attach.getOriginalName(), "UTF-8"));
			
			//Content-Disposition값 설정
			headers.setContentDisposition(ContentDisposition.builder("attach").filename(encodingName).build());
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK); //처리성공
		} catch(Exception e) {
			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
		}
	}
	
	//첨부파일 삭제
	@PostMapping("/attach/delete")
	public ResponseDto<Integer> deleteAttachment(Attach attach) {
		//ajax 이용하여 넘어왔음
		
//		attachment 상태 N으로 변경
		int result = boardMapper.deleteAttachmentFileNo(attach);
		
		// 결과값 돌려주기 위함
		if(result != 0) {
			return new ResponseDto<Integer>(HttpStatus.OK, result); // 200, 성공적인 요청
		} else {
			return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR, result); //500 서버에서 예기치 않은 오류 
		}
	}
	
}
