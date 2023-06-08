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
      <div><span id="si2">
       <select name="si2" onchange="getText2('si2')">
          <option value="">시도를 선택하세요</option>
       </select></span>
      <span id="gu2">
       <select name="gu2" onchange="getText2('gu2')">
          <option value="">구군을 선택하세요</option>
       </select></span>
      </div>
       
   <input type="button" value="시티코드" onclick="place()"/>
  
 <script type="text/javascript">
 $(function(){
     //place() //수출입은행 환율 정보 조회. 서버에서 배열로 전송받아서 화면 출력하기
     getcity()
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
 
 
 
 
 function getcity(){	//서버에서 문자열로 전달 받기
		$.ajax({
			url : "${path}/api/select",
			success : function(data){
				console.log(data)
				let arr = data.substring(data.indexOf('[')+1,data.indexOf(']')).split(",");
				$.each(arr,function(i,item){
					// i : 인덱스. 첨자. 0부터 시작
					// item: 배열의 요소
					$("select[name=si2]").append(function(){
						return "<option>"+item+"</option>"
					})
				})
			}
		})
	}
 
	function getText2(name){
		let city = $("select[name='si2']").val();
		let gu = $("select[name='gu2']").val();
		let disname;
		let toptext="구군을 선택하세요";
		let params="";
		if(name == "si2"){
			params = "si2="+city.trim();
			disname = "gu2";
		} else{
			return;
		}
		alert(params)
		$.ajax({
			url : "${path}/api/selectText",
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
</script> 
</body>
</html>