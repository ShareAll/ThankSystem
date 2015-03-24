(function() {


angular.module('thank.services.deviceCheckService',[])
	.factory('deviceCheckService',['$http','$timeout','$q','$location','apiBase','$ionicPlatform',
		'$ionicHistory','$state',DeviceCheckService]);

function DeviceCheckService($http,$timeout,$q,$location,apiBase,$ionicPlatform,$ionicHistory,$state) {

	return {
		listenClick:listenClick,
		check:check,
		notify:notify,
		updateNotification:updateNotification
	};
	function listenClick($scope) {
		$scope.$on('cordovaLocalNotification:click', function(notification) {
  			console.log("listenClick:click on "+notification.id);
  			$ionicHistory.nextViewOptions({
		  		disableBack: true
			});
			$state.go('app.todoList', {}, {location:'replace'});
		});
					
	}
	function check() {
		return $q(function(resolve,reject) {
			$ionicPlatform.ready(function() {
				var ret=[];
				if(window.cordova) {
					ret.push({"name":"cordova","state":"Ready"});
				} else {
					ret.push({"name":"cordova","state":"Not Ready"});
				}
				if(window.cordova && window.cordova.plugins.notification) {
					ret.push({"name":"notification","state":"Ready"});
				} else {
					ret.push({"name":"notification","state":"Not Ready"});
				}
				if(window.navigator && window.navigator.camera) {
					ret.push({"name":"camera","state":"Ready"});
				} else {
					ret.push({"name":"camera","state":"Not Ready"});
				}
        		resolve({
        			data:ret
        		});
   			
			});
		});
	}
	function updateNotification(content) {
		return $q(function(resolve,reject) {
			$ionicPlatform.ready(function() {

        		if(window.cordova && window.cordova.plugins && cordova.plugins.notification) {
        		
        			cordova.plugins.notification.local.update(content);

      				resolve({
						data:"Notification Update"
					});
   				} else {
   					reject({
						data:"Device is NOT ready"
					});
   				}
  

        	});		//ionicPlatform Ready
		});//$q
	}
	function notify(content) {
		return $q(function(resolve,reject) {
			$ionicPlatform.ready(function() {

        		if(window.cordova && window.cordova.plugins && cordova.plugins.notification) {
        			window.plugin.notification.local.promptForPermission();

      				cordova.plugins.notification.local.schedule(content);
      				resolve({
						data:"Notification Sent"
					});
   				} else {
   					reject({
						data:"Device is NOT ready"
					});
   				}
  

        	});//ionicPlatform Ready
		}); //$q
	}




} //End of DeviceCheckService




})();