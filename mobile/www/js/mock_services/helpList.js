(function() {

angular.module('thank.services.helpListService',[])
	.factory('helpListService',['$http','$timeout','$q','$location','apiBase','$ionicPlatform',HelpListService]);

function HelpListService($http,$timeout,$q,$location,apiBase,$ionicPlatform) {
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
		add:add,
		list:list,
		getDetail:getDetail
	};
	function add(goalData) {
		return $q(function(resolve,reject){
			var newGoal={
				'id':goals.length+1,
				'img':'img/karma.png',
				'title':goalData.title,
				'completeness':0.0,
				'friends':0,
				'comments':0
			};
			goals.push(newGoal);
			resolve({
				data:newGoal
			})
		});
	}
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