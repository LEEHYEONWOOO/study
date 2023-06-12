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
       <input value="" name="zscode" id="zscode">
       
   <input type="button" value="시티코드" onclick="ecclocationApi()"/>
   
   <div class="w3-container">
     <div>
        <table id="weather" class="w3-table-all">
        
        </table>
     </div>
  </div>
  
 <script type="text/javascript">
 $(function(){
    getcity()
     //getText2('si2') 위에 getcity안쓰고 이거만 쓸수있는 방법이있을거같음;;
})
 
 function ecclocationApi() {
	 params = "zscode=" + document.getElementById('zscode').value;
	 $("#weather *").remove();
			$.ajax({
				url : "${path}/api/ecclocationApi",
				type : "POST",
				data : params,
         success : function(data) {
        	 console.log(data)
              let table = '<caption>'+$("select[name=si2]").val()+' '+$("select[name=gu2]").val()+'</caption><tr><td>충전소명</td><td>충전기타입</td><td>주소</td><td>이용가능시간</td><td>운영기관연락처</td></tr>';
              $.each(data, function(i){
            	  
                  let chgerType = data[i].chgerType.replace(/(01|02|03|04|05|06|07|89)/g, function(ex){
                      switch(ex){
                       case "01" : return "DC차데모";
                       case "02" : return "AC완속";
                       case "03" : return "DC차데모+AC3상";
                       case "04" : return "DC콤보";
                       case "05" : return "DC차데모+DC콤보";
                       case "06" : return "DC차데모+AC3상+DC콤보";
                       case "07" : return "AC3상";
                       case "89" : return "H2";
                      }
                })
                
                 table += '<tr><td>'+data[i].statNm+'</td><td>'+chgerType+'</td><td>'+data[i].addr+'</td><td>'+data[i].useTime+'</td><td>'+data[i].busiCall+'</td></tr>';
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
			$('input[name=zscode]').attr('value','')
			gu=null
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
					placecode(si2,gu2)
				}
			}
		})
	}
	
	 function placecode(si2,gu2){	//서버에서 문자열로 전달 받기
		 let city = $("select[name='si2']").val();
		let gu = $("select[name='gu2']").val();
		 params = "si2=" + city.trim() + "&gu2=" + gu.trim();
			$.ajax({
				url : "${path}/api/placecode",
				data : params,
				type : "POST",
				success : function(data){
					console.log(data)
					console.log(data[1].row[0].region_cd.substr(0,5));
					$('input[name=zscode]').attr('value',data[1].row[0].region_cd.substr(0,5))
				}
			})
		}
	
</script> 
</body>
</html>