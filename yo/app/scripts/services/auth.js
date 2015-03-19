define(['angular'], function(angular) {
'use strict';

angular.module('wftoolsApp.services.authService',[])
	.factory('authService',['$http','$q','apiBase',AuthService]);

function AuthService($http,$q,apiBase) {
	return {
		getContext:getContext,
		login:login,
		signUp:signUp
	};
	function getContext() {
		return $http.get(apiBase+'/auth2');
	}
	function login(loginModel) {
		return $http.post(apiBase+'/auth2/login',{
			emailAddress:loginModel.emailAddress,
			password:loginModel.password
		});
		/*return $http({
    		method: 'POST',
    		url: apiBase + '/auth2/login',
    		data: $.param({
				userName:loginModel.userName,
				password:loginModel.password,
				forwardUrl:'/welcome.html',
				format:'json'    			
    		}),
    		headers: {'Content-Type': 'application/x-www-form-urlencoded'} 
		});
		/*
		return $q(function(resolve,reject) {
			setTimeout(function() {
				var data={
					userName:loginModel.userName
				};
				resolve({
					data:data
				});
			},1000);
		});*/
	};
	function signUp(loginModel) {
		return $http.post(apiBase+'/auth2/signup',{
			name:loginModel.userName,
			emailAddress:loginModel.emailAddress,
			password:loginModel.password
		});
		/*return $http({
    		method: 'POST',
    		url: apiBase + '/auth/signup',
    		data: $.param({
				userName:loginModel.userName,
				password:loginModel.password,
				emailAddress:loginModel.emailAddress,
				forwardUrl:'',
				format:'json'  			
    		}),
    		headers: {'Content-Type': 'application/x-www-form-urlencoded'} 
		});*/
		/*return $http.post(apiBase + '/auth/signup', {
			loginName:loginModel.userName,
			password:loginModel.password,
			emailAddress:loginModel.emailAddress,
			forwardUrl:''
		});*/
/*		return $q(function(resolve,reject) {
			setTimeout(function() {
				var data={
					userName:loginModel.userName
				};
				resolve({
					data:data
				});
			},1000);
		});*/
	}
	
};//END AuthService

});
