package com.myaws.myapp.controller;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myaws.myapp.domain.CommentVo;
import com.myaws.myapp.service.CommentService;

@RestController // 리스폰스바디가 있는 컨트롤러라는 뜻
@RequestMapping(value="/comment")
public class CommentController {
	
	
	@Autowired
	CommentService commentService;
	
	
	@RequestMapping(value="/{bidx}/commentList.aws")
	public JSONObject commentList(@PathVariable("bidx") int bidx) {
		
		
		JSONObject js = new JSONObject();
				
		ArrayList<CommentVo> clist = commentService.commentSelectAll(bidx);
		
		js.put("clist", clist);
		
		
		
				
		return js;
	}
	
	
	

}
