package com.myaws.myapp.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.MemberVo;
import com.myaws.myapp.domain.PageMaker;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.service.BoardService;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping(value = "/board/")
public class BoardController {
	
	
	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(BoardController.class);
	 * 
	 * //@Autowired //private Test tt;
	 * 
	 * 
	 * 
	 * @Autowired private BoardService boardService;
	 * 
	 * @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	 * 
	 * 
	 * @RequestMapping(value = "boardList.aws",method = RequestMethod.GET) public
	 * String boardList(Model model) {
	 * 
	 * //logger.info("boardListµé¾î¿È"); ArrayList<BoardVo> alist =
	 * boardService.boardSelectAll();
	 * 
	 * model.addAttribute("alist", alist);
	 * 
	 * return "WEB-INF/board/boardList"; }
	 */
	
	
	@Autowired(required=false) 
	private BoardService boardService;
	
	@Autowired(required=false)
	private PageMaker pm;
	
	
	@RequestMapping(value="boardList.aws")
	public String boardList(SearchCriteria scri, Model model) {
		
		
		int cnt = boardService.boardTotalCount(scri);
		pm.setScri(scri);
		pm.setTotalCount(cnt);
		
		ArrayList<BoardVo> blist = boardService.boardSelectAll(scri);
		
		model.addAttribute("blist", blist);
		model.addAttribute("pm", pm);
		
		String path="WEB-INF/board/boardList";		
		return path;
	}
	
	
	
	

}
