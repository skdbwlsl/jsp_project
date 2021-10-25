<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath}"/>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>delete.jsp<hr>
	id:${userId }<br>  <%--session은 그냥 이름으로 지정했다. 삭제했을 때  --%>
	id : ${sessionScope.userId }   <%--아이디가 나오는지  --%>
	<jsp:useBean id="dao" class = "com.care.root.member.dao.MemberDAO"/> <%--dao객체 만들기 --%>
	<c:set var = "result" value="${dao.delete(userId) }"/> <%--delete(삭제메소드에) id를 넘긴다(userId) --%>
	
	<c:choose>
	<c:when test="${result==1 }">    <%--if문 --%>
		<script type="text/javascript">
			alert('삭제 성공~!!');
			location.href = 'membership.jsp'  <%--전체적으로 보여줄 페이지로 이동 --%>
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			alert('삭제 실패~!!');
			location.href = 'member_info.jsp?id=${dto.getId()}' <%--개인정보로 다시가서 수정하도록 --%>
		</script>
	</c:otherwise>
</c:choose>

</body>
</html>