angular.module('thank.controllers', [])
	.controller('todoCtrl', ['$scope','$stateParams','todoService',

function ToDoCtrl($scope,$stateParams,todoService) {

  $scope.todoList = [];
  todoService.list().then(function SUCCESS(resp) {
  	$scope.todoList=resp.data;
  });

  if($stateParams.id) {
  		$scope.selectedTodo = todoService.detail($stateParams.id);	
  }
  

}



])


.controller('detailCtrl', ['$scope','$stateParams','todoService',

function ToDoCtrl($scope,$stateParams,todoService) {
	
	todoService.detail($stateParams.todoId).then(function(resp){
		$scope.selectedTodo =resp.data; 
	});
	var templates=["loved","nofear"];
	$scope.card={
		templateName:templates[0]
	}
	$scope.slideHasChanged=function(index){
		$scope.card.templateName=templates[index];
	}
	$scope.send=function() {
		console.dir($scope.card);
	};
	

  

}



]);



