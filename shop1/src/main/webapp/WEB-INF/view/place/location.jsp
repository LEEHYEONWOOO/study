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
       <input value="" name="sinum" id="sinum">
       <input value="" name="gunum" id="gunum">
       
   <input type="button" value="시티코드" onclick="place()"/>
   
   <div class="w3-container">
     <div>
        <table id="weather" class="w3-table-all">
        
        </table>
     </div>
  </div>
  
 <script type="text/javascript">
 $(function(){
     //place() //수출입은행 환율 정보 조회. 서버에서 배열로 전송받아서 화면 출력하기
     getcity()
})
 
 function place() {
	 alert("si2="+document.getElementById('sinum').value + "&gu2=" + document.getElementById('gunum').value)
	 params = "si2="+document.getElementById('sinum').value + "&gu2=" + document.getElementById('gunum').value;
	 $("#weather *").remove();
	 /*
	 $.ajax("${path}/api/placeApi",{ // Map로 데이터 수신*/
			$.ajax({
				url : "${path}/api/placeApi",
				type : "POST",
				data : params,
         success : function(data) {
        	 console.log(data)
              let table = '<caption>'+$("select[name=si2]").val()+' '+$("select[name=gu2]").val()+'</caption><tr><td>상호명</td><td>주소</td><td>차종</td></tr>';
              $.each(data, function(i){
                 table += '<tr><td>'+data[i].stnPlace+'</td><td>'+data[i].stnAddr+'</td><td>'+data[i].carType+'</td></tr>';
              });
              $("#weather").append(table)
           },
           error : function(e) {
              alert("충전소 찾다가 에러발생 : "+e.status)
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
		}else if(name == "gu2"){
			params = "si2=" + city.trim() + "&gu2=" + gu.trim();
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
				if(city!=null && gu!=null && city!="" && gu!= ""){ 
					//$("#sinum").val=arr.(0)
					alert("sinum : "+arr[0]+", gunum : "+arr[1])
					$('input[name=sinum]').attr('value',arr[0])
					$('input[name=gunum]').attr('value',arr[1])
				}
			}
		})
		
	}
</script> 
</body>
</html>