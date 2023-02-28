package com.oht.second.vo;

public class Pagination {
	
	public static PageInfo getPageInfo(int listCount, int currentPage, int pageLimit, int boardLimit) {
				
		int maxPage = (int)((double)listCount / boardLimit + 0.9);		
		int startPage = (((int)((double)currentPage / pageLimit + 0.9))-1) * pageLimit + 1;		
		int endPage = startPage + pageLimit - 1;
		
		if(maxPage < endPage) {
			endPage = maxPage;
		}
		
		//PageInfo 생성자를 이용하여 새로운 인스턴스를 생성한다
		return new PageInfo(currentPage, listCount, pageLimit, maxPage, startPage, endPage, boardLimit);
		
		 
	}

}
