<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>  
<%-- /shop1/src/main/webapp/WEB-INF/view/admin/list.jsp --%>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 목록</title>
<script type="text/javascript">
	function allchkbox(allchk) {
		$(".idchks").prop("checked",allchk.checked)
	}
</script>
<style>
	.noline {
		text-decoration: none;
	}
</style>
</head>
<body>

<h2>회원 목록</h2>
<form action="mailForm" method="post">
	<table class="w3-table-all">
		<tr>
			<th>
				아이디
				<a href="list?sort=10" class="noline">▲</a>
				<a href="list?sort=11" class="noline">▼</a>
			</th>
			<th>
				이름
				<a href="list?sort=20" class="noline">▲</a>
				<a href="list?sort=21" class="noline">▼</a>
			</th>
			<th>
				전화
				<a href="list?sort=30" class="noline">▲</a>
				<a href="list?sort=31" class="noline">▼</a>
			</th>
			<th>
				생일
				<a href="list?sort=40" class="noline">▲</a>
				<a href="list?sort=41" class="noline">▼</a>
			</th>
			<th>
				이메일
				<a href="list?sort=40" class="noline">▲</a>
				<a href="list?sort=41" class="noline">▼</a>
			</th>
			<th>관리</th>
			<th><input type="checkbox" name="allchk" onchange="allchkbox(this)"></th>
		</tr>
		<c:forEach items="${list}" var="user">
		<tr>
			<td>${user.userid}</td>
			<td>${user.username}</td>
			<td>${user.phoneno}</td>
			<td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/></td>
			<td>${user.email}</td>
			<td>
				<a href="../user/update?userid=${user.userid}">수정</a>
				<a href="../user/delete?userid=${user.userid}">강제탈퇴</a>
				<a href="../user/mypage?userid=${user.userid}">회원정보</a>
			</td>
			<td><input type="checkbox" name="idchks" class="idchks" value="${user.userid}"></td>
		</tr>
		</c:forEach>
		<tr>
			<td colspan="7" style="text-align:center;"><input type="submit" value="메일전송"></td>
		</tr>
	</table>
</form>

</body>
</html>