<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%-- /shop1/src/main/webapp/WEB-INF/view/exception.jsp --%>    

<script>
	alert("${exception.message}") // CartEmptyException.getMessate() 메서드 호출
	location.href="${exception.url}" // CartEmptyException.getUrl() 메서드 호출
</script>
