<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.place_box {
  margin-top: 100px;
}

.box1, .box2 {
  display: inline-block;
  background-color : yellow;
  width: 50%;
  height:500px;
}
</style>
</head>

<body>
<div class="place_box">
	<div class="box1">
		<a href="${path}../place/map">충전소 위치 찾기</a>
	</div>
	<div class="box2">
		<a href="${path}../place/map">충전소 위치 찾기</a>
	</div>
</div>
</body>
</html>