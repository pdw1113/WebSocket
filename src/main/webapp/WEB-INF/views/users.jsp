<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:forEach var="user" items="${requestScope.userList}">
	<c:if test="${user != sessionScope.loginUser}">
		<a class="list-group-item list-group-item-action list-group-item-light rounded-0 aop" onclick="roomEnter(this);">
		  	<div class="media">
		  		<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
		    	<div class="media-body ml-4">
		      		<div class="d-flex align-items-center justify-content-between mb-1">
		         	<h6 class="mb-0">${user.userName}</h6>
		         	<small class="small font-weight-bold" id="${user.userUuid}">${requestScope.date}</small>
		      		</div>
		    	 	<p class="font-italic mb-0 text-small">${user.userUuid}</p>
		    	</div>
			</div>
		</a>
	</c:if>
</c:forEach>