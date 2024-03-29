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

	@Override
	public UserDetails loadUserByUsername(String memId) throws UsernameNotFoundException {
		//UserDetails : Spring Security에서 사용자의 정보를 담는 인터페이스
		
		Member member = memberMapper.getMemberAccount(memId);
		
		return member;
	}	
	
	@Transactional
	public int enrollProcess(Member member) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //비밀번호 암호화
		member.setMemPwd(passwordEncoder.encode(member.getMemPwd()));
		member.setRole("ROLE_USER"); //회원권한 부여
		
		return memberMapper.enrollProcess(member);
	}		
}
