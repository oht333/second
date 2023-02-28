package com.oht.second.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {

	private int currentPage; 	// 현재 페이지
	private int listCount;      // 총 게시글의 수 [ SELECT COUNT(*) FROM BOARD ]
	private int pageLimit;    // 한 페이지 하단에 보여질 페이징 갯수 ex) 총 페이지가 13개면, 1 ~ 10 / 11 ~ 13
	private int maxPage;     // 총 페이지    (1 ~ 50) : 50
	private int startPage;    // 시작 페이지  (1    11    21    31    41)
	private int endPage;     // 끝 페이지    (10    20    30    40    50)
	private int boardLimit;	//한 페이지에 보여질 게시글 최대 수
	
}
