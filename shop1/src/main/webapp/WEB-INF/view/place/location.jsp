<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>충전소 정보.</h1>
    <div class="w3-container">
     <div>
        <table id="place" class="w3-table-all" style="width:30%;">
        
        </table>
        <button id="send">전달</button>
     </div>
  </div>
  
   <input type="button" value="시티코드" onclick="cityCode()"/>
  
 <script type="text/javascript">
 $(function(){
     place() //수출입은행 환율 정보 조회. 서버에서 배열로 전송받아서 화면 출력하기
     
})
 
 function place() {
     $.ajax("${path}/api/placeApi",{ // Map로 데이터 수신
        success : function(data) {
       	 console.log(data)
             
          },
          error : function(e) {
             alert("place 조회시 서버 오류 발생 : "+e.status)
          }
       })
    }
 
 function cityCode() {
     $.ajax("${path}/api/cityCodeApi",{ // Map로 데이터 수신
        success : function(data) {
       	 console.log(data)
             
          },
          error : function(e) {
             alert("place 조회시 서버 오류 발생 : "+e.status)
          }
       })
    }
</script> 
</body>
</html>