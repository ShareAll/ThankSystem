(function() {
	angular.module('thank.controllers.goalCtrl', [])
		.controller('goalCtrl', ['$rootScope','$scope','$timeout','$stateParams','goalService',GoalCtrl]);

	function GoalCtrl($rootScope,$scope,$timeout,$stateParams,goalService) {
		  $scope.goals = [];
		  $rootScope.curGoal=null;
		  console.info("Load GOAL Control");
		  
		  goalService.list().then(function SUCCESS(resp) {
		  	$scope.goals=resp.data;
		  	$timeout(function() {
		  		$('.circliful').circliful();
		  	},100);
		  	//console.dir($scope.goals); 	
		  });

	}//GoalCtrl

})();



