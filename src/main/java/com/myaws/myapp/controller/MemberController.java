package com.myaws.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MemberController {
	
	@RequestMapping(value = "/member/memberJoin.aws",method = RequestMethod.GET)
	public String memberJoin() {
		
		
		
		return "WEB-INF/member/memberJoin";
	}
	
	
	
	
	@RequestMapping(value = "/member/memberLogin.aws",method = RequestMethod.GET)
	public String memberLogin() {
		
		
		
		return "WEB-INF/member/memberLogin";
	}

}
