package com.myaws.myapp.persistance;

import com.myaws.myapp.domain.MemberVo;

public interface MemberMapper {
	
	public int memberInsert(MemberVo mv);
	
	public int memberIdCheck(String memberId);
	
	
	public MemberVo memberLoginCheck(String memberId);

}
