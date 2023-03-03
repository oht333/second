package com.oht.second.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oht.second.model.BoardDAO;
import com.oht.second.vo.Board;
import com.oht.second.vo.PageInfo;

@Service
public class BoardService {

	@Autowired
	private BoardDAO boardDao;

	public ArrayList<Board> boardList(PageInfo paging) {
        return boardDao.boardList(paging);
	}
	
	public Board detailBoard(Board board) {
		return boardDao.detailBoard(board);
	}

//	public Board detailBoard(int boardNo) {
//		return boardDao.detailBoard(boardNo);
//	}
	
	public int boardWrite(Board board) {
		return boardDao.boardWrite(board);
	}
	
	public void deleteBoard(Board board) {
		boardDao.deleteBoard(board);
	}

	public int editBoard(Board board) {
		return boardDao.editBoard(board); 
	}

	public int findAllCnt() {
		return boardDao.findAllCnt();
	}
	
}
