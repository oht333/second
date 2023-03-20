package com.oht.second.vo;

public class Pagination {
	
    public static PageInfo getPageInfo(int currentPage, int listCount) {

        int pageLimit = 10;     // 한 페이지에서 보여질 페이지 버튼 수
        int maxPage;            // 전체 페이지 중 가장 마지막 페이지
        int startPage;          // 현재 페이지에서 보여질 페이징 버튼의 시작 페이지
        int endPage;            // 현재 페이지에서 보여질 페이징 버튼의 끝 페이지        
        int boardLimit = 10;    // 한 페이지에서 보여질 게시글 갯수
        
        maxPage = (int)((double)listCount / boardLimit + 0.9);
        startPage = (((int)((double)currentPage / pageLimit + 0.9)) - 1) * pageLimit + 1;
        endPage = startPage + pageLimit - 1;
        
        if(maxPage < endPage) {
            endPage = maxPage;
        }
        
        return new PageInfo(currentPage, listCount, pageLimit, maxPage, startPage, endPage, boardLimit);    	
    }
}
