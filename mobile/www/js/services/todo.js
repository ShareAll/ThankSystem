angular.module('thank.services',[])
	.factory('todoService',['$http','$timeout','$q','$location','apiBase',

function TodoService($http,$timeout,$q,$location,apiBase) {
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
	return {
		getScore:getScore,
		complete:complete,
		list:list,
		detail:detail
	};
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
				totalScore+=val.score
				console.info("totalScore="+totalScore);
			}
		});
		return 0;

	}
	function detail(id) {
		return $q(function(resolve,reject) {
			$timeout(function() {
				var data={'title':'Send thank '+id,'id':id};
				resolve({
					data:data
				})
				resolve({
					data:data
				});
			},1000);
		});

	}; //function detail





} //End of TodoService


]);
