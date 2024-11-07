package com.myaws.myapp.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.MemberVo;
import com.myaws.myapp.domain.PageMaker;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.service.BoardService;
import com.myaws.myapp.util.UploadFileUtiles;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	 * //logger.info("boardList들어옴"); ArrayList<BoardVo> alist =
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
	
	@Resource(name="uploadPath")
	private String uploadPath;
	
	
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
	
	@RequestMapping(value="boardWrite.aws")
	public String boardWrite() {
		
		
		String path ="WEB-INF/board/boardWrite"; 
		return path;
	}
	
	@RequestMapping(value="boardWriteAction.aws")
	public String boardWriteAction(BoardVo bv,@RequestParam("filename") MultipartFile filename, HttpServletRequest request,RedirectAttributes rttr) throws Exception { //보드Vo타입으로 바인딩해서 받는다, 멀티파트파일로 파일도 받는다
		//System.out.println("boardWriteAction");
		MultipartFile file = filename;
		
		String uploadedFiledName = "";
		
		if(! file.getOriginalFilename().equals("")) {
			
			uploadedFiledName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
			//System.out.println("OriginalFilename");
		}
		
		String midx = request.getSession().getAttribute("midx").toString();
		//System.out.println("보드 컨트롤러midx : "+ midx);
		
		int midx_int = Integer.parseInt(midx);
		String ip = getUserIp(request);
		
		bv.setUploadedFilename(uploadedFiledName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
		int value = boardService.boardInsert(bv);
		
			String path="redirect:/board/boardList.aws";
			if(value ==2) {
				path ="redirect:/board/boardList.aws";
								
			}else {
				rttr.addFlashAttribute("msg", "입력이 잘못되었습니다");
				path ="redirect:/board/boardWrite.aws";
				
			}
		
		 
		return path;
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
