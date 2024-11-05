package com.myaws.myapp.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myaws.myapp.domain.MemberVo;
// Component또는 Bean 타입을 썼지만 세부적으로 구분하기위해 Service를 쓴다
import com.myaws.myapp.persistance.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{
	
	private MemberMapper mm;
	
	
	@Autowired   //스프링코어한테 여기다 주입해줘라고 이야기 하는것
	public MemberServiceImpl(SqlSession sqlSession) {
		this.mm = sqlSession.getMapper(MemberMapper.class);
		
	}	
	
	
	@Override
	public int memberInsert(MemberVo mv) {
		int value = mm.memberInsert(mv);
		
		return value;
	}


	@Override
	public int memberIdCheck(String memberId) {
		int value = mm.memberIdCheck(memberId);
		
		return value;
	}


	@Override
	public MemberVo memberLoginCheck(String memberId) {
		
		MemberVo mv = mm.memberLoginCheck(memberId);
		//System.out.println("mv:"+mv);
		
		return mv;
	}


	@Override
	public ArrayList<MemberVo> memberSelectAll() {
		
		ArrayList<MemberVo> alist = mm.memberSelectAll();
		
		return alist;
	}
	
	
	
	
	
	
	

}
