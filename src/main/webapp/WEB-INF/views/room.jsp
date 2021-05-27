<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- 로그인 했을 때 --%>
<c:if test="${!empty requestScope.sender}">
	<a class="list-group-item list-group-item-action list-group-item-light rounded-0 aop" onclick="roomEnter(this);" id="${requestScope.uuid}">
	  	<div class="media">
	  		<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
	    	<div class="media-body ml-4">
	      		<div class="d-flex align-items-center justify-content-between mb-1">
	         	<h6 class="mb-0">${requestScope.sender}</h6>
	         	<small class="small font-weight-bold">${requestScope.date}</small>
	      		</div>
	    	 	<p class="font-italic mb-0 text-small">${requestScope.uuid}</p>
	    	</div>
		</div>
	</a>
</c:if>
<%-- 새로고침 했을 때 --%>
<c:if test="${empty requestScope.sender}">
	<c:forEach var="user" items="${map}">
		<a class="list-group-item list-group-item-action list-group-item-light rounded-0 aop" onclick="roomEnter(this);" id="${fn:split(user.value,':')[1]}">
		  	<div class="media">
		  		<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
		    	<div class="media-body ml-4">
		      		<div class="d-flex align-items-center justify-content-between mb-1">
		         	<h6 class="mb-0">${fn:split(user.value,':')[0]}</h6>
		         	<small class="small font-weight-bold">${requestScope.date}</small>
		      		</div>
		    	 	<p class="font-italic mb-0 text-small">${fn:split(user.value,':')[1]}</p>
		    	</div>
			</div>
		</a>
	</c:forEach>
</c:if>