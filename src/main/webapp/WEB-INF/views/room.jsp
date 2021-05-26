<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<a class="list-group-item list-group-item-action list-group-item-light rounded-0">
  	<div class="media">
  		<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
    	<div class="media-body ml-4">
      		<div class="d-flex align-items-center justify-content-between mb-1">
         	<h6 class="mb-0">${requestScope.sender}</h6>
         	<small class="small font-weight-bold">${requestScope.date}</small>
      		</div>
    	 	<p class="font-italic mb-0 text-small">-</p>
    	</div>
	</div>
</a>