<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- 내가 보낸 메세지 --%>
<c:if test="${empty requestScope.sender}">
	<div class="media w-50 ml-auto mb-3">
		<div class="media-body">
		    <div class="bg-primary rounded py-2 px-3 mb-2">
		    	<p class="text-small mb-0 text-white">${requestScope.content}</p>
		    </div>
	    	<p class="small text-muted">${requestScope.date}</p>
	  	</div>
	</div>
</c:if>
<%-- 상대방이 보낸 메세지 --%>
<c:if test="${!empty requestScope.sender}">
	<div class="media w-50 mb-3">
		<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
  		<div class="media-body ml-3">
		    <div class="bg-light rounded py-2 px-3 mb-2">
		    	<p class="text-small mb-0 text-muted">${requestScope.content}</p>
		    </div>
    		<p class="small text-muted">${requestScope.date}</p>
  		</div>
	</div>
</c:if>