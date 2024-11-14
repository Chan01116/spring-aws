<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>




<c:if test="${!empty msg}">
    <script>
        alert('<c:out value="${msg}" />');
    </script>
</c:if>
<c:if test="${not empty msg}">
    <script>
        alert('${msg}');
    </script>
</c:if>
<%-- <%
	String msg = "";
	if(request.getAttribute("msg")!= null){ 
	msg = (String)request.getAttribute("msg");
	}
	
	
	if(msg!=""){
		out.print("<script>alert('"+msg+"');</scrip>");
		
	}
%>     --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link href="${pageContext.request.contextPath}/resources/css/style2.css" rel = "stylesheet">
<script>



function check(){
	let fm = document.frm;
	  	  		  
	  if(fm.subject.value == ""){
		  alert("제목을 입력해주세요");
		  fm.subject.focus();
		  return;
	  }else if(fm.contents.value == ""){
		  alert("내용을 입력해주세요");
		  fm.contents.focus();
		  return;
		  
	  }else if(fm.writer.value == ""){
		  alert("작성자를 입력해주세요");
		  fm.writer.focus();
		  return;
		
	  }else if(fm.password.value == ""){
		  alert("비밀번호를 입력해주세요");
		  fm.password.focus();
		  return;
				  
	  }
	  
	  let ans = confirm("저장하시겠습니까?");  // 함수의 값을 참과 거짓 true false로 나눈다
	  
	  if(ans == true){
		  fm.action ="${pageContext.request.contextPath}/board/boardWriteAction.aws";
		  fm.method = "post";
		  fm.enctype = "multipart/form-data";  // 데이터도 올려야 하니까
		  fm.submit();
		  
	  }
	  
	  
	 
	  return;
	 
}

</script>

</head>
<body>
<form name = "frm">
<h3>글쓰기</h3>
<hr id = "tod">
	<div>제목 <input type ="text" name = "subject"> </div>
	<hr id = "mid">
	<div>내용 <input type ="text" name = "contents"> </div>
	<hr id = "mid">
	<div>작성자 <input type ="text" name = "writer"> </div>
	<hr id = "mid">
	<div>비밀번호 <input type ="password" name = "password"> </div>
	<hr id = "mid">
	<div>첨부파일 <input type="file" name="attachfile">파일선택</button></div>  <!--바이너리 타입-->
	<hr id = "mid">
	<div> <button type = "button" name = "save" onclick = "check();">저장</button> <button type = "button" name = "cancle" onclick = "history.back();">취소</button> </div>
	<table>
		<td id = "NoTop">번호</td>
		<td id = "WriterTop">작성자</td>
		<td id = "TitleTop">내용</td>
		<td id = "WritedayTop">날짜</td>
	</table>

</form>
</body>
</html>