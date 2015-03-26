(function() {


angular.module('thank.services.facebookService',[])
	.factory('facebookService',['$http','$timeout','$q','$location','apiBase','$ionicPlatform',
		'$ionicHistory','$state',FacebookService]);

function FacebookService($http,$timeout,$q,$location,apiBase,$ionicPlatform,$ionicHistory,$state) {
	var inited=false;
	var FACEBOOK_APP="441815479314051";
	var auth=null;
	return {
		init:init,
		login:login,
		getPhotos:getPhotos

	};
	function init() {
		if(inited) return;	
		$ionicPlatform.ready(function() {
			inited=true;
			if(window.facebookConnectPlugin) {
        		facebookConnectPlugin.browserInit(FACEBOOK_APP_ID);
        	}
		});		
	} //end of init

	function login(permissions) {
		if(!window.facebookConnectPlugin) {
			return $q(function(resolve,reject) {
				reject({
					data:"device not ready"
				});	
			});
		} else {

			return $q(function(resolve,reject) {
		 		facebookConnectPlugin.login(permissions,function SUCCESS(resp) {
		 			auth=resp.authResponse;
		 			resolve(resp);
		 		}, function FAIL(resp) {
		 			reject(resp);
		 		});
			});
		}
	
	} //end of login

	function getPhotos() {
		if(!window.facebookConnectPlugin || auth==null) {
			return $q(function(resolve,reject) {
				reject({
					data:"device not ready"
				});
			});
		} else {			
			return $q(function(resolve,reject) {
				facebookConnectPlugin.api(
        			"me?fields=photos",
        			['user_photos'],
        			function SUCESS(resp) {
        				var photoLinks=[];
        				
        				if(resp.photos) {
        					angular.forEach(resp.photos.data,function (val,ind) {
        						if(val.images) {
        							angular.forEach(val.images,function(imgSrc,imgInd){
        								photoLinks.push(imgSrc);
        							});
        						}
        						
        					});
        				}
        				//resolve(resp.photos.data);
        				resolve(photoLinks);
        			},function FAIL(resp) {
        				reject(resp);
        			});
			});
		}
	} //getPhotos


} //End of FacebookService




})();