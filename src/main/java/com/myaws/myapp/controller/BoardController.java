package com.myaws.myapp.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.MemberVo;
import com.myaws.myapp.domain.PageMaker;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.service.BoardService;
import com.myaws.myapp.util.MediaUtils;
import com.myaws.myapp.util.UploadFileUtiles;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
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
	public String boardWriteAction(BoardVo bv,@RequestParam("attachfile") MultipartFile attachfile, HttpServletRequest request,RedirectAttributes rttr) throws Exception { //보드Vo타입으로 바인딩해서 받는다, 멀티파트파일로 파일도 받는다
		//System.out.println("boardWriteAction");
		MultipartFile file = attachfile;
		
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
	
	@RequestMapping(value="boardContents.aws")
	public String boardContents(@RequestParam("bidx") int bidx, Model model) {
		
		boardService.boardViewCntUpdate(bidx);
		BoardVo bv = boardService.boardSelectOne(bidx);
		
		model.addAttribute("bv", bv);
		
		String path ="WEB-INF/board/boardContents"; 
		return path;
	}
	
	@RequestMapping(value="/displayFile.aws", method=RequestMethod.GET)  //가상경로에다가 겟방식으로 파일이름을 넘긴다 
	public ResponseEntity<byte[]> displayFile(
			@RequestParam("fileName") String fileName,
			@RequestParam(value="down",defaultValue="0") int down
			){
		
		ResponseEntity<byte[]> entity = null;  // 바이트타입 객체를 담는다
		InputStream in = null; // 시작하는 시점의 데이터수로 = 인풋스트림
		
		
		try{
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);//확장자를 물어봄
			MediaType mType = MediaUtils.getMediaType(formatName);//무슨타입인지 알려고
			
			HttpHeaders headers = new HttpHeaders();		
			 
			in = new FileInputStream(uploadPath+fileName); //파일을 읽음			
			
			if(mType != null){ // 이미지파일이면
				
				if (down==1) {
					fileName = fileName.substring(fileName.indexOf("_")+1);
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.add("Content-Disposition", "attachment; filename=\""+
							new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");	
					
				}else {
					headers.setContentType(mType);	
				}
				
			}else{
				
				fileName = fileName.substring(fileName.indexOf("_")+1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; filename=\""+
						new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\""); //다운로드받는 방식으로				
			}
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in),headers,HttpStatus.CREATED);
			
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		
		
		
		return entity;
	}
	
	@ResponseBody
	@RequestMapping(value="boardRecom.aws",method=RequestMethod.GET)
	public JSONObject boardRecom(@RequestParam("bidx") int bidx) {
		
		int value = boardService.boardRecomUpdate(bidx);
		
		JSONObject js = new JSONObject();
		
		js.put("recom", value);
		
		return js;
	}
	@RequestMapping(value="boardDelete.aws")
	public String boardDelete(@RequestParam("bidx") int bidx, Model model) {
		
		model.addAttribute("bidx", bidx);
		
		String path = "WEB-INF/board/boardDelete";
		
		
		return path;
	}
	
	
	
	
	
	
	
	@RequestMapping(value="boardDeleteAction.aws",method=RequestMethod.POST)
	public String boardDeleteAction(@RequestParam("bidx") int bidx, @RequestParam("password") String password, HttpSession session) {
		
		int midx = Integer.parseInt(session.getAttribute("midx").toString());
		
		int value = boardService.boardDelete(bidx,midx,password);
		
		String path = "redirect:/board/boardList.aws";
		if(value == 0) {
			path = "redirect:/board/boardDelete.aws?bidx="+bidx;
						
		}
		return path;
	}
	
	@RequestMapping(value="boardModify.aws")
	public String boardModify(@RequestParam("bidx") int bidx, Model model) {
		
		
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		model.addAttribute("bv", bv);
		
		String path ="WEB-INF/board/boardModify"; 
		return path;
	}
	
	
	@RequestMapping(value="boardModifyAction.aws")
	public String boardModifyAction(BoardVo bv,@RequestParam("attachfile") MultipartFile attachfile, HttpServletRequest request,RedirectAttributes rttr) throws Exception { //보드Vo타입으로 바인딩해서 받는다, 멀티파트파일로 파일도 받는다
		//System.out.println("boardWriteAction");
		//파일업로드를 하고 update를 하기위한 service를 만든다
		
		int value = 0;
		
		
		MultipartFile file = attachfile;
		String uploadedFiledName = "";
		
		if(!file.getOriginalFilename().equals("")) {
			
			uploadedFiledName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
			
		}
		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx);
		String ip = getUserIp(request);
		bv.setUploadedFilename(uploadedFiledName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		bv.setUploadedFilename(uploadedFiledName); //filename 컬럼값으로 넣을려고
		
		
		
		
		value = boardService.boardUpdate(bv);
		
				
		
			String path="redirect:/board/boardList.aws";
			if(value ==0) {
				rttr.addFlashAttribute("msg", "글이 수정되지 않았습니다");
				path ="redirect:/board/boardModfiy.aws?bidx="+bv.getBidx();
								
			}else {
				
				path ="redirect:/board/boardContents.aws?bidx="+bv.getBidx();
				
			}
		
		 
		return path;
	}
	
	
	@RequestMapping(value="boardReply.aws")
	public String boardReply(@RequestParam("bidx") int bidx, Model model) {
		
		BoardVo bv = boardService.boardSelectOne(bidx);
				
		model.addAttribute("bv", bv);
		
		String path ="WEB-INF/board/boardReply"; 
		return path;
	}
	
	
	@RequestMapping(value="boardReplyAction.aws")
	public String boardReplyAction(BoardVo bv,@RequestParam("attachfile") MultipartFile attachfile, HttpServletRequest request,RedirectAttributes rttr) throws Exception { //보드Vo타입으로 바인딩해서 받는다, 멀티파트파일로 파일도 받는다
		//System.out.println("boardWriteAction");
		//파일업로드를 하고 update를 하기위한 service를 만든다
		
		int value = 0;
		
		
		MultipartFile file = attachfile;
		String uploadedFiledName = "";
		
		if(!file.getOriginalFilename().equals("")) {
			
			uploadedFiledName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
			
		}
		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx);
		String ip = getUserIp(request);
		bv.setUploadedFilename(uploadedFiledName); //filename 컬럼값으로 넣을려고
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
				
		
		//value = boardService.boardRelpy(bv);
		
		
				
			int maxBidx = 0;
			maxBidx = boardService.boardReply(bv);
			String path="";
			if(maxBidx != 0) {
				
				path ="redirect:/board/boardContents.aws?bidx="+maxBidx;
								
			}else {
				rttr.addFlashAttribute("msg", "답변이 등록되지 않았습니다");
				path ="redirect:/board/boardReply.aws?bidx="+bv.getBidx();
				
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
