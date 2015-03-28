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
					$http.post(apiBase+"/auth2/autoLogin",{
						"deviceId":window.device.uuid
					}).then(function(resp) {					
						resolve(resp);
					},function(resp) {
						reject(resp);
					});

				} else {
					reject({
						data:{"status":"Device NOT ready"}
					});
				}
			});
		});					
	}

	function deviceSignUp(loginData) {
		return $q(function(resolve,reject){
			$ionicPlatform.ready(function() {
				if(window.device) {
					$http.post(apiBase+"/auth2/deviceLogin",{
						"deviceId":window.device.uuid,
						"emailAddress":loginData.emailAddress,
						"password":loginData.password,
						"name":loginData.emailAddress
					}).then(function(resp) {
						resolve(resp);
					},function(resp) {
						reject(resp);
					});
				} else {
					$http.post(apiBase+"/auth2/login",{
						"emailAddress":loginData.emailAddress,
						"password":loginData.password
						
					}).then(function(resp) {
						resolve(resp);
					},function(resp) {
						reject(resp);
					});
				}
			});			
		});
	}
	


} //End of DeviceCheckService




})();