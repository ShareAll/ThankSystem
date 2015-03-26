(function() {


angular.module('thank.services.loginService',[])
	.factory('loginService',['$http','$timeout','$q','$location','apiBase','$ionicPlatform',
		'$ionicHistory','$state',LoginService]);

function LoginService($http,$timeout,$q,$location,apiBase,$ionicPlatform,$ionicHistory,$state) {
	return {
		autoLogin:autoLogin,
		deviceSignUp:deviceSignUp
	};

	function autoLogin() {
		return $q(function(resolve,reject) {
			$ionicPlatform.ready(function() {
				if(window.device) {
					resolve({
						data:{
							'name':'fenwang'
						}
					});
				} else {
					reject({
						data:{"status":"fail"}
					});
				}
			});
		});					
	}

	function deviceSignUp(loginData) {
		return $q(function(resolve,reject){
			$ionicPlatform.ready(function() {
				if(window.device) {
					resolve({
						data:{
							'name':'fenwang'
						}

					})
				} else {
					resolve({
						data:{
							'name':'fenwang'
						}

					});
				}
			});			
		});
	}
	


} //End of DeviceCheckService




})();