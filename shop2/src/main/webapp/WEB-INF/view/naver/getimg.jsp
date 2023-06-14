<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
$(function(){
    //place() //수출입은행 환율 정보 조회. 서버에서 배열로 전송받아서 화면 출력하기
    getimg1()
})


function getimg1() {
    $.ajax("https://www.seoul.co.kr/news/newsView.php?id=20230614500090&wlog_sub=svt_006",{ // Map로 데이터 수신
       success : function(data) {
    	   //console.log(data)
    	   const img = document.querySelector('body img');
    	   const imgUrl = img.src;
    	   console.log(imgUrl+"이게맞다고?");
         },
         error : function(e) {
            alert("place 조회시 서버 오류 발생 : "+e.status)
         }
      })
   }
   
function getimg2() {
	const img = document.querySelector('img');
	const imgUrl = img.src;
	console.log(imgUrl);
}
function getimg3() {
	var img = document.getElementsByTagName("img");
	alert(img.getAttribute("img"));
}
</script>
</head>
<body>
<input type="button" value="이미지도둑1" onclick="getimg2()"/>
<input type="button" value="이미지도둑2" onclick="getimg3()"/>
콘솔을봐라 콘솔을
</body>
</html>