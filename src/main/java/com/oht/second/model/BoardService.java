package com.oht.second.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oht.second.mapper.BoardMapper;
import com.oht.second.model.BoardDAO;
import com.oht.second.vo.Attach;
import com.oht.second.vo.Board;
import com.oht.second.vo.PageInfo;

@Service
public class BoardService {

	@Autowired
	private BoardDAO boardDao;

	public ArrayList<Board> boardList(PageInfo paging, String category, String keyword) {
        return boardDao.boardList(paging, category, keyword);
	}
	
	public Board detailBoard(int boardNo) {
		return boardDao.detailBoard(boardNo);
	}
	
	public int boardWrite(Board board) {
		return boardDao.boardWrite(board);
	}
	
	public void deleteBoard(int boardNo) {
		boardDao.deleteBoard(boardNo);
	}

	public int editBoard(Board board) {
		return boardDao.editBoard(board); 
	}

	public int findAllCnt(String category, String keyword) {
		return boardDao.findAllCnt(category, keyword);
	}

	@Transactional
	public int insertAttach(Attach attach) {
		
		return boardDao.insertAttach(attach);
	}

	public Attach detailAttach(int boardNo) {
		return boardDao.detailAttach(boardNo);
	}

	public Attach selectAttach(int fileNo) {
		return boardDao.selectAttach(fileNo);
	}
	
}
