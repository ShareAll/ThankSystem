(function() {
	
	angular.module('thank.controllers.todoDetailCtrl', [])
		.controller('todoDetailCtrl', ['$scope','$stateParams','todoService',TodoDetailCtrl]);

	function TodoDetailCtrl($scope,$stateParams,todoService) {
		todoService.detail($stateParams.todoId).then(function(resp){
			$scope.selectedTodo =resp.data; 
		});
		var templates=["loved","nofear"];
		$scope.card={
			id:$stateParams.todoId,
			templateName:templates[0]
		}
		$scope.slideHasChanged=function(index){
			$scope.card.templateName=templates[index];
		}
		$scope.send=function() {
			//console.dir($scope.card);
			todoService.complete($scope.card.id);
		};
	} //TodoDetailCtrl

})();