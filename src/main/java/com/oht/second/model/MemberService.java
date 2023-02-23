package com.oht.second.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oht.second.mapper.MemberMapper;
import com.oht.second.vo.Member;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

	@Autowired
	private MemberMapper memberMapper;
	
//	@Autowired
//	private MemberDAO memberDao;
	
//	public Member loginMember(Member member) {
//		return memberDao.loginMember(member);
//	}

	@Override
	public UserDetails loadUserByUsername(String memId) throws UsernameNotFoundException {
		//UserDetails : Spring Security에서 사용자의 정보를 담는 인터페이스
		Member member = memberMapper.getMemberAccount(memId);
		
		if(member == null) {
			throw new UsernameNotFoundException("User " + memId + " not found");
		}
		
		return member;
	}
	
	
	@Transactional
	public void enrollProcess(Member member) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //비밀번호 암호화
		member.setMemPwd(passwordEncoder.encode(member.getMemPwd()));
		member.setRole("ROLE_USER");
		
		memberMapper.enrollProcess(member);
	}
}
