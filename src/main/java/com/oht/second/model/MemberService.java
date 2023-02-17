package com.oht.second.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oht.second.vo.Member;

@Service
public class MemberService {

	@Autowired
	private MemberDAO memberDao;
	
	public Member loginMember(Member member) {
		return memberDao.loginMember(member);
	}


}
