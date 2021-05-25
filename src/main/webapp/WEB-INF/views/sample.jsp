<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${!empty sessionScope.loginUser}">
<div class="alert alert-primary container">
	${sessionScope.loginUser}님 환영합니다.
</div>
<div class="container">
	<button class="btn btn-danger" onclick="logout();">로그아웃</button>
</div>
</c:if>
<c:if test="${empty sessionScope.loginUser}">
	<input type="email" id="name" class="form-control" placeholder="이름" autofocus>
	<div class="input-group-append">
		<button class="btn btn-primary" type="button" onclick="login();">로그인</button>
	</div>
</c:if>