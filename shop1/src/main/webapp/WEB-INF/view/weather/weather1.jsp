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
      $.ajax("${path}/api/weatherApi",{ // Map로 데이터 수신
         success : function(data) {
        	 console.log(data)
              let table = '<caption>'+$("#date").val()+'/'+'</caption>';
              $.each(data, function(i){
                 let category = data[i].category.replace(/(POP|PTY|PCP|REH|SNO|SKY|TMP|TMN|TMX|UUU|VVV|WAV|VEC|WSD)/g, function(ex){
                       switch(ex){
                        case "POP" : return "강수확률";
                        case "PTY" : return "강수형태";
                        case "PCP" : return "1시간강수량";
                        case "REH" : return "습도";
                        case "SNO" : return "1시간신적설";
                        case "SKY" : return "하늘상태";
                        case "TMP" : return "1시간기온";
                        case "TMN" : return "일최저기온";
                        case "TMX" : return "일최고기온";
                        case "UUU" : return "풍속";     
                        case "VVV" : return "풍속";     
                        case "WAV" : return "파고";     
                        case "VEC" : return "풍향";     
                        case "WSD" : return "풍속";     
                       }
                 })
                 	if(data[i].fcstDate=="20230608") {
                 		if(data[i].fcstTime=="0500") {
                      		table += '<tr><td>' + category +'</td><td>' +data[i].fcstValue+ '</td><td>' +data[i].fcstDate+ '</td><td>' +data[i].fcstTime+ '</td></tr>';
                 		}
                 	}
                  });
              $("#weather").append(table)
           },
           error : function(e) {
              alert("날씨 조회시 서버 오류 발생 : "+e.status)
           }
        })
     }
     
function weather2() {
    $.ajax("${path}/api/weatherApi",{ // Map로 데이터 수신
       success : function(data) {
    	   $("#weather *").remove();
            let table = '<caption>'+$('#date').val()+'/'+'</caption>';
            $.each(data, function(i){
               let category = data[i].category.replace(/(POP|PTY|PCP|REH|SNO|SKY|TMP|TMN|TMX|UUU|VVV|WAV|VEC|WSD)/g, function(ex){
                     switch(ex){
                      case "POP" : return "강수확률";
                      case "PTY" : return "강수형태";
                      case "PCP" : return "1시간강수량";
                      case "REH" : return "습도";
                      case "SNO" : return "1시간신적설";
                      case "SKY" : return "하늘상태";
                      case "TMP" : return "1시간기온";
                      case "TMN" : return "일최저기온";
                      case "TMX" : return "일최고기온";
                      case "UUU" : return "풍속";     
                      case "VVV" : return "풍속";     
                      case "WAV" : return "파고";     
                      case "VEC" : return "풍향";     
                      case "WSD" : return "풍속";     
                     }
               })
               	if(data[i].fcstDate==$('#date').val()) {
               		if(data[i].fcstTime==$('#time').val()) {
            			table += '<tr><td>' + category +'</td><td>' +data[i].fcstValue+ '</td><td>' +data[i].fcstDate+ '</td><td>' +data[i].fcstTime+ '</td></tr>';
             		}
            	}
             });
         $("#weather").append(table)
      },
      error : function(e) {
         alert("날씨 조회시 서버 오류 발생 : "+e.status)
      }
   })
}
     
		
	function weather3() { 
/*
var param = [
			{'data':$('#date').val(),
			'loc':$('#loc').val() }
	]*/
    let param = [{'loc':$('#loc').val(), 'date':$('#date').val()}]; 

   	$.ajax({
   	　　type:'post'
   	  
   	　　, contentType:'application/json'
   	　　, data: JSON.stringify(param)
   	　　//, url: "${path}/weather/weather2"
   	　　, url: "${path}/api/weatherApi"
   	　　, success: function(data) {
   		$("#weather").remove();
   	　　　　alert(data);
   	　　}, error:function(e) {
   	　　　　alert("error: " + e);
   	　　}
   	});
}
</script>

</head>
<body>
<div>
  <select id="loc">
  <option value="서서">서울</option>
  <option value="경경">경기</option>
  <option value="전전">대전</option>
  <option value="대대">대구</option>
  <option value="부부">부산</option>
  </select>
</div>
<div>



<c:set var="today" value="<%=new java.util.Date()%>" />
<c:set var="to1" value="<%=new java.util.Date(new java.util.Date().getTime()+60*60*24*1000)%>" />
<c:set var="to2" value="<%=new java.util.Date(new java.util.Date().getTime()+2*60*60*24*1000)%>" />
<c:set var="to3" value="<%=new java.util.Date(new java.util.Date().getTime()+3*60*60*24*1000)%>" />

<select id="date">
  <option value=<fmt:formatDate value="${today}" pattern="yyyyMMdd" />><fmt:formatDate value="${today}" pattern="yyyy년MM월dd일" /></option>
  <option value=<fmt:formatDate value="${to1}" pattern="yyyyMMdd" />><fmt:formatDate value="${to1}" pattern="yyyy년MM월dd일" /></option>
  <option value=<fmt:formatDate value="${to2}" pattern="yyyyMMdd" />><fmt:formatDate value="${to2}" pattern="yyyy년MM월dd일" /></option>
  <option value=<fmt:formatDate value="${to3}" pattern="yyyyMMdd" />><fmt:formatDate value="${to3}" pattern="yyyy년MM월dd일" /></option>
  </select>
  
  <select id="time">
  <option value="0000">00시00분</option>
  <option value="0100">01시00분</option>
  <option value="0200">02시00분</option>
  <option value="0300">03시00분</option>
  <option value="0400">04시00분</option>
  <option value="0500">05시00분</option>
  <option value="0600">06시00분</option>
  <option value="0700">07시00분</option>
  <option value="0800">08시00분</option>
  <option value="0900">09시00분</option>
  <option value="1000">10시00분</option>
  </select>
  
</div>
 <input type="button" value="날씨검색" onclick="weather2()"/>
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