(function() {
	
	angular.module('thank.controllers.goalDetailCtrl', [])
		.controller('goalDetailCtrl', ['$scope','$stateParams','$interval','goalService','goalUpdateService',GoalDetailCtrl]);

	function GoalDetailCtrl($scope,$stateParams,$interval,goalService,goalUpdateService) {
		var curId=0;
		var contentElm=angular.element(document.getElementById('updates'));
		$interval(function(){
			goalUpdateService.listen(curId).then(function SUCCESS(resp) {
				if(resp.data) {
					contentElm.append("<p>"+resp.data.name+":"+resp.data.content+"</p>");	
					console.info("consume msg "+curId+": "+resp.data.content);
					curId++;

				} else {
					console.info("no data for "+curId);
				}
			});	
		},2000);
		
		$scope.send=function() {
			if($scope.msg && $scope.msg.length>0) {
				goalUpdateService.send("me",$scope.msg);
			}
		}
		
	} //GoalDetailCtrl

})();