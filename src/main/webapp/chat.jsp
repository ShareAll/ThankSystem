<html ng-app="thankApp">
<%
	String roomId=request.getParameter("room");
	if(roomId==null) roomId="global";
	
%>
<head>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
	<script src="https://cdn.rawgit.com/gdi2290/angular-websocket/v1.0.9/angular-websocket.min.js"></script>
</head>
<body ng-controller="chatController">
	<p>{{socket_status}} {{socket_reconnect_timer}}</p>
	 <form ng-submit="sendMessage()">
	 	<input type="hidden" value="<%=roomId%>" id="roomId"/>
        <input type="text" placeholder="type and press enter to chat" ng-model="msg"/>
     </form>
    <div id="console-container">
        <wf-chat-console wf-Data="newChats"></wf-chat-console>
    </div>
    <script>
    	angular.module('thankApp', ['ngWebSocket'])
    		.directive("wfChatConsole",['$compile',WfChatConsole])
    		.controller('chatController', ['$scope','$websocket','$interval',chatController]);
    	
    	function WfChatConsole($compile) {
    		return {
    			restrict: "EAC",
    			transclude:false,
    		    scope:{
					wfData:'='
    		    },
    		    link: function($scope,elm,attrs) {
    		    	$scope.$watch('wfData',function(newVal) {
    		    		angular.forEach(newVal.data,function(val,ind) {
    		    			var vv=JSON.parse(newVal.data);
    		    			elm.append($compile("<a>"+vv.userName+"</a><p>"+vv.textContent+"</p>")($scope));	
    		    		});
    		    	},true);
    		    }
    		}
    	}
    	
    	function chatController($scope,$websocket,$interval) {

    	    $scope.socket_reconnect_timer=null;
    	    $scope.socket_status="Connecting";
    	    $scope.newChats={};
    	    var roomId=angular.element(document.getElementById("roomId")).val();
    		var ws=$websocket('ws://localhost:8080/ThankWeb/ws/chat/'+roomId);
    		ws.onOpen(function(message) {
    			console.info("On Open");
    			$scope.socket_status="Connected";
    			$scope.socket_reconnect_timer=null;
    		});
    	    ws.onClose(function(message) {
    	    	console.info("On Close");
    	    	$scope.socket_status="Disconnected";
    	    });
    		$interval(function() {
    			if($scope.socket_reconnect_timer!=null) {
    				if($scope.socket_reconnect_timer<=0) {
    					ws.reconnect();
    					$scope.socket_reconnect_timer=10;
    				}
    				$scope.socket_reconnect_timer--;
    			} else if($scope.socket_status=="Disconnected") {
    				$scope.socket_reconnect_timer=10;
    				$scope.socket_status="Connecting";
    			} 			
    		},1000);
    		ws.onMessage(function(message) {
    			$scope.newChats.data=[message.data];
    			console.info(message);
    		});
    		
    		$scope.sendMessage=function() {
    			if($scope.msg) {
    				var json=JSON.stringify({
    					textContent:$scope.msg
    				});
    				ws.send(json);
    			}
    		}
    		
 	
    	}
    
    
    
    </script>
    
</body>
<!-- 


    <title>Chat</title>
    <style type="text/css"><![CDATA[
        input#chat {
            width: 410px
        }

        #console-container {
            width: 400px;
        }

        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 170px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }
    ]]></style>
    <script type="application/javascript">
        "use strict";

        var Chat = {};

        Chat.socket = null;

        Chat.connect = (function(host) {
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            Chat.socket.onopen = function () {
                Console.log('Info: WebSocket connection opened.');
                document.getElementById('chat').onkeydown = function(event) {
                    if (event.keyCode == 13) {
                        Chat.sendMessage();
                    }
                };
            };

            Chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                Console.log('Info: WebSocket closed.');
            };

            Chat.socket.onmessage = function (message) {
                Console.log(message.data);
            };
        });

        Chat.initialize = function() {
            if (window.location.protocol == 'http:') {
                Chat.connect('ws://' + window.location.host + '/ThankWeb/ws/chat');
            } else {
                Chat.connect('wss://' + window.location.host + '/ThankWeb/ws/chat');
            }
        };

        Chat.sendMessage = (function() {
            var message = document.getElementById('chat').value;
            if (message != '') {
                Chat.socket.send(message);
                document.getElementById('chat').value = '';
            }
        });

        var Console = {};

        Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 25) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });

        Chat.initialize();


        document.addEventListener("DOMContentLoaded", function() {
            // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
            var noscripts = document.getElementsByClassName("noscript");
            for (var i = 0; i < noscripts.length; i++) {
                noscripts[i].parentNode.removeChild(noscripts[i]);
            }
        }, false);

    </script>
</head>
<body>
<div class="noscript">
<h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets rely on Javascript being enabled. Please enable
    Javascript and reload this page!</h2></div>
<div>
    <p>
        <input type="text" placeholder="type and press enter to chat" id="chat" />
    </p>
    <div id="console-container">
        <div id="console"/>
    </div>
</div>
</body>
</html>

 -->