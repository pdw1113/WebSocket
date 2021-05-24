<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
	<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
	<title>Home</title>
	<style>
		.div{
		 width: 300px; 
		 height:200px; 
		 border:1px solid black;
		 float:left;
		}
		
		.div2{
		 float:right;
		}
	</style>
</head>
<body>
	<h1>Socket Test</h1>
	<div class="div">
		<span>이름 : </span>
		<input type="text" id="name"/>
		<button onclick="login();">로그인</button>
		<button onclick="connect();">연결</button>
	</div>
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
	<div class="div" id="roomList">
		<div></div>
	</div>
	<div id="message" class="div2">
	</div>
	<script>
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
			console.log(webSocket);
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
        	
        	console.log(receive[0]);
     		
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
        		console.log(data);
        		document.getElementById("roomList").innerHTML += "<br/><div>" + data.room + "</div>"
        	}else{
        		console.log(evt.data);
        	}
		}
		
		<!-- webSocket 메세지 화면에 표시해주기 -->
        function writeResponse(text){
        	document.getElementById("message").innerHTML+="<br/>"+text;
        }
        
        function chatClear(){
        	document.getElementById("message").innerHTML=" ";
        }
        
        function login(){
    		// 게시글 리스트 AJAX 통신
    		$.ajax({
    		    url:"/login?name=" + document.getElementById("name").value,
    		    type:"get",
    		    success:function(data){
    		    	alert("로그인 성공");
    		    	$("#name").attr("readonly",true);
    		    },
    		    error:function(jqxhr, textStatus, errorThrown){
    		       console.log("ajax 처리 실패");
    		    }
    		});
        }
	</script>
</body>
</html>
