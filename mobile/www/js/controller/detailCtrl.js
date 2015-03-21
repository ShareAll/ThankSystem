angular.module('thank.controllers', [])
	.controller('detailCtrl', ['$scope','$stateParams','todoService',

function ToDoCtrl($scope,$stateParams,todoService) {
	console.info("open...");
  if($stateParams.todoId) {
  		$scope.selectedTodo = todoService.detail($stateParams.todoId);	
  }
  

}



]);

