package com.oht.second.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.oht.second.vo.Member;

@Mapper
public interface MemberMapper {

	public Member getMemberAccount(String memId);
	
	public int enrollProcess(Member member);
}

