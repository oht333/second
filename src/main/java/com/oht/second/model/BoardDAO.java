package com.oht.second.model;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oht.second.mapper.BoardMapper;
import com.oht.second.vo.Attach;
import com.oht.second.vo.Board;
import com.oht.second.vo.PageInfo;

@Repository
public class BoardDAO {

	@Autowired
	private BoardMapper boardMapper;

	public ArrayList<Board> boardList(PageInfo paging, String category, String keyword) {
		
		int offset = (paging.getCurrentPage() - 1) * paging.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, paging.getBoardLimit()); //offset , 한 페이지에 보여질 게시글 최대 수
		// offset + limit 의 건수를 가져와서 앞의 offset건 만큼 건너뜀
		return boardMapper.boardList(rowBounds, category, keyword);
	}
	
	public Board detailBoard(int boardNo) {		
		return boardMapper.detailBoard(boardNo);
	}
	
	public int boardWrite(Board board) {
		return boardMapper.boardWrite(board);
	}
	
	public void deleteBoard(int boardNo) {
		boardMapper.deleteBoard(boardNo);
	}

	public int editBoard(Board board) {
		return boardMapper.editBoard(board); 
	}

	public int findAllCnt(String category, String keyword) {
		return boardMapper.findAllCnt(category, keyword);
	}

	@Transactional
	public int insertAttach(Attach attach) {
		return boardMapper.insertAttach(attach);
	}

	public Attach detailAttach(int boardNo) {
		return boardMapper.detailAttach(boardNo);
	}

	public Attach selectAttach(int fileNo) {
		return boardMapper.selectAttach(fileNo);
	}

}

