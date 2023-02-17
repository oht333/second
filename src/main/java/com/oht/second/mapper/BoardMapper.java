package com.oht.second.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.oht.second.vo.Board;

@Mapper
public interface BoardMapper {

	public ArrayList<Board> boardList();
}
