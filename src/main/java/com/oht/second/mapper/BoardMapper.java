package com.oht.second.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.oht.second.vo.Attach;
import com.oht.second.vo.Board;
import com.oht.second.vo.PageInfo;

@Mapper
public interface BoardMapper {
	
	public ArrayList<Board> boardList(RowBounds rowBounds, String category, String keyword);
	
	public Board detailBoard(int boardNo);
	
	public int boardWrite(Board board);
	
	public void deleteBoard(int boardNo);

	public int editBoard(Board board);

	public int findAllCnt(String category, String keyword);

	public int insertAttach(Attach attach);
	
	public int selectLastInsertId();

	public Attach detailAttach(int boardNo);

	public Attach selectAttach(int fileNo);
	
}
