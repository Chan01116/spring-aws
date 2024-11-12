package com.myaws.myapp.service;



import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.CommentVo;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.persistance.BoardMapper;
// Component또는 Bean 타입을 썼지만 세부적으로 구분하기위해 Service를 쓴다
import com.myaws.myapp.persistance.CommentMapper;


@Service
public class CommentServiceImpl implements CommentService{
	
	private CommentMapper cm; //mybatis용 메서드
	
	
	@Autowired   //스프링코어한테 여기다 주입해줘라고 이야기 하는것
	public CommentServiceImpl(SqlSession sqlSession) {
		this.cm = sqlSession.getMapper(CommentMapper.class);
		
	}


	@Override
	public ArrayList<CommentVo> commentSelectAll(int bidx) {
		
		
		ArrayList<CommentVo> clist = cm.commentSelectAll(bidx);		
		
		return clist;
	}







	

}
