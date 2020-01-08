<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<c:set var="port" value="${pageContext.request.localPort}"/>
<c:set var="server" value="${pageContext.request.serverName}"/>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function(){
		var ws = new WebSocket("ws://${server}:${port}${path}/chatting.shop")
		//서버와 연결 될 때 호출
		ws.onopen = function(){
			$("#chatStatus").text("info:connection opened")
			
			//chatInput 태그에 이벤트 등록
			$("input[name=chatInput]").on("keydown",function(evt){ //키가 눌려졌을때의 이벤트 등록
				//evt : 키이벤트 객체
				if(evt.keyCode == 13){ //enter 키의 코드값
					var msg = $("input[name=chatInput]").val()
					ws.send(msg) //입력된 메세지 서버로 전송
					$("input[name=chatInput]").val("") //입력된 내용 지움
				}
			})
		}
		//서버에서 메세지 수신한 경우
		ws.onmessage = function(event){
			//prepend() : 태그의 앞에 메세지 출력
			$("textarea").eq(0).prepend(event.data+"\n")
		}
		//서버 연결 해제 된 경우
		ws.onclose = function(event){ //****로그인부분이랑 꼬임
			$("#chatStatus").text("info:connection close");
		}
	})
</script>
</head>
<body>
<p>
<div id="chatStatus"></div>
<textarea rows="15" cols="40" name="chatMsg" readonly></textarea><br>
메세지 입력 : <input type="text" name="chatInput">
</body>
</html>