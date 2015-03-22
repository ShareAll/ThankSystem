(function() {
	angular.module('thank.controllers.todoCtrl', [])
		.controller('todoCtrl', ['$scope','$stateParams','todoService',TodoCtrl]);

	function TodoCtrl($scope,$stateParams,todoService) {
		  $scope.todoList = [];
		  todoService.initReminder();
		  todoService.list().then(function SUCCESS(resp) {
		  	$scope.newTasks=resp.data.newTasks;
		  	$scope.completeTasks=resp.data.completeTasks;
		  });
		  $scope.getScore=function() {
		  	return todoService.getScore();
		  }
		  $scope.showDetail=function(taskId) {

		  }

	}//TodoCtrl

})();



