<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" /> 
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>openAPI</title>
   <script>
function ajaxCall() {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "/controller/test2");
  xhr.onload = function() {
    if (xhr.status === 200) {
      var jsonObject = JSON.parse(xhr.responseText);
      // jsonObject를 처리합니다.
    } else {
      // 에러 처리
      alert("아님")
    }
  };
  xhr.send();
}
</script>
<script>
var jsonObject = JSON.stringify(jsonObject);
</script>

</head>
<body>
<input type="button" value="ajax 호출" onclick="ajaxCall();">
<p>이름: ${jsonObject}</p>
===데이터===
<br>
${sb}
${jsonsb}
<br>
===데이터===
  <table>
        <tbody class="table_body">
        </tbody>
    </table>
</body>
</html>