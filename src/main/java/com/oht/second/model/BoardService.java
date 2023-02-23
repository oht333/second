package com.oht.second.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oht.second.model.BoardDAO;
import com.oht.second.vo.Board;

@Service
public class BoardService {

	@Autowired
	private BoardDAO boardDao;

	public ArrayList<Board> boardList() {
        return boardDao.boardList();
	}
	
	public Board detailBoard(Board board) {
		return boardDao.detailBoard(board);
	}
	
	public int writeBoard(Board board) {
		return boardDao.writeBoard(board);
		
	}
	
	public void deleteBoard(Board board) {
		boardDao.deleteBoard(board);
	}

	public int editBoard(Board board) {
		return boardDao.editBoard(board); 
	}
}
