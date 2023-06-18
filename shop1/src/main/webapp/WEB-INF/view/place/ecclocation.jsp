<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

    <style>
.map_wrap, .map_wrap * {margin:0;padding:0;font-family:'Malgun Gothic',dotum,'돋움',sans-serif;font-size:12px;}
.map_wrap a, .map_wrap a:hover, .map_wrap a:active{color:#000;text-decoration: none;}
.map_wrap {position:relative;width:100%;height:500px;}
#menu_wrap {position:absolute;top:0;left:0;bottom:0;width:250px;margin:10px 0 30px 10px;padding:5px;overflow-y:auto;background:rgba(255, 255, 255, 0.7);z-index: 1;font-size:12px;border-radius: 10px;}
.bg_white {background:#fff;}
#menu_wrap hr {display: block; height: 1px;border: 0; border-top: 2px solid #5F5F5F;margin:3px 0;}
#menu_wrap .option{text-align: center;}
#menu_wrap .option p {margin:10px 0;}  
#menu_wrap .option button {margin-left:5px;}
#placesList li {list-style: none;}
#placesList .item {position:relative;border-bottom:1px solid #888;overflow: hidden;cursor: pointer;min-height: 65px;}
#placesList .item span {display: block;margin-top:4px;}
#placesList .item h5, #placesList .item .info {text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
#placesList .item .info{padding:10px 0 10px 55px;}
#placesList .info .gray {color:#8a8a8a;}
#placesList .info .jibun {padding-left:26px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;}
#placesList .info .tel {color:#009900;}
#placesList .item .markerbg {float:left;position:absolute;width:36px; height:37px;margin:10px 0 0 10px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png) no-repeat;}
#placesList .item .marker_1 {background-position: 0 -10px;}
#placesList .item .marker_2 {background-position: 0 -56px;}
#placesList .item .marker_3 {background-position: 0 -102px}
#placesList .item .marker_4 {background-position: 0 -148px;}
#placesList .item .marker_5 {background-position: 0 -194px;}
#placesList .item .marker_6 {background-position: 0 -240px;}
#placesList .item .marker_7 {background-position: 0 -286px;}
#placesList .item .marker_8 {background-position: 0 -332px;}
#placesList .item .marker_9 {background-position: 0 -378px;}
#placesList .item .marker_10 {background-position: 0 -423px;}
#placesList .item .marker_11 {background-position: 0 -470px;}
#placesList .item .marker_12 {background-position: 0 -516px;}
#placesList .item .marker_13 {background-position: 0 -562px;}
#placesList .item .marker_14 {background-position: 0 -608px;}
#placesList .item .marker_15 {background-position: 0 -654px;}
#placesList .item .marker_16 {background-position: 0 -654px;} <!-- 15에 대한 기준? -->
#placesList .item .marker_17 {background-position: 0 -654px;} <!-- 15에 대한 기준? -->

#placesList .item .marker_17 {background-position: 0 -654px;}
#pagination {margin:10px auto;text-align: center;}
#pagination a {display:inline-block;margin-right:10px;}
#pagination .on {font-weight: bold; cursor: default;color:#777;}
</style>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.21/lodash.min.js" integrity="sha512-WFN04846sdKMIP5LKNphMaWzU7YpMyCU245etK3g/2ARYbPK9Ub18eG+ljU96qKRCWh+quCY7yefSmlkQw1ANQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script> <!-- locash메소드 -->
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
        <table id="placetable" class="w3-table-all">
        
        </table>
     </div>
  </div>
  
  <div class="map_wrap">
    <div id="map" style="width:100%;height:100%;position:relative;overflow:hidden;"></div>

    <div id="menu_wrap" class="bg_white">
        <div class="option">
        </div>
        <hr>
        <ul id="placesList"></ul>
        <div id="pagination"></div>
    </div>
</div>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=02d94db8e10d97b2ae5cfd31f23e9c4c&libraries=services"></script>
<script>
var mark_index=0;
// 마커를 담을 배열입니다
var markers = [];

var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };  

// 지도를 생성합니다    
var map = new kakao.maps.Map(mapContainer, mapOption); 

// 장소 검색 객체를 생성합니다
const ps = new kakao.maps.services.Places();  

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
var infowindow = new kakao.maps.InfoWindow({zIndex:1});


var maxCallCnt = 0;
var runCnt = 0;
const searchInMethod = (data2 ,status, pagination) => {
	runCnt++;
	if (status === kakao.maps.services.Status.OK) {  
        for(var j=0; j<data2.length; j++){
        	//console.log(pagination)
       		//console.log(data2)
        	//console.log(ercnt+'번 실행됨. 현재 실행중인 j  반복문 : '+j)
        	//console.log(data2[j].road_address_name + '++++++'+j)
        	//console.log(data2[j].id + '++++++'+j)
        	//console.log(data2[j].place_url + '++++++'+j)
        	//console.log(data2[j].id + '++++++'+j)
        	
	       // if(data[i].addr == data2[j].road_address_name){
        	//	alert(data[i].addr +"  ==   "+ data2[j].road_address_name)
	        //}
        }
        	searchArr.push(data2[0])
        	//console.log("data2[0]값이 들어갑니다. : ", data2[0]);
        	

        	
        	//console.log(typeof(data2[0]))
	   	        //displayPlaces(data2[0]);//0을 입력해서 맨처음꺼만 맵 입력했었음
			
	   	        
	   	        
	   	        //displayPlaces(searchArr);//0을 입력해서 맨처음꺼만 맵 입력했었음
	   	    	//console.log(searchArr)
        
        
        displayPagination(pagination);
	   	        
	   	console.log("maxCall cnt : ", maxCallCnt, " runCnt : ", runCnt);
    	if(maxCallCnt === runCnt) {
    		console.log("searchArr 제대로 나와야함. : ", searchArr);
    		displayPlaces(searchArr);
    		runCnt = 0;
    	}
		ercnt++
} else if (status === kakao.maps.services.Status.ZERO_RESULT) {
	ercnt++
    alert(data[i].statNm+'에 대한 검색 결과가 존재하지 않습니다.'+ercnt);
    return;

} else if (status === kakao.maps.services.Status.ERROR) {
	ercnt++
    alert('검색 결과 중 오류가 발생했습니다.'+ercnt);
    return;

}
}

// 키워드로 장소를 검색합니다
//searchPlaces();
searchArr = [];
// 키워드 검색을 요청하는 함수입니다
console.log("search places 1111111111111111");
function searchPlaces(data) {
    		ercnt=0;
    		
    		maxCallCnt = data.length;
    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    for (i=0; i<data.length; i++ ) {
    	console.log('i에 대한 for문 시작')
    	
    	
    	// searchInMethod
    	ps.keywordSearch(data[i].statNm, searchInMethod)
    	// 1000 난 필요없는데;;      -> 새로운 무언갈 창조~~~~
    	// 935  난 이게필요함 ;ㅣ;     ->  
    			
    	//for ( var i=0; i<1; i++ ) {
    	//ps.keywordSearch(data[i].statNm, placesSearchCB);
    	
    	/* ps.keywordSearch(data[i].statNm, function(data2, status, pagination) {//CB함수가 있던자리지만 화끈하게 날렸음
    		if (status === kakao.maps.services.Status.OK) {  
    	        
    	        for(var j=0; j<data2.length; j++){
    	        	//console.log(pagination)
   	        		//console.log(data2)
    	        	//console.log(ercnt+'번 실행됨. 현재 실행중인 j  반복문 : '+j)
    	        	//console.log(data2[j].road_address_name + '++++++'+j)
    	        	//console.log(data2[j].id + '++++++'+j)
    	        	//console.log(data2[j].place_url + '++++++'+j)
    	        	//console.log(data2[j].id + '++++++'+j)
    	        	
	    	       // if(data[i].addr == data2[j].road_address_name){
    	        	//	alert(data[i].addr +"  ==   "+ data2[j].road_address_name)
    		        //}
    	        }
    	        	searchArr.push(data2[0])
    	        	console.log("data2[0]값이 들어갑니다. : ", data2[0]);
    	        	
    	        	
    	        	//console.log(typeof(data2[0]))
 		   	        //displayPlaces(data2[0]);//0을 입력해서 맨처음꺼만 맵 입력했었음
					
 		   	        
 		   	        
 		   	        //displayPlaces(searchArr);//0을 입력해서 맨처음꺼만 맵 입력했었음
 		   	    	//console.log(searchArr)
    	        
    	        
    	        displayPagination(pagination);
				ercnt++
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
			ercnt++
            alert(data[i].statNm+'에 대한 검색 결과가 존재하지 않습니다.'+ercnt);
            return;

        } else if (status === kakao.maps.services.Status.ERROR) {
			ercnt++
            alert('검색 결과 중 오류가 발생했습니다.'+ercnt);
            return;

        } // else if
    	} // keywordSearch
	); */
	} // for
    //displayPlaces(searchArr);
	//console.log(JSON.stringify(searchArr))
	console.log("search places Done 222222");
	
	
	console.log(searchArr)
    //displayPlaces(searchArr);
    console.log(searchArr[3])
} // searchPlaces



		
    

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {
	console.log(places)
    var listEl = document.getElementById('placesList'), 
    menuEl = document.getElementById('menu_wrap'),
    fragment = document.createDocumentFragment(), 
    bounds = new kakao.maps.LatLngBounds(), 
    listStr = '';
    
    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();
    
    for ( var i=0; i<places.length; i++ ) {

        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i), 
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        (function(marker, title) {
            kakao.maps.event.addListener(marker, 'mouseover', function() {
                displayInfowindow(marker, title);
            });

            kakao.maps.event.addListener(marker, 'mouseout', function() {
                infowindow.close();
            });

            itemEl.onmouseover =  function () {
                displayInfowindow(marker, title);
            };

            itemEl.onmouseout =  function () {
                infowindow.close();
            };
        })(marker, places[i].place_name);

        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}


// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {
	mark_index++
    var el = document.createElement('li'),
    itemStr = '<span class="markerbg marker_' + (mark_index) + '"></span>' +
                '<div class="info">' +
                '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>'; 
    }
                 
//      itemStr += '  <span class="tel"><a href='+place_url+'></span>' 
      itemStr += '  <span class="tel">' + places.phone  + '</span>' +
                '</div>';           

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage 
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }   
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;
    //console.log(pagination)

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

//    for (i=1; i<=pagination.last; i++) {
    for (i=1; i<=7; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

    infowindow.setContent(content); //infowindow 에 표시할 내용
    infowindow.open(map, marker);
}

 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {   
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}

 $(function(){
    getcity()
     //getText2('si2') 위에 getcity안쓰고 이거만 쓸수있는 방법이있을거같음;;
})
 
 function ecclocationApi() {
	 params = "zscode=" + document.getElementById('zscode').value;
	 $("#placetable *").remove();
	 removeAllChildNods(document.getElementById('placesList'));
	 removeMarker();
	 mark_index = 0;
	 var placeslist = []
			$.ajax({
				url : "${path}/api/ecclocationApi",
				type : "POST",
				data : params,
         success : function(data) {
        	 data1=data
        	 console.log(data[0].addr)
           	data = _.uniqBy(data,'statNm')
           	data = _.uniqBy(data,'addr')
              let table = '<caption>'+$("select[name=si2]").val()+' '+$("select[name=gu2]").val()+'</caption><tr><td>충전소명</td><td>충전기타입</td><td>주소</td><td>이용가능시간</td><td>운영기관연락처</td></tr>';
              $.each(data, function(i){
            	  placeslist[i] = data[i].statNm;
            	  //console.log(placeslist)
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
              const set = new Set(placeslist);
              placeslist = [...set];
              $("#placetable").append(table)
              //console.log(placeslist)// 이게 전기차 검색시의 모든 결과 목록 뽑은건데
              searchPlaces(data)
           },
           error : function(e) {
              alert("충전소 찾다가 에러발생 : "+e.status)
           }
        })
     }
 
 function cityCode() {
     $.ajax("${path}/api/cityCodeApi",{ // Map로 데이터 수신
        success : function(data) {
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
					$('input[name=zscode]').attr('value',data[1].row[0].region_cd.substr(0,5))
				}
			})
		}
	
</script> 
</body>
</html>