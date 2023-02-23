package com.oht.second.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oht.second.mapper.BoardMapper;
import com.oht.second.vo.Board;

@Repository
public class BoardDAO {

	@Autowired
	private BoardMapper boardMapper;
	
	public ArrayList<Board> boardList() {
		return boardMapper.boardList();
	}
	
	public Board detailBoard(Board board) {		
		return boardMapper.detailBoard(board);
	}
	
	public int writeBoard(Board board) {
		return boardMapper.writeBoard(board);
		
	}
	
	public void deleteBoard(Board board) {
		boardMapper.deleteBoard(board);
	}
//	
//	public int editBoard(Board board) { 
//		return boardMapper.editBoard(board); 
//	}

	public int editBoard(Board board) {
		return boardMapper.editBoard(board); 
	}
	
}

