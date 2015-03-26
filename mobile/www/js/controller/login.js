(function() {
	angular.module('thank.controllers.loginCtrl', [])
		.controller('loginCtrl', ['$scope','$rootScope','$timeout','$stateParams','$ionicHistory','$state','todoService','loginService',LoginCtrl]);

function LoginCtrl($scope,$rootScope,$timeout,$stateParams,$ionicHistory,$state,todoService,loginService) {
 	//login 
	$scope.loginData = {};
	$scope.inAutoLogin=true;
	//auto login
	loginService.autoLogin().then(function(resp) {
		$rootScope.currentUser=resp.data;
		$ionicHistory.nextViewOptions({
            disableBack: true
        });
        $state.go('app.todoList', {}, {location:'replace'});
		
	},function() {
		$scope.inAutoLogin=false;
		console.info("auto login fail");
	});


  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    loginService.deviceSignUp($scope.loginData).then(function(resp){
    	$rootScope.currentUser=resp.data;
   		$ionicHistory.nextViewOptions({
            disableBack: true
        });
        $state.go('app.todoList', {}, {location:'replace'});
    },function() {

    });
    
  };// do login

  $scope.logout=function() {
  	$scope.inAutoLogin=false;
  	$ionicHistory.nextViewOptions({
            disableBack: true
    });
  	$state.go('app.login',{}, {location:'replace'});
  }

}//LoginCtrl

})();



