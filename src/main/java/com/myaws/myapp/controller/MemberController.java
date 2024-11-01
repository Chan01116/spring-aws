package com.myaws.myapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myaws.myapp.domain.MemberVo;
import com.myaws.myapp.service.MemberService;
import com.myaws.myapp.service.Test;

@Controller
@RequestMapping(value = "/member/")
public class MemberController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	//@Autowired
	//private Test tt;
	
	
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping(value = "memberJoin.aws",method = RequestMethod.GET)
	public String memberJoin() {
		
		//logger.info("memberJoinµé¾î¿È");
		
				
		//logger.info("tt°ªÀº? : "+tt.test());
		
		return "WEB-INF/member/memberJoin";
	}
	
	
	@RequestMapping(value = "memberJoinAction.aws",method = RequestMethod.POST)
	public String memberJoinAction(MemberVo mv) {
		
		logger.info("memberJoinActionµé¾î¿È");
		
		int value = memberService.memberInsert(mv);
		logger.info("value : "+value);
		
		String path = "";
		if(value == 1) {
			path = "redirect:/";
			
		}else if(value == 0) {
			path = "redirect:/member/memberJoin.aws";
			
		}
				
		
		return path;
	}
	
	
	
	
	
	
	@RequestMapping(value = "memberLogin.aws",method = RequestMethod.GET)
	public String memberLogin() {
		
		
		
		return "WEB-INF/member/memberLogin";
	}

}
