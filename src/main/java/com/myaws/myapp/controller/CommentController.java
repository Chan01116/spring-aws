package com.myaws.myapp.controller;

import java.net.InetAddress;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.myaws.myapp.domain.CommentVo;
import com.myaws.myapp.service.CommentService;
import com.myaws.myapp.util.UserIp;

@RestController // 리스폰스바디가 있는 컨트롤러라는 뜻
@RequestMapping(value="/comment")
public class CommentController {
	
	
	@Autowired
	CommentService commentService;
	
	
	@Autowired(required=false)
	private UserIp userIp;
	
	
	@RequestMapping(value="/{bidx}/{block}/commentList.aws")
	public JSONObject commentList(@PathVariable("bidx") int bidx,@PathVariable("block") int block) {
		
		String moreView = "";
		int nextBlock = 0;
		int cnt = commentService.commentTotalCnt(bidx);
		
		if(cnt > block*15) {
			moreView = "Y";
			nextBlock = block+1;
		}else {
			moreView = "N";
			nextBlock = block;
			
		}
		
		
		ArrayList<CommentVo> clist = commentService.commentSelectAll(bidx,block);
		JSONObject js = new JSONObject();
		js.put("clist", clist);
		js.put("moreView", moreView);
		js.put("nextBlock", nextBlock);
		
		
				
		return js;
	}
	
	
	@RequestMapping(value="/commentWriteAction.aws",method=RequestMethod.POST)
	public JSONObject commentWriteAction(CommentVo cv, HttpServletRequest request) throws Exception {
		
		JSONObject js = new JSONObject();
		String cip = userIp.getUserIp(request);
		cv.setCip(cip);
		int value = commentService.commentInsert(cv);
		
		js.put("value", value);
		
		
		
				
		return js;
	}
	
	
	@RequestMapping(value="/{cidx}/commentDeleteAction.aws")
	public JSONObject commentDeleteAction(@PathVariable("cidx") int cidx, HttpServletRequest request, CommentVo cv) throws Exception {
		
		
		int midx = Integer.parseInt(request.getSession().getAttribute("midx").toString());
		
		cv.setMidx(midx);
		cv.setCidx(cidx);
		cv.setCip(userIp.getUserIp(request));
		int value = commentService.commentDelete(cv);
		JSONObject js = new JSONObject();
		
		
		js.put("value", value);
		
		
		
				
		return js;
	}
	
	

}
