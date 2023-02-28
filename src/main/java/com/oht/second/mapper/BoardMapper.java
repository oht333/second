package com.oht.second.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.oht.second.vo.Board;
import com.oht.second.vo.PageInfo;

@Mapper
public interface BoardMapper {

//	public ArrayList<Board> boardList(RowBounds rowBounds);

	public ArrayList<Board> boardList();
	
	public int selectTotalCount();
	
	public Board detailBoard(Board board);

//	public Board detailBoard(int boardNo);
	
	public int boardWrite(Board board);
	
	public void deleteBoard(Board baord);

	public int editBoard(Board board);
}
