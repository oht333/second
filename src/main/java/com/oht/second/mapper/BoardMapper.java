package com.oht.second.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.oht.second.vo.Board;

@Mapper
public interface BoardMapper {

	public ArrayList<Board> boardList();
	
	public Board detailBoard(Board board);
	
	public int writeBoard(Board board);
	
	public void deleteBoard(Board baord);

	public int editBoard(Board board);
}
