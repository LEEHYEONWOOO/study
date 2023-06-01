<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>    
<!-- /shop1/src/main/webapp/WEB-INF/view/item/create.jsp
		서버에서 파라미터 검증, 파일 업로드
 -->    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 등록</title>
</head>
<body>

<%-- <form:form ... /> => enctype="multipart/form-data" 일 경우 method는 자동으로 post로 설정됨 --%>
<form:form modelAttribute="item" action="create" enctype="multipart/form-data">
	<h2>상품 등록</h2>
	<table>
		<tr>
			<td>상품 명</td>
			<td><form:input path="name"/></td> <!-- path : 파라미터 값 -->
			<td><font color="red"><form:errors path="name" /></font></td>
			<%-- <form:input path="name" /> =>> --%>
			<%-- <input type="text " name="name" id="name" value="${item.name}"> --%>
		</tr>
		<tr>
			<td>상품 가격</td>
			<td><form:input path="price"/></td>
			<td><font color="red"><form:errors path="price" /></font></td>
		</tr>
		<tr>
			<td>상품 이미지</td>
			<td colspan="2"><input type="file" name="picture"></td>
		</tr>
		<tr>
			<td>상품 설명</td>
			<td><form:textarea path="description" cols="20" rows="5" /></td>
			<td><font color="red"><form:errors path="description" /></font></td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="submit" value="상품등록">
				<input type="button" value="상품목록" onclick="location.href='list'">
			</td>
		</tr>
	</table>
</form:form>



</body>
</html>