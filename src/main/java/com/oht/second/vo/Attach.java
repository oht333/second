package com.oht.second.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Attach {
	private int fileNo;
	private String originalName;
	private String saveName;
	private String path;
	private int boardNo;
	private String memId;
}
