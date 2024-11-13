<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.myaws.myapp.domain.*" %>
    
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <%
    BoardVo bv = (BoardVo)request.getAttribute("bv");  // 강제형변환 양쪽형을 맞춰준다
    
    
    String memberName = "";
    if(session.getAttribute("memberName")!=null){
    	memberName = (String)session.getAttribute("memberName");
    	
    	
    }
    int midx = 0;
    if(session.getAttribute("midx")!=null){
    	midx = Integer.parseInt(session.getAttribute("midx").toString());
    	
    	
    }
    
    %>
    
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<link href="/resources/css/style2.css" rel = "stylesheet">

<script>
//제이쿼리는 함수명이 앞으로
// jquery 로 만드는 함수
// read밖에 생성
<%-- $.boardCommentList = function(){   지피티 코드
    $.ajax({
        type: "get",
        url: "<%=request.getContextPath()%>/comment/commentList.aws?bidx=<%=bv.getBidx()%>",
        dataType: "json",
        success: function(result){
            console.log(result);  // 결과 로그 출력
            let commentList = $("#commentList");
            commentList.empty();
            for(let i=0; i<result.length; i++){
                let comment = result[i];
                let commentHtml = "<div><strong>" + comment.cidx + comment.cwriter + "</strong> (" + comment.writeday + ")<br>" + comment.ccontents +"<br>"+ comment.delyn + "</div>";
                commentList.append(commentHtml);
            }
        },
        error: function(xhr, status, error){
            console.log(xhr.responseText);  // 에러 내용 로그 출력
            alert("댓글 로딩 실패: " + error);
        }
    });
} --%>

function checkImageType(fileName){
	
	var pattern = /jpg$|gif$|png$|jpeg$/i;  //자바스크립트의 정규표현식
	
	
	return fileName.match(pattern);
}

function getOriginalFileName(fileName){   //실제 원본 파일이름추출
	
	var idx = fileName.lastIndexOf("_")+1;
	
	return fileName.substr(idx);
}

function getImageLink(fileName){ //실제이미지 링크 가져오기
	
	var front = fileName.substr(0,12);
	
	var end = fileName.substr(14);
	
	return front+end;
}

 function download(){
	//주소사이에s-는 빼고
	var downloadImage = getImageLink("<%=bv.getFilename()%>"); 
	var downLink = "<%=request.getContextPath()%>/board/displayFile.aws?fileName="+downloadImage+"&down=1";
	
	return downLink;
}






function commentDel(cidx){
	
	let ans = confirm("삭제하시겠습니까?");
	
	if(ans == true){
		$.ajax({
			type :  "get",    //전송방식
			url : "<%=request.getContextPath()%>/comment/"+cidx+"/commentDeleteAction.aws",
			dataType : "json",       // json타입은 문서에서  {"키값" : "value값","키값2":"value값2"}
			success : function(result){   //결과가 넘어와서 성공했을 받는 영역
				//alert("전송성공 테스트");				
				//alert(result.value);
				$.boardCommentList();
			},
			error : function(){  //결과가 실패했을때 받는 영역						
				alert("전송실패");
			}			
		});			
		
		
	}
		
	return;
}





$.boardCommentList = function(){
	
	//alert("test");
	let block = $("#block").val();
	//alert("block : "+ block);
	
	
	//alert("ddddddd");
	$.ajax({
		type :  "get",    //전송방식
		url : "<%=request.getContextPath()%>/comment/<%=bv.getBidx()%>/"+block+"/commentList.aws",
		dataType : "json",       // json타입은 문서에서  {"키값" : "value값","키값2":"value값2"}
		success : function(result){   //결과가 넘어와서 성공했을 받는 영역
			//alert("전송성공 테스트");			
		
		if(result.moreView == "N"){
			$("#morebtn").css("display","none");  // 감춘다
			
		}else{
			$("#morebtn").css("display","block"); // 보여준다
			
		}
		
		
		
		var strTr = "";				
		$(result.clist).each(function(){			
			
			var btnn = "";
			//현재 로그인 사람과 댓글쓴 사람의 번호가 같을때만 나타내준다
			if(this.midx == "<%=midx%>"){  
			if(this.delyn =="N"){
				btnn = "<button type = 'button' onclick = 'commentDel("+this.cidx+");'>삭제</button>"
			}
			}
			
			strTr = strTr + "<tr>"
			+"<td>"+this.cidx+"</td>"
			+"<td>"+this.cwriter+"</td>"
			+"<td class='content'>"+this.ccontents+"</td>"
			+"<td>"+this.writeday+"</td>"
			+"<td>"+btnn+"</td>"
			+"</tr>";					
		});		       
		
		var str  = "<table class='replyTable'>"
			+"<tr>"
			+"<th>번호</th>"
			+"<th>작성자</th>"
			+"<th>내용</th>"
			+"<th>날짜</th>"
			+"<th>DEL</th>"
			+"</tr>"+strTr+"</table>";		
		
		$("#commentListView").html(str);		
						
		},
		error : function(){  //결과가 실패했을때 받는 영역						
			alert("전송실패");
		}			
	});	
}






$(document).ready(function(){
	
	$("#dUrl").html(getOriginalFileName("<%=bv.getFilename()%>"));
	
	$("#dUrl").click(function(){
		$("#dUrl").attr("href",download());
		return;
		
	});
	
	
	$.boardCommentList();
	
		
	$("#btn").click(function(){
		//alert("추천버튼 클릭");		
	
		$.ajax({
			type :  "get",    //전송방식
			url : "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>",
			dataType : "json",       // json타입은 문서에서  {"키값" : "value값","키값2":"value값2"}
			success : function(result){   //결과가 넘어와서 성공했을 받는 영역
				//alert("전송성공 테스트");	
			
				var str ="추천("+result.recom+")";			
				$("#btn").val(str);			
			},
			error : function(){  //결과가 실패했을때 받는 영역						
				alert("전송실패");
			}			
		});			
	});	
	
	
	
	
	$("#cmtBtn").click(function(){
		
		
		let midx = "<%=midx%>";
		if(midx == ""|| midx == "null" || midx == null|| midx == 0){
			alert("로그인을 해주세요");
			return;
		}
		
		
		let cwriter = $("#cwriter").val()
		let ccontents = $("#ccontents").val()
		
		if(cwriter == "" ){
			alert("작성자를 입력해주세요");
			$("#cwriter").focus();
			return;
			
			
		}else if(ccontents == ""){
			alert("내용을 입력해주세요");
			$("#ccontents").focus();
			return;
		}
		
		
		
		$.ajax({
			type :  "post",    //전송방식
			url : "<%=request.getContextPath()%>/comment/commentWriteAction.aws",
			data : {"cwriter" : cwriter, 
				    "ccontents" : ccontents, 
				    "bidx" :"<%=bv.getBidx()%>", 
				    "midx" : "<%=midx%>"},     
				        
			dataType : "json",	        
			success : function(result){   //결과가 넘어와서 성공했을 받는 영역
				//alert("전송성공 테스트");	
				//var str ="("+result.value+")";			
				//alert(str);
				$.boardCommentList();
				if(result.value == 1){
					$("#ccontents").val("");
					
				}
			
			
			
				
			},
			error : function(){  //결과가 실패했을때 받는 영역						
				alert("전송실패");
			}			
		});			
    });
	
	

	
	
});



</script>
</head>
<body name = "bd">
	<h3>글내용</h3>
	<hr id = "top">
	<div><th id = "contentsTitle"><%=bv.getSubject() %>(조회수:<%=bv.getViewcnt() %>)</th><br>
	<input type="button" id="btn" value="추천(<%=bv.getRecom() %>)">
	
		</div>
		<div>
		<%=bv.getContents()%>
		</div>
		
		
	<hr id = "mid">
	<hr id = "mid">
	<%if(bv.getFilename()==null|| bv.getFilename().equals("")){}else{ %>
	<img src="<%=request.getContextPath()%>/board/displayFile.aws?fileName=<%=bv.getFilename() %>">
	<p>
	<a id="dUrl" href= "#" class = "fileDown">
	첨부파일 다운로드
	</a></p>
	<%} %>
	<hr id = "battom">
	<div> <a class = "btn aBtn" id = "contentsBtn"  href = "<%=request.getContextPath() %>/board/boardModify.aws?bidx=<%=bv.getBidx()%>">수정</a>
	<a class = "btn aBtn" id = "contentsBtn" href = "<%=request.getContextPath() %>/board/boardDelete.aws?bidx=<%=bv.getBidx()%>">삭제</a>
	<a class = "btn aBtn" id = "contentsBtn" href = "<%=request.getContextPath() %>/board/boardReply.aws?bidx=<%=bv.getBidx()%>">답변</a>
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardList.aws">목록</a></div>
	
	<input type = "text" id = "cwriter" name = "cwriter" value = "<%=memberName%>" readonly = "readonly" style = "width:100px;">
	<input type = "text" id = "ccontents" name ="ccontnents">
	<button type = "button" id = "cmtBtn" >댓글쓰기</button>
	<div id="commentListView"></div>
	
	<input type = "hidden" id = "block" value = "1">
	<div id = "morebtn" style = "text-align:center; line-height:50px;">
		<button type = "button" id = "more">더보기</button>
	</div>





</body>
</html>