(function() {
	angular.module('thank.controllers.deviceCheckCtrl', [])
		.controller('deviceCheckCtrl', ['$scope','$stateParams','$location','deviceCheckService','todoService','$state','$ionicHistory',DeviceCheckCtrl]);

	function DeviceCheckCtrl($scope,$stateParams,$location,deviceCheckService,todoService,$state,$ionicHistory) {
		  
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

		 

	}//DeviceCheckCtrl

})();



