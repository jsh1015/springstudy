<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판목록</title>
<script>
	function listdo(page){
		document.searchform.pageNum.value=page;
		document.searchform.submit();
	}
</script>
</head>
<body>
<table>
	<tr><td colspan="5">
		<div style="display : inline">
			<form action="list.shop" method="post" name="searchform">
				<input type="hidden" name="pageNum" value="1">
				<select name="searchtype" style="width:100px">
					<option value="">선택하세요</option>
					<option value="subject">제목</option>
					<option value="name">작성자</option>
					<option value="content">내용</option>
				</select>
				<script>
					document.searchform.searchtype.value="${param.searchtype}";
				</script>
				<input type="text" name="searchcontent" value="${param.searchcontent}" style="width:250px;">
				<input type="submit" value="검색">
			</form>
		</div>
</table>
<table>
	<c:if test="${listcount > 0 }">
		<tr><td colspan="4">Spring 게시판</td><td>글개수 : ${listcount}</td></tr>
		<tr><th width="8%">번호</th><th width="50%">제목</th>
			<th width="14%">작성자</th><th width="17%">등록일</th>
			<th width="11%">조회수</th>
		</tr>
		<c:forEach items="${boardlist}" var="board">
			<tr>
				<td>${boardno}</td><c:set var="boardno" value="${boardno-1 }"/>
				<td style="text-align:left">
					<c:if test="${!empty board.fileurl}"><%--null이거나 비어있는 경우 empty --%>
						<a href="file/${board.fileurl }" style="text-decoration:none;">@</a>
					</c:if>
					<c:if test="${empty board.fileurl}">
						&nbsp;&nbsp;&nbsp; 
					</c:if>
					<%--답글 표시하기 --%>
					<c:forEach begin="1" end="${board.grplevel}">
						&nbsp;&nbsp;&nbsp;
					</c:forEach>
					<c:if test="${board.grplevel>0}"><%--답변글인 경우 들여쓰기 --%>
						└  <%-- ㅂ+한자 --%>
					</c:if>
					<a href="detail.shop?num=${board.num}">${board.subject}</a>
				</td>
				<td>${board.name}</td>
				<td>${board.regdate}</td>
				<td>${board.readcnt}</td>
			</tr>
		</c:forEach> <!-- for구문 끝 --> 
		<tr><td colspan="5">
			<c:if test="${pageNum >1 }">
				<a href="javascript:listdo(${pageNum-1 })">[이전]</a>
			</c:if>
			<c:if test="${pageNum <=1}">[이전]</c:if>
			<c:forEach var="a" begin="${startpage }" end="${endpage}">
				<c:if test="${a==pageNum }"> 
						[${a}] 
				</c:if>
				<c:if test="${a!=pageNum }">
						<a href="javascript:listdo(${a})">[${a}]</a>
				</c:if>
			</c:forEach> 
			<%-- 다음페이지 없음 --%>
			<c:if test="${pageNum >= maxpage}">
				[다음]
			</c:if>
			<c:if test="${pageNum < maxpage}">
					<a href="javascript:listdo(${pageNum+1 })">[다음]</a>
			</c:if>
		</td></tr>
	</c:if>
	<c:if test="${listcount == 0}">
		<tr><td colspan="5">등록된 게시글이 없습니다.</td></tr>
	</c:if>
	<tr><td colspan="5" style="text-align:right">
		<a href="write.shop">[글쓰기]</a></td>
	</tr> 
	</table>
</body>
</html>