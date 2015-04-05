(function() {

angular.module('thank.services.goalService',[])
	.factory('goalService',['$http','$timeout','$q','$location','apiBase','$cordovaLocalNotification','$ionicPlatform',GoalService]);

function GoalService($http,$timeout,$q,$location,apiBase,$cordovaLocalNotification,$ionicPlatform) {
//mock data;
	var goals=[
			{'id':1,'img':'img/karma.png','title':'Travel around US in 3 monthes','completeness':10.56,'friends':21,'comments':10},
			{'id':2,'img':'img/karma.png','title':'Lose 10 pound in 20 days','completeness':55,'friends':11,'comments':9,'notes':'No updates in 5 days'},
			{'id':3,'img':'img/karma.png','title':'Learn OpenStack in 20 days','completeness':0,'friends':5,'comments':200}
	];
	var mileStones=[
		{'goalId':3,'title':'install dev stack','score':5}
	];

	
	return {
		list:list,
		getDetail:getDetail
	};
	function list() {
		return $q(function(resolve,reject) {
			resolve({
				data:goals
			});
		});
	}//end of list

	function getDetail(goalId) {
		var selectedGoal=null;
		angular.forEach(goals,function(val,ind) {
			if(val.id==goalId) {
				selectedGoal=val;
			}
		});
		return $q(function(resolve,reject) {
			resolve({
				data:selectedGoal
			});
		});

	}



} //End of GoalService




})();