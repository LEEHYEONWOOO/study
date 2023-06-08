<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />    
<%-- /shop1/src/main/webapp/layout/gdulayout.jsp --%>    

<!DOCTYPE html>
<html>
<head>
<title><sitemesh:write property="title" /></title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script type="text/javascript" src= 
"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js">
</script>
<script type="text/javascript"
   src="http://cdn.ckeditor.com/4.5.7/standard/ckeditor.js">
</script>
<style>
html,body,h1,h2,h3,h4,h5 {font-family: "Raleway", sans-serif}
</style>
<sitemesh:write property="head" />
</head>
<body class="w3-light-grey">

<!-- Top container -->
<div class="w3-bar w3-top w3-black w3-large" style="z-index:4">
  <button class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-light-grey" onclick="w3_open();"><i class="fa fa-bars"></i> &nbsp;Menu</button>
  <span class="w3-bar-item w3-right">
	<c:if test="${empty sessionScope.loginUser}">
	 <a href="${path}/user/login">로그인</a>
	 <a href="${path}/user/join">회원가입</a>
	</c:if>   
	<c:if test="${!empty sessionScope.loginUser}">
	${sessionScope.loginUser.username}님이 로그인 하셨습니다.&nbsp;&nbsp;
	 <a href="${path}/user/logout">로그아웃</a>
	</c:if>   
  </span>
</div>
<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
  <div class="w3-container w3-row">
    <div class="w3-col s4">
    </div>
    <div class="w3-col s8 w3-bar">
      <c:if test="${!empty sessionScope.loginUser}">
      <span>반갑습니다, <strong>${sessionScope.loginUser.username}님</strong></span><br>
      </c:if>
      <c:if test="${empty sessionScope.loginUser}">
      <span><strong>로그인하세요</strong></span><br>
      </c:if>
    </div>
  </div>
  <hr>
  <div class="w3-bar-block">
    <a href="#" class="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black" onclick="w3_close()" title="close menu"><i class="fa fa-remove fa-fw"></i>&nbsp; Close Menu</a>
    <a href="${path}/user/mypage?userid=${loginUser.userid}" 
    class="w3-bar-item w3-button w3-padding <c:if test='${url == "user"}'>w3-blue</c:if>">
    <i class="fa fa-users fa-fw"></i>&nbsp; 회원관리</a>
    <a href="${path}/item/list" 
    class="w3-bar-item w3-button w3-padding <c:if test='${url == "item"}'>w3-blue</c:if>">
    <i class="fa fa-eye fa-fw"></i>&nbsp; 상품관리</a>
    <a href="${path}/chat/chat" 
    class="w3-bar-item w3-button w3-padding <c:if test='${url == "chat"}'>w3-blue</c:if>">
    <i class="fa fa-eye fa-fw"></i>&nbsp; 채팅하기</a>
    <hr>
    <a href="${path}/board/list?boardid=1" 
    class="w3-bar-item w3-button w3-padding <c:if test='${url == "board" && boardid=="1"}'>w3-blue</c:if>">
    <i class="fa fa-eye fa-fw"></i>&nbsp; 공지사항</a>
    <a href="${path}/board/list?boardid=2" 
    class="w3-bar-item w3-button w3-padding <c:if test='${url == "board" && boardid=="2"}'>w3-blue</c:if>">
    <i class="fa fa-eye fa-fw"></i>&nbsp; 자유게시판</a>
    <a href="${path}/board/list?boardid=3" 
    class="w3-bar-item w3-button w3-padding <c:if test='${url == "board" && boardid=="3"}'>w3-blue</c:if>">
    <i class="fa fa-eye fa-fw"></i>&nbsp; QnA</a>
  </div>
  
  <div>
  	<div id="exchange"></div>
  </div>
  
</nav>


<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px;margin-top:43px;">

  <!-- Header -->
  <header class="w3-container" style="padding-top:22px">
    <h5><b><i class="fa fa-dashboard"></i> My Dashboard</b></h5>
  </header>

  <div class="w3-row-padding w3-margin-bottom">
    <div class="w3-quarter">
      <div class="w3-container w3-red w3-padding-16">
        <div class="w3-left"><i class="fa fa-comment w3-xxxlarge"></i></div>
        <div class="w3-right">
          <h3>52</h3>
        </div>
        <div class="w3-clear"></div>
        <h4>Messages</h4>
      </div>
    </div>
    <div class="w3-quarter">
      <div class="w3-container w3-blue w3-padding-16">
        <div class="w3-left"><i class="fa fa-eye w3-xxxlarge"></i></div>
        <div class="w3-right">
          <h3>99</h3>
        </div>
        <div class="w3-clear"></div>
        <h4>Views</h4>
      </div>
    </div>
    <div class="w3-quarter">
      <div class="w3-container w3-teal w3-padding-16">
        <div class="w3-left"><i class="fa fa-share-alt w3-xxxlarge"></i></div>
        <div class="w3-right">
          <h3>23</h3>
        </div>
        <div class="w3-clear"></div>
        <h4>Shares</h4>
      </div>
    </div>
    <div class="w3-quarter">
      <div class="w3-container w3-orange w3-text-white w3-padding-16">
        <div class="w3-left"><i class="fa fa-users w3-xxxlarge"></i></div>
        <div class="w3-right">
          <h3>50</h3>
        </div>
        <div class="w3-clear"></div>
        <h4>Users</h4>
      </div>
    </div>
  </div>

  <div class="w3-panel">
  <sitemesh:write property="body" />
  </div>
  <hr>
  <!-- Footer -->
  <footer class="w3-container w3-padding-16 w3-light-grey">
    <h4>FOOTER</h4>
    <p>Powered by <a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    <hr>
    <div><span id="si">
       <select name="si" onchange="getText('si')">
          <option value="">시도를 선택하세요</option>
       </select></span>
      <span id="gu">
       <select name="gu" onchange="getText('gu')">
          <option value="">구군을 선택하세요</option>
       </select></span>
      <span id="dong">
       <select name="dong">
          <option value="">동리를 선택하세요</option>
       </select></span></div>
  </footer>

  <!-- End page content -->
</div>

<script>
// Get the Sidebar
var mySidebar = document.getElementById("mySidebar");

// Get the DIV with overlay effect
var overlayBg = document.getElementById("myOverlay");

// Toggle between showing and hiding the sidebar, and add overlay effect
function w3_open() {
  if (mySidebar.style.display === 'block') {
    mySidebar.style.display = 'none';
    overlayBg.style.display = "none";
  } else {
    mySidebar.style.display = 'block';
    overlayBg.style.display = "block";
  }
}

// Close the sidebar with the close button
function w3_close() {
  mySidebar.style.display = "none";
  overlayBg.style.display = "none";
}
</script>
<script type="text/javascript">
	$(function(){
		getSido2()
		exchangeRate2()
	})
	/*
	function getSido(){	//서버에서 리스트 객체를 배열로 직접 전달받음
		$.ajax({
			url : "${path}/ajax/select",
			success : function(arr){
				//arr : 서버에서 전달 받는 리스트 객체를 배열로 인식함
				$.each(arr,function(i,item){
					// i : 인덱스. 첨자. 0부터 시작
					// item: 배열의 요소
					$("select[name=si]").append(function(){
						return "<option>"+item+"</option>"
					})
				})
			}
		})
	}
	*/
	function getSido2(){	//서버에서 문자열로 전달 받기
		$.ajax({
			url : "${path}/ajax/select2",
			success : function(data){
				console.log(data)
				let arr = data.substring(data.indexOf('[')+1,data.indexOf(']')).split(",");
				$.each(arr,function(i,item){
					// i : 인덱스. 첨자. 0부터 시작
					// item: 배열의 요소
					$("select[name=si]").append(function(){
						return "<option>"+item+"</option>"
					})
				})
			}
		})
	}
	function getText(name){
		let city = $("select[name='si']").val();
		let gu = $("select[name='gu']").val();
		let disname;
		let toptext="구군을 선택하세요";
		let params="";
		if(name == "si"){
			params = "si="+city.trim();
			disname = "gu";
		}else if(name == "gu"){
			params = "si=" + city.trim() + "&gu=" + gu.trim();
			disname = "dong";
			toptext = "동리를 선택하세요";
		} else{
			return;
		}
		$.ajax({
			url : "${path}/ajax/select",
			type : "POST",
			data : params,
			success : function(arr){
				$("select[name="+disname+"] option").remove();
				$("select[name="+disname+"]").append(function(){
					return "<option value=''>"+toptext+"</option>"
				})
				$.each(arr,function(i,item){
					$("select[name="+disname+"]").append(function(){
						return "<option>"+item+"</option>"
					})
				})
			}
		})
		
	}
	/*
	function exchangeRate() {
		$.ajax("${path}/ajax/exchange",{
			success : function(data){
				console.log(data)
				$("#exchange").html(data)
			},
			error : function(e){
				alert("환율 조회시 서버 오류 발생 : "+ e.status)
			}
		})
	}
	*/
	function exchangeRate2() {
		$.ajax("${path}/ajax/exchange2",{
			success : function(json){
				console.log(json)
				let html = "<h4 class='w3-center'>수출입은행<br>"+json.exdate+"</h4>"
				html += "<table class='w3-table-all w3-margin-right'>"
				html += "<tr><th>통화</th><th>기준율</th><th>받으실때</th><th>보내실때</th><tr>"
				$.each(json.trlist,function(i,tds){
					html += "<tr><td>"+tds[0] + "<br>" +tds[1]+ "</td><td>"+tds[4]+"</td>"
						+"<td>"+tds[2]+"</td><td>"+tds[3]+"</td></tr>"
				})
				html += "</table>"
				$("#exchange").html(html)
			},
			
			error : function(e){
				alert("환율 조회시 서버 오류 발생 : "+ e.status)
			}
		})
	}
</script>
</body>
</html>