package com.oht.second.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
	private int boardNo;
	private String title;
	private String content;
	private Date regdate;
	private String memId;
	private String attach;
	private int viewCount;
	private String status;
}
