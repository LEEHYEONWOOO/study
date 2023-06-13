<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!-- /shop1/src/main/webapp/WEB-INF/view/main/home.jsp -->     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Electric Car</title>
<style>
</style>
</head>
<body>
<div id="img_main" style="display:none; margin-bottom:300px;">
	<img src="${path}../img/test.gif" alt="Image" style="width:100%">
</div>

<div id="img_map" style="display:none; margin-bottom: 200px;">
	<div class="container" style="display: flex; justify-content: center;">
		<div class="inner1">
			<img src="${path}../img/kakaomap_icon.png" alt="Image" style="width:300px;">
		</div>
		<div class="inner2" style="width:800px; padding: 70px 0px 0px 170px;">
			<h2 style="color: #0a0a0a;">Services</h2>
			<p>카카오맵을 기반으로<br>사용자에게 위치를 제공합니다.</p>
			<a href="${path}../place/location">바로가기</a>
		</div>
	</div>
</div>

<div id="img_pay" style="display:none; margin-bottom: 200px;">
	<div class="container" style="display: flex; justify-content: center;">
		<div class="inner2" style="width:800px; padding: 70px 0px 0px 480px;">
			<h2 style="color: #0a0a0a;">Services</h2>
			<p>카카오맵을 기반으로<br>사용자에게 위치를 제공합니다.</p>
			<a href="${path}../user/login">바로가기</a>
		</div>
		<div class="inner1">
			<img src="${path}../img/kakaomap_icon.png" alt="Image" style="width:300px;">
		</div>
	</div>
</div>

<script>
$(document).ready(function () {
	$("#img_main").fadeIn(1000);
  });
  
$(window).scroll(function() {
	  if ($(window).scrollTop() > 500) {
	    $("#img_map").fadeIn(1000);
	  }
	  if ($(window).scrollTop() > 900) {
	    $("#img_pay").fadeIn(1000);
	  }
	});
</script>
</body>
</html>