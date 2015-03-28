(function() {
	
	angular.module('thank.controllers.todoDetailCtrl', [])
		.controller('todoDetailCtrl', ['$scope','$timeout','$stateParams','todoService','cardService',TodoDetailCtrl]);

	function TodoDetailCtrl($scope,$timeout,$stateParams,todoService,cardService) {
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
			$scope.isSending=true;
			$scope.lastError="";
			cardService.send($scope.card).then(function() {
				todoService.complete($scope.card.id);
				$scope.isSending=false;
			},function(resp) {				
				$scope.lastError=resp.data.errorMsg;
				$scope.isSending=false;				
			});
			//console.dir($scope.card);
			
		};
	} //TodoDetailCtrl

})();