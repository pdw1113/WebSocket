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
		<div class="container w-25 mb-5">
			<div id="loginContainer" class="input-group">
				<c:if test="${empty sessionScope.loginUser}">
					<input type="email" id="name" class="form-control" placeholder="이름" autofocus>
					<div class="input-group-append">
						<button class="btn btn-primary" type="button" onclick="login();">로그인</button>
					</div>
				</c:if>
				<c:if test="${!empty sessionScope.loginUser}">
					<div>
						<h3>
							<span class="badge badge-primary">${sessionScope.loginUser}님 환영합니다.</span>
						</h3>
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
			      		<div class="list-group rounded-0">
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
			        		<a class="list-group-item list-group-item-action list-group-item-light rounded-0">
					          	<div class="media">
					          		<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
					            	<div class="media-body ml-4">
					              		<div class="d-flex align-items-center justify-content-between mb-1">
						                	<h6 class="mb-0">팀 버너스리</h6>
						                	<small class="small font-weight-bold">25 Dec</small>
					              		</div>
					            	 	<p class="font-italic mb-0 text-small">Hey, How are you?</p>
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
				<div class="px-4 py-5 chat-box bg-white">
				    <!-- 상대방 메세지 -->
					<div class="media w-50 mb-3">
						<img src="resources/pic/sample.png" alt="user" width="50" class="rounded-circle">
				  		<div class="media-body ml-3">
						    <div class="bg-light rounded py-2 px-3 mb-2">
						    	<p class="text-small mb-0 text-muted">Test which is a new approach all solutions</p>
						    </div>
				    		<p class="small text-muted">12:00 PM | Aug 13</p>
				  		</div>
					</div>
				    <!-- 상대방 메세지 -->
					<!-- 내가 보낸 메세지 -->
					<div class="media w-50 ml-auto mb-3">
						<div class="media-body">
						    <div class="bg-primary rounded py-2 px-3 mb-2">
						    	<p class="text-small mb-0 text-white">Test which is a new approach to have all solutions</p>
						    </div>
					    	<p class="small text-muted">12:00 PM | Aug 13</p>
					  	</div>
					</div>
					<!-- 내가 보낸 메세지 -->
				</div>
				<!-- 메세지 입력 창 -->
		      	<form action="#" class="bg-light">
		        	<div class="input-group">
			          	<input type="text" placeholder="Type a message" aria-describedby="button-addon2" class="form-control rounded-0 border-0 py-4 bg-light">
			          	<div class="input-group-append">
			            	<button id="button-addon2" type="submit" class="btn btn-link"> <i class="fa fa-paper-plane"></i></button>
			          	</div>
		        	</div>
		      	</form>
				<!-- 메세지 입력 창 -->
	    	</div>
		</div>
		<!-- 채팅창 -->
	</div>
	<!-- 채팅 컨테이너 -->
	
	<div class="div">
		<span>방이름 : </span>
		<input type="text" id="roomName">
		<button onclick="send('create');">채팅방만들기</button>
	</div>
	<div class="div">
		<span>내용 : </span>
		<textarea id="chat"></textarea>
		<button onclick="send('send');">발송</button>
	</div>
	<div class="div">
		<button onclick="disconnect();">종료</button>
		<button onclick="chatClear();">CLEAR</button>
	</div>
	<div id="roomList" class="div">
	</div>
	<div id="message" class="div2">
	</div>
	
	<script>
		console.log("${sessionScope.loginUser}");
		<!-- 로그인 (세션 추가) -->
        function login(){
        	
        	let name = document.getElementById("name").value;
        	
    		// 게시글 리스트 AJAX 통신
    		$.ajax({
    		    url:"/login?name=" + name,
    		    type:"get",
    		    dataType: "html",
    		    success:function(data){
    		    	alert(data);
    		    	$("#name").attr("readonly",true);
    		    	
    		    	// 웹소켓 연결
    		    	connect();
    		    	// 로그인 확인
    		    	$("#loginContainer").html(name + "님 환영합니다.");
    		    },
    		    error:function(jqxhr, textStatus, errorThrown){
    		       console.log("ajax 처리 실패");
    		    }
    		});
        }
        
		<!-- webSocket 변수 선언 -->
		let webSocket;

		<!-- webSocket 연결 -->
		function connect(){
			
			// webSocket 연결되지 않았을 때만 연결
			if(webSocket === undefined){
				let wsUri = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/webSocket/echo";
				webSocket = new WebSocket(wsUri);
				webSocket.onopen = onOpen;
				/* webSocket.onclose = onClose; */
				webSocket.onmessage = onMessage;
			}else{
				document.getElementById("message").innerHTML+="<br/>" + "<b>이미 연결되어 있습니다!!</b>";
			}
		}

		<!-- webSocket 연결 성공 시 -->
		function onOpen(){
			document.getElementById("message").innerHTML+="<br/>" + "<b>" + $("#name").val() + "님이 웹소켓에 연결되었습니다!!</b>";
		}

		<!-- webSocket 연결 종료 -->
		function disconnect(){
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

		<!-- webSocket 메세지 발송 시 -->
		function send(handle){
			
			let data = null;
			
			if(handle === "send"){
				data = {
					"handle" : "send",
					"sender" : document.getElementById("name").value,
					"content" : document.getElementById("chat").value,
					"c" : "ccc"
				}
			}else if(handle === "create"){
				data = {
					"handle" : "create",
					"room" : document.getElementById("roomName").value
				}
			}

			let jsonData = JSON.stringify(data);
			
			webSocket.send(jsonData);
		}
		
		<!-- webSocket 메세지 수신 시 -->
		function onMessage(evt){			
        	let receive = evt.data.split(",");
     		
        	if(receive[0] === "send"){
                let data = {
                   	 "sender" : receive[1],
                   	 "content" : receive[2],
                     "c" : receive[3]
                };
                writeResponse(data.sender + " : " + data.content);
        	}else if(receive[0] === "room"){
        		let data = {
       				"room" : receive[1]
        		}
        		document.getElementById("roomList").innerHTML += "<br/><button onclick='enterRoom();'>" + data.room + "</button>";
        	}else{
        	}
		}
		
		<!-- webSocket 메세지 화면에 표시해주기 -->
        function writeResponse(text){
        	document.getElementById("message").innerHTML+="<br/>"+text;
        }
        
        function chatClear(){
        	document.getElementById("message").innerHTML=" ";
        }
        
	</script>
</body>
</html>
