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
var date = $("#date").val();
var location = $("#location").val();
var param = {"date":date, "location":location}

$(function(){
      weather() //수출입은행 환율 정보 조회. 서버에서 배열로 전송받아서 화면 출력하기
})

function weather() {
      $.ajax("${path}/api/weatherApi",{ // Map로 데이터 수신
    	  success : function(data) {
              let table = '<caption>'+data[0].baseDate+'</caption>';
              $.each(data, function(i){
                 let category = data[i].category.replace(/(PTY|REH|RN1|T1H|UUU|VEC|VVV|WSD)/g, function(ex){
                       switch(ex){
                        case "PTY" : return "test1";
                        case "REH" : return "test2";
                        case "RN1" : return "test3";     
                        case "T1H" : return "test4";     
                        case "UUU" : return "test5";     
                        case "VEC" : return "test6";     
                        case "VVV" : return "test7";     
                        case "WSD" : return "test8";     
                       }
                 })
                      table += '<tr><td>' + category +'</td><td>' +data[i].obsrValue+ '</td></tr>';
                  });
              $("#weather").append(table)
           },
           error : function(e) {
              alert("날씨 조회시 서버 오류 발생 : "+e.status)
           }
        })
     }
</script>

</head>
<body>
<div>
  <select name="location">
  <option value="1">서울</option>
  <option value="2">경기</option>
  <option value="3">대전</option>
  <option value="4">대구</option>
  <option value="5">부산</option>
  </select>
</div>
<div>



<c:set var="today" value="<%=new java.util.Date()%>" />
<c:set var="tomorrow" value="<%=new java.util.Date(new java.util.Date().getTime()+60*60*24*1000)%>" />

<select id="date">
  <option value=<fmt:formatDate value="${today}" pattern="yyyyMMdd" />><fmt:formatDate value="${today}" pattern="yyyy년MM월dd일" /></option>
  <option value=<fmt:formatDate value="${tomorrow}" pattern="yyyyMMdd" />><fmt:formatDate value="${tomorrow}" pattern="yyyy년MM월dd일" /></option>
  </select>
  
</div>
<button id="button1" value="">
날씨검색
</button>
<h1>예제 출력입니다.</h1>
    <div class="w3-container">
     <div>
        <table id="weather" class="w3-table-all">
        
        </table>
        <button id="send">전달</button>
     </div>
  </div>
</body>
</html>