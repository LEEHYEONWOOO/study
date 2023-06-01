<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
    
<!-- /shop1/src/main/webapp/WEB-INF/view/item/list.jsp -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 목록</title>
</head>
<body>

<a href="create">상품 등록</a> <!-- get 방식 호출 -->
<a href="../cart/cartView">장바구니</a>

<table>
	<tr style="background-color: #BDBDBD;">
		<th width="80">상품 ID</th>
		<th width="320">상품 명</th>
		<th width="100">가격</th>
		<th width="80">수정</th>
		<th width="80">삭제</th>
	</tr>
	<c:forEach items="${itemList}" var="item">
		<tr style="background-color: #EAEAEA;">
			<td align="center">${item.id}</td>
			<td align="left"><a href="detail?id=${item.id}">${item.name}</a></td>
			<td align="right"><fmt:formatNumber value="${item.price}" pattern="###,###"/></td>
			<td align="center"><a href="update?id=${item.id}">수정</a></td>
			<td align="center"><a href="delete?id=${item.id}">삭제</a></td>
		</tr>
	</c:forEach>
</table>
</body>
</html>