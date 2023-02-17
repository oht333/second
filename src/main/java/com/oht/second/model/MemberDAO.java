package com.oht.second.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oht.second.mapper.MemberMapper;
import com.oht.second.vo.Member;

@Repository
public class MemberDAO {

	@Autowired
	private MemberMapper memberMapper;
	
	public Member loginMember(Member member) {
		return memberMapper.loginMember(member);
	}

}
