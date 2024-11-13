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
	
	
	@RequestMapping(value="/commentWriteAction.aws",method=RequestMethod.POST)
	public JSONObject commentWriteAction(CommentVo cv, HttpServletRequest request) throws Exception {
		
		JSONObject js = new JSONObject();
		String cip = getUserIp(request);
		cv.setCip(cip);
		int value = commentService.commentInsert(cv);
		
		js.put("comment", value);
		
		
		
				
		return js;
	}
	
	
	
	
	
	
	
	
	public String getUserIp(HttpServletRequest request) throws Exception {
			
	        String ip = null;
	       // HttpServletRequest request = ((ServletRequestAttributes)ServletRequestContext.currentRequestAttributes()).getRequest();
	
	        ip = request.getHeader("X-Forwarded-For");
	        
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("Proxy-Client-IP"); 
	        } 
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("WL-Proxy-Client-IP"); 
	        } 
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("HTTP_CLIENT_IP"); 
	        } 
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("X-Real-IP"); 
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("X-RealIP"); 
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("REMOTE_ADDR");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getRemoteAddr(); 
	        }
	        
	        
	        
	        if(ip.equals("0:0:0:0:0:0:0:1")||ip.equals("127.0.0.1")) {
	        	InetAddress address = InetAddress.getLocalHost();
	        	ip = address.getHostName() + "/" + address.getHostAddress();
	        	
	        }
	        		
			return ip;
		}
	

}
