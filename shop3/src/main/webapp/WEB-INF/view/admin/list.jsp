<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록</title>
<script type="text/javascript">
	function allchkbox(allchk){
		//getElementsByName:tag의 name 속성이 idchks인 태그목록을 전달 (배열로 전달)
//		var chks = document.getElementsByName("idchks")
//		for(var i=0;i<chks.length;i++){
//			chks[i].checked = allchk.checked
//		}
	//jquery 방식
		$("input[name=idchks]").prop("checked",allchk.checked)
//		$(".idchks").attr("checked",allchk.checked)
	}
</script>
</head>
<body>
<form action="mailForm.shop" method="post">
<table>
	<tr><td colspan="7">회원목록</td></tr>
	<tr><th>아이디</th><th>이름</th><th>전화</th><th>생일</th>
		<th>이메일</th><th>&nbsp;</th>
		<th><input type="checkbox" name="allchk" onchange="allchkbox(this)"></th></tr>
	<c:forEach items="${list}" var="user">
	<tr><td>${user.userid}</td><td>${user.username}</td>
		<td>${user.phoneno}</td>
		<td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/></td>
		<td>${user.email}</td>
		<td><a href="../user/update.shop?id=${user.userid}">수정</a>
			<a href="../user/delete.shop?id=${user.userid}">강제탈퇴</a>
			<a href="../user/mypage.shop?id=${user.userid}">회원정보</a></td>
		<td><c:if test="${user.userid ne 'admin'}">
				<input type="checkbox" name="idchks" value="${user.userid}">
			</c:if></td></tr>
	</c:forEach>
	<tr><td colspan="7"><input type="submit" value="메일보내기"></td></tr>
</table>
</form>
</body>
</html>