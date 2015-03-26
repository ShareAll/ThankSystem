(function() {
	angular.module('thank.controllers.deviceCheckCtrl', [])
		.controller('deviceCheckCtrl', ['$scope','$timeout','$stateParams','$location','deviceCheckService','todoService','facebookService','$state','$ionicHistory',DeviceCheckCtrl]);

	function DeviceCheckCtrl($scope,$timeout,$stateParams,$location,deviceCheckService,todoService,facebookService,$state,$ionicHistory) {
		  
		  deviceCheckService.check().then(function SUCCESS(resp) {
		  	$scope.errMessage=JSON.stringify(resp.data);
		  	$scope.checkResult=resp.data;
		  },function FAIL(resp) {
		  	$scope.checkResult={"name":"device","state":JSON.stringify(resp.data)};
		  });

		  $scope.takePicture=function() {
		  	if(navigator && navigator.camera) {
		  		navigator.camera.getPicture(onSuccess,onFail,{quality: 10,destinationType:Camera.DestinationType.DATA_URL });
		  		$scope.errMessage="attaching.. ";
				function onSuccess(imageData) {
					$scope.errMessage="attached";
					var image=angular.element(document.getElementById('myPhoto'));
				    image.attr('src', "data:image/jpeg;base64," + imageData);
				 	$scope.$apply();   
				}
				function onFail(message) {
					$scope.errMessage=message;

				}
		  	} else {
		  		$scope.errMessage="Camera is NOT ready";
		  		console.info("error");
		  	}
		  	
		  }; //takePicture
		  $scope.gotoTodo=function() {
		  		$ionicHistory.nextViewOptions({
  					disableBack: true
				});
		  		$state.go('app.todoList', {}, {location:'replace'});
				
		  };

		  $scope.scheduleNotification=function() {
		  	var now = new Date().getTime();
		  	var tenSecondsLater = new Date(now + 10 * 1000);
		  	todoService.list().then(function(resp) {
		  		var taskNums=resp.data.newTasks.length;
		  		if(taskNums>0) {
		  			var title="You have "+taskNums+" tasks";

				  	deviceCheckService.notify({
				  		id: 10,
						title: title,
						text: "Please go complete them to receive karma score",
						badge:taskNums,
						at: tenSecondsLater
						//sound:"file://sound.mp3"
						
				  	}).then(function(resp) {
				  		$scope.errMessage=resp.data + taskNums;
				  		
				  	},function(resp) {
				  		$scope.errMessage=resp.data;
				  	});


		  		}

		  	});//todo List
		  		

		  };//scheduleNotification

		 $scope.loginFacebook=function() {
		 	var permissions=["public_profile","email","user_friends","user_photos"];
		 	facebookService.login(permissions).then(function SUCCESS(resp) {
		 		$scope.errMessage=JSON.stringify(resp);
		 		$timout(function() {$scope.$apply();},100);
		 	}, function FAIL(resp) {
		 		$scope.errMessage=JSON.stringify(resp);
		 		$timeout(function() {$scope.$apply();},100);
		 	});
		 }//login facebook

		 $scope.facebookImages=[
		 	{source:'https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-prn2/v/t1.0-9/552630_532302626784987_102626387_n.jpg?oh=89bd48a861d580355ee34d685ea116ba&oe=556F9A53&__gda__=1437995037_2381a0414f0a8827fd3cfcb4677b8e25'},
		 	{source:'https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-prn2/v/t1.0-9/p600x600/552630_532302626784987_102626387_n.jpg?oh=e3cb8c7b7c1c2a972195470d43f7f130&oe=55726CF4&__gda__=1438318213_76da2761d9eccde12a88a5d0642a4a35'}
		 ];
		 $scope.getFacebookPhoto=function() {
		 	facebookService.getPhotos().then(function SUCCESS(resp){
		 		$scope.errMessage=JSON.stringify(resp);
		 		$scope.facebookImages=resp;
		 		$timout(function() {$scope.$apply();},100);
		 	},function FAIL(resp){
		 		$scope.errMessage=JSON.stringify(resp);
		 		$timeout(function() {$scope.$apply();},100);
		 	});
		 }//get Facebook Photo
	}//DeviceCheckCtrl

})();



