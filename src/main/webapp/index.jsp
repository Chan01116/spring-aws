<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <!-- 항상 맨위에 있어야 함 -->
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스프링 학습하기</title>
</head>
<body>
<%-- <%
if(session.getAttribute("midx") != null){
	out.print(session.getAttribute("memberName") + "<a href='"+request.getContextPath()+"/member/memberLogout.aws'>로그아웃</a>");
}
%> --%>

<!-- 값이 비어있으면 midx!=null 둘다 가능 -->
<c:if test="${!empty sessionScope.midx}">
${memberName}&nbsp;
<a href='${pageContext.request.contextPath }/member/memberLogout.aws'>로그아웃</a>
</c:if>
<br>
<a href ="${pageContext.request.contextPath }/member/memberJoin.aws">회원가입 페이지</a>
<br>
<a href ="${pageContext.request.contextPath}/member/memberLogin.aws">회원로그인 페이지</a>
<br>
<a href ="${pageContext.request.contextPath}/member/memberList.aws">회원 목록가기</a>
<br>
<a href ="${pageContext.request.contextPath}/board/boardList.aws">게시판 목록가기</a>
</body>
</html>