<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /shop1/src/main/webapp/WEB-INF/view/chat/chat.jsp --%>    
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<c:set var="port" value="${pageContext.request.localPort}" /><%-- 포트번호 :  8080 --%>
<c:set var="server" value="${pageContext.request.serverName}" /><%-- IP주소 : localhost --%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Weather Api 호출 예제</title>


<script type="text/javascript">
$(function(){
	   weather() //수출입은행 환율 정보 조회. 서버에서 배열로 전송받아서 화면 출력하기
})

function weather() {
	   $.ajax("${path}/weather/weatherApi",{ // Map로 데이터 수신
		   success : function(json) {
			   console.log(json)
			   console.log(json[0])
			   console.log(json[0].obsrValue)
			   let html = "<h4 class='w3-center'>단기예보 API<br>"+json[0].baseDate+"</h4>"
			   html += "<table class='w3-table-all w3-margin-right'>"
			   for(let i=0;i < json.length;i++) {
				   html += "<tr><td>"+json[i].category+"</td><td>"+json[i].obsrValue+"</td></tr>"
				   }
			   html += "</table>"
			   $("#weather").html(html)
			   
			   
			   
		   },
		   error : function(e) {
			   alert("weatherApi 조회시 서버 오류 발생 :" + e.status)
		   }
	   })
}
</script>
</head>
<body>
<h1>예제 출력입니다.</h1>
  <div>
     <div id="weather" style="margin: 6px;"></div>
  </div>
</body>
</html>