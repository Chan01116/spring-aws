package com.myaws.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.CommentVo;
import com.myaws.myapp.domain.SearchCriteria;

public interface CommentMapper {

	public ArrayList<CommentVo> commentSelectAll(int bidx);
	public int commentInsert(CommentVo cv);

}
