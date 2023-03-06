package com.oht.second.model;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import com.oht.second.mapper.BoardMapper;
import com.oht.second.vo.Board;
import com.oht.second.vo.PageInfo;

@Repository
public class BoardDAO {

	@Autowired
	private BoardMapper boardMapper;

	public ArrayList<Board> boardList(PageInfo paging) {
		
		int offset = (paging.getCurrentPage() - 1) * paging.getBoardLimit();
		
		RowBounds rowBounds = new RowBounds(offset, paging.getBoardLimit());
		
		return boardMapper.boardList(rowBounds);
	}

//	public ArrayList<Board> boardList() {	
//		return boardMapper.boardList();
//	}
	
	public Board detailBoard(Board board) {		
		return boardMapper.detailBoard(board);
	}
	
//	public Board detailBoard(int boardNo) {		
//		return boardMapper.detailBoard(boardNo);
//	}
	
	public int boardWrite(Board board) {
		return boardMapper.boardWrite(board);
	}
	
	public void deleteBoard(int boardNo) {
		boardMapper.deleteBoard(boardNo);
	}

	public int editBoard(Board board) {
		return boardMapper.editBoard(board); 
	}

	public int findAllCnt() {
		return boardMapper.findAllCnt();
	}

}

