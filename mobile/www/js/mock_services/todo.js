(function() {


angular.module('thank.services.todoService',[])
	.factory('todoService',['$http','$timeout','$q','$location','apiBase','$ionicPlatform','deviceCheckService',TodoService]);

function TodoService($http,$timeout,$q,$location,apiBase,$ionicPlatform,deviceCheckService) {
//mock data;
	var newTasks=[
			{'id':1,'img':'img/karma.png','title':'Send ThankYou Card 1','author':'KARMA','notes':'Send thankyou and receive karma','score':5},
			{'id':2,'img':'img/karma.png','title':'Send ThankYou Card 2','author':'KARMA','notes':'Send thankyou and receive karma','score':5},
			{'id':3,'img':'img/karma.png','title':'Send ThankYou Card 3','author':'KARMA','notes':'Send thankyou and receive karma','score':5},
			{'id':4,'img':'img/karma.png','title':'Send ThankYou Card 4','author':'KARMA','notes':'Send thankyou and receive karma','score':5}

	];
	var completeTasks=[
			{'id':5,'img':'img/karma.png','title':'Send ThankYou Card 5','author':'KARMA','notes':'Send thankyou and receive karma','score':5}
	];
	var totalScore=1000;
	var isReminderInited=false;
	return {
		initReminder:initReminder,
		getScore:getScore,
		complete:complete,
		list:list,
		detail:detail
	};
	
	function initReminder() {
		if(isReminderInited) return;
		
        var alarmTime = new Date();
        alarmTime.setMinutes(alarmTime.getMinutes() + 1);
        list().then(function(resp) {
        	
        	$ionicPlatform.ready(function() {
        		if (!window.cordova || !window.cordova.plugins.notification) {
      				return;
   				}
   				isReminderInited=true;
   				var now = new Date().getTime(),
                _5_sec_from_now = new Date(now + 5 * 1000);
	        	angular.forEach(resp.data.newTasks,function(val,ind){
	        		cordova.plugins.notification.local.schedule({
					    id: val.id,
					    title: val.title,
					    text: val.notes,
					    at: _5_sec_from_now,
					    sound: null
					    
					});


	        	});//angulalr forEach

        	});//ionicPlatform Ready
        }); //list
    
	}
	function getScore() {
		return totalScore;
	}
	function list() {
		return $q(function(resolve,reject) {
			$timeout(function() {
				//var completeTasks=[];
				var data={
					newTasks:newTasks,
					completeTasks:completeTasks
				};
				resolve({
					data:data
				});
			},100);
		});
	}; //function list

	function complete(id) {
		angular.forEach(newTasks,function(val,ind) {
			if(val.id==id){
				newTasks.splice(ind, 1);
				completeTasks.push(val);
				$location.path('app/todoList');
				totalScore+=val.score;
				console.info("totalScore="+totalScore);
			}
		});
		//update badge
		console.log("Prepare to send complete notification");
		list().then(function(resolve,reject) {
			var numOfItems=newTasks.length;
			var now = new Date().getTime();
		  	var oneSecondsLater = new Date(now + 1 * 1000);
			deviceCheckService.notify({
				id: 10,
				title: "You have "+numOfItems+" tasks",
				text: "Please go complete them to receive karma score",
				badge:numOfItems
			});
			console.log("todo.complete:Send "+numOfItems+" to device");
			

		});
		return 0;

	}
	function detail(id) {
		return $q(function(resolve,reject) {
			$timeout(function() {
				
				angular.forEach(newTasks,function(val,index) {
					if(val.id==id) {
						resolve({
							data:val
						})
					}
				});
				return resolve({data:{}});
			},1000);
		});

	}; //function detail





} //End of TodoService




})();