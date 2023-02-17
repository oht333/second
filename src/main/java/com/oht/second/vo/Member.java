package com.oht.second.vo;

public class Member {
	private String memId;
	private String memPwd;
	private String memName;
	
	public Member(String memId, String memPwd, String memName) {
		super();
		this.memId = memId;
		this.memPwd = memPwd;
		this.memName = memName;
	}
	
	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getMemPwd() {
		return memPwd;
	}

	public void setMemPwd(String memPwd) {
		this.memPwd = memPwd;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	@Override
	public String toString() {
		return "Member [memId=" + memId + ", memPwd=" + memPwd + ", memName=" + memName + "]";
	}
	
}
