(function() {
	angular.module('thank.controllers.loginCtrl', [])
		.controller('loginCtrl', ['$scope','$rootScope','$timeout','$stateParams','$ionicHistory','$state','todoService','loginService',LoginCtrl]);

function LoginCtrl($scope,$rootScope,$timeout,$stateParams,$ionicHistory,$state,todoService,loginService) {
 	//login 
	$scope.loginData = {};
	$scope.inAutoLogin=true;
	//auto login
    /*
	console.log("start auto login for 100 miliseconds");
	$timeout(function() {
		loginService.autoLogin().then(function(resp) {
			console.log("autoLogin Done "+JSON.stringify(resp));
			$rootScope.currentUser=resp.data;
			$ionicHistory.nextViewOptions({
	            disableBack: true
	        });
	        $state.go($rootScope.default_page, {}, {location:'replace'});
			$scope.inAutoLogin=false;
		},function(resp) {
			console.log("autoLogin Done "+JSON.stringify(resp));
			$scope.inAutoLogin=false;
			console.info("auto login fail");
		});

	},500);
*/

    // Perform the login action when the user submits the login form
    $scope.doLogin = function() {
        loginService.deviceSignUp($scope.loginData).then(function SUCCESS(resp){
    	   $rootScope.currentUser=resp.data;
            //  $rootScope.currentUser.friends=["pzou@ebay.com","jikarma@ebay.com",'fenwang@ebay.com'];
   		   $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go($rootScope.default_page, {}, {location:'replace'});
        },function(resp) {
    	   $scope.lastError=resp.data.errorMsg;
        });
    };// do login

    $scope.logout=function() {
        $scope.inAutoLogin=false;
        $ionicHistory.nextViewOptions({
            disableBack: true
        });
        console.info("logout");
        $state.go('login',{}, {location:'replace'});
    }

}//LoginCtrl

})();



