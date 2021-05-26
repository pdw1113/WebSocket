<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" id="bootstrap-css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/chat.css">
	<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<div class="container py-5 px-4">
	
		<!-- 제작자 및 디자인 정보 -->
		<header class="text-center">
			<h1 class="display-4 text-white">Spring 1:1 Chatting</h1>
			<p class="text-white lead mb-0">
				<a href="https://retrieverj.tistory.com/" class="text-white">Developed by retrieverj</a>
			</p>
			<p class="text-white lead mb-4">Snippet by
				<a href="https://bootstrapious.com" class="text-white">Bootstrapious</a>
			</p>
		</header>
		<!-- 제작자 및 디자인 정보 -->
		
		<!-- 로그인 -->
		<div class="container w-25 mb-3">
			<div id="loginContainer" class="input-group">
				<%-- 로그인 X --%>
				<c:if test="${empty sessionScope.loginUser}">
					<input type="email" id="name" class="form-control" placeholder="이름" autofocus>
					<div class="input-group-append">
						<button class="btn btn-primary" type="button" onclick="login();">로그인</button>
					</div>
				</c:if>
				<%-- 로그인 O --%>
				<c:if test="${!empty sessionScope.loginUser}"> 
					<div class="alert alert-primary container">
						<span id="loginName">${sessionScope.loginUser.name}</span>님 환영합니다.
					</div>
					<div class="container">
					<button class="btn btn-danger" onclick="logout();">로그아웃</button>
					</div>
				</c:if>
			</div>
		</div>
		<!-- 로그인 -->
		
		<!-- 채팅 컨테이너 -->
		<div class="row rounded-lg overflow-hidden shadow">
			<!-- 채팅방 목록 -->
			<div class="col-5 px-0">
				<div class="bg-white">
				
				    <div class="bg-gray px-4 py-2 bg-light">
				      	<p class="h5 mb-0 py-1">Chat Room List</p>
				    </div>

					<div class="messages-box">
			      		<div class="list-group rounded-0" id="roomList">
			        		<a class="list-group-item list-group-item-action active text-white rounded-0">
					          	<div class="media">
					          		<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
					            	<div class="media-body ml-4">
					              		<div class="d-flex align-items-center justify-content-between mb-1">
						                	<h6 class="mb-0">김범수</h6>
						                	<small class="small font-weight-bold">25 Dec</small>
					              		</div>
					            	 	<p class="font-italic mb-0 text-small">안녕하세요.</p>
					            	</div>
				        		</div>
			        		</a>
		      			</div>
			    	</div>
		  		</div>
			</div>
			<!-- 채팅방 목록 -->
			
			<!-- 채팅창 -->
			<div class="col-7 px-0">
			
				<div class="px-4 pt-5 chat-box bg-white" id="message">
				<%-- 메세지 동적 생성 --%>
				</div>
				
				<!-- 메세지 입력 창 -->
	        	<div class="input-group">
		          	<input type="text" id="chat" placeholder="메세지를 입력하세요." class="form-control rounded-0 border-0 py-4 bg-light">
		          	<div class="input-group-append">
		            	<button type="button" class="btn btn-link bg-white" onclick="send('message');">
		            		<i class="fa fa-paper-plane"></i>
		            	</button>
		          	</div>
	        	</div>
				<!-- 메세지 입력 창 -->
	    	</div>
		</div>
		<!-- 채팅창 -->
	</div>
	<!-- 채팅 컨테이너 -->
	
	<script>
		// HTML 전송 공통 AJAX
		function ajaxForHTML(url, data, contentType){
			
			let htmlData;
			
    		// HTML AJAX 통신
    		$.ajax({
    		    url : url,
    		    data: data,
    		    contentType: contentType,
    		    type:"POST",
    		 	// html(jsp)로 받기
    		    dataType: "html",
    		    async: false,
    		    // 성공 시
    		    success:function(data){
    		    	htmlData = data;
    		    },
    		    error:function(jqxhr, textStatus, errorThrown){
    		       alert("ajax 처리 실패");
    		    }
    		});
    		
    		return htmlData;
		}
	
		<!-- 로그인 (세션 추가 및 WebSocket 연결) -->
        function login(){
        	// 이름
        	let name = document.getElementById("name").value;
        	// AJAX 통신
        	let data = ajaxForHTML("/login", {"name" : name});
        	// 로그인 성공 시
        	if(data !== ""){
            	// DOM 변경
            	$("#loginContainer").html(data);
            	// WebSocket 연결
            	connect();
            	return;
        	}
        	alert("로그인 실패!");
        }
        
        <!-- 로그아웃 (세션 제거) -->
        function logout(){
        	// AJAX 통신
        	let data = ajaxForHTML("/logout");
        	// DOM 변경
        	$("#loginContainer").html(data);
        	// WebSocket 연결 해제
			if(webSocket !== undefined){
				// 연결 종료
				webSocket.close();
				// 객체 초기화
				webSocket = undefined;
				document.getElementById("message").innerHTML+="<br/>" + "<b>웹소켓 연결이 해제되었습니다.</b>";
			}else{
				document.getElementById("message").innerHTML+="<br/>" + "<b>연결된 웹소켓이 존재하지 않습니다.</b>";
			}
        }
        
		<!-- webSocket 변수 선언 -->
		let webSocket;
		
		/* 
			WebSocket Session은 새로고침 시, 연결이 해제 된다.
			하지만, HttpSession은 해제되지 않는다.
			그러므로, HttpSession이 연결되어 있을 때(로그인 중)에는 
			WebSocketSession을 연결시켜 주도록 하자.
		*/
		if(webSocket === undefined && "${sessionScope.loginUser}" !== ""){
			connect();
		}
		
		<!-- webSocket 연결 -->
		function connect(){
			
			// webSocket 연결되지 않았을 때만 연결
			if(webSocket === undefined){
				let wsUri = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/webSocket/echo";
				webSocket = new WebSocket(wsUri);
				webSocket.onopen = onOpen;
				webSocket.onmessage = onMessage;
				/* webSocket.onclose = onClose; */
			}else{
				document.getElementById("message").innerHTML+="<br/>" + "<b>이미 연결되어 있습니다!!</b>";
			}
		}

		<!-- webSocket 연결 성공 시 -->
		function onOpen(){
			// TODO
		}
		
		<!-- webSocket 메세지 발송 -->
		function send(handle){
			
			let data = null;
			let chatMessage = document.getElementById("chat");
			
			if(chatMessage.value){
				if(handle === "message"){
					data = {
						"handle" : "message",
						"sender" : "${sessionSocpe.loginUser.name}" || document.getElementById("loginName").innerHTML,
						"content" : chatMessage.value
					}
				}
				let jsonData = JSON.stringify(data);
				
				webSocket.send(jsonData);
				
				chatMessage.value = "";
			}
		}
		
		// 엔터로 채팅 전송
		$(document).on('keydown', '#chat', function(e){
            if(e.keyCode == 13 && !e.shiftKey) {
            	send('message');
            }
       	});
		
		<!-- webSocket 메세지 수신 시 -->
		function onMessage(evt){			
			
        	let receive = evt.data.split(",");
        	let data;
     		
        	if(receive[0] === "message"){
                data = {
                	 "handle" : receive[0],
                   	 "sender" : receive[1],
                   	 "content" : receive[2]
                };
        	}else if(receive[0] === "login"){
                data = {
                   	 "handle" : receive[0],
                     "sender" : receive[1]
                };
        	}
        	
            writeResponse(data);
		}
		
		<!-- webSocket 메세지 화면에 표시해주기 -->
        function writeResponse(data){
        	
        	if(data.handle === "message"){
            	// JSON.stringify() : JavaScript 객체 → JSON 객체 변환
            	let messageData = ajaxForHTML("/message", 
    					        			  JSON.stringify(data), 
    					        			  "application/json");
            	// 화면에 추가
            	document.getElementById("message").innerHTML += messageData;
            	
            	console.log($('#message').scrollTop($('#message').prop('scrollHeight')));
        	}else if(data.handle === "login"){            	
        		// JSON.stringify() : JavaScript 객체 → JSON 객체 변환
            	let messageData = ajaxForHTML("/room", 
						        			  JSON.stringify(data), 
						        			  "application/json");
            	// 화면에 추가
            	document.getElementById("roomList").innerHTML += messageData;
        		
        	}
        }
        
        function chatClear(){
        	document.getElementById("message").innerHTML=" ";
        }
        
	</script>
</body>
</html>
