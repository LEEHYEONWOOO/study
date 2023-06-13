<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<%-- /shop1/src/main/webapp/WEB-INF/view/chat/chat.jsp --%>

<c:set var="port" value="${pageContext.request.localPort}" /> <%-- 포트번호 : 8080 --%>
<c:set var="server" value="${pageContext.request.serverName}" /> <%-- IP주소 : localhost --%>
<c:set var="path" value="${pageContext.request.contextPath}" /> <%-- IP주소 : localhost --%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>

<script type="text/javascript">
	$(function() {
		let ws = new WebSocket("ws://${server}:${port}${path}/chatting")
		
		ws.onopen = function() {
			$("#chatStatus").text("info:connection opened")
			$("input[name=chatInput]").on("keydown",function(evt){
				if(evt.keyCode == 13) { // 13 : enter key
					let msg = $("input[name=chatInput]").val()
					ws.send(msg)
					$("input[name=chatInput]").val("")
				}
			})
		}
		ws.onmessage = function(event) { // 서버에서 메세지를 수신 한 경우
			$("textarea").eq(0).prepend(event.data+"\n") // append로 반대로 사용 가능
			// event.data : 수신된 메세지 정보, prepend : 앞쪽에 추가
		}
		ws.onclose = function(event){
			$("#chatStatus").text("info:connection close")
		}
	})
</script>

</head>
<body>
<p><div id="chatStatus"></div>
	<textarea name="chatMsg" rows="15" cols="40" class="w3-input" style="width:385px;"></textarea>
	<br><br>
	<input type="text" name="chatInput" class="w3-input" style="width:385px;" placeholder="메세지 입력">
</body>
</html>