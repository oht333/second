package com.oht.second.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.oht.second.vo.Member;

@Mapper
public interface MemberMapper {
//	public Member loginMember(Member member);

	public Member getMemberAccount(String memId);
	
	public void enrollProcess(Member member);
}

