(function() {
	angular.module('thank.controllers.helpListCtrl', [])
		.controller('helpListCtrl', ['$rootScope','$scope','$timeout','$stateParams','helpListService','$ionicModal',HelpListCtrl]);

	function HelpListCtrl($rootScope,$scope,$timeout,$stateParams,helpListService,$ionicModal) {
		  $scope.helpList = [];
		  $rootScope.curGoal=null;
		  console.info("Load GOAL Control");
		  $scope.goalData={
		  	'goalText':''
		  };
		  $scope.setCurGoal=function(title){
		  	if(!title) title="";
		  	if(title.length>20) title=title.substring(0,20)+"...";
		  	$rootScope.curGoal=title;
		  	console.info("set cur goal "+title);
		  };

		 

		  helpListService.list().then(function SUCCESS(resp) {
		  	$scope.helpList=resp.data;
		  	$timeout(function() {
		  		$('.circliful').circliful();
		  	},100);
		  	//console.dir($scope.goals); 	
		  });


		  /**Open New Goal Window**/
		  $ionicModal.fromTemplateUrl('templates/newGoal.html', {
    		scope: $scope,
    		animation: 'slide-in-up'
  		  }).then(function(modal) {
    			$scope.goalModal = modal;
  		  });
  		  $scope.openGoalModal = function() {
  		  	console.info("open modal");
    		$scope.goalModal.show();
  		  };
  		  $scope.submitGoal=function() {
  		  	console.dir($scope.goalData)
  		  	helpListService.add($scope.goalData).then(function() {
  		  		$scope.closeModal();
  		  		$timeout(function() {
		  			$('div.circliful:not(:has(span.circle-text))').circliful();
		  		},100);
  		  	})
  		  }
  		  $scope.closeModal = function() {
    		$scope.goalModal.hide();
  		  };
  			
  		  $scope.$on('$destroy', function() {
    			$scope.goalModal.remove();
  		  });
  
	}//GoalCtrl

})();



