(function() {
	
	angular.module('thank.controllers.helpDetailCtrl', [])
		.controller('helpDetailCtrl', ['$scope','$stateParams','$interval','helpListService','helpDetailService','$ionicScrollDelegate','$timeout','$mdBottomSheet',HelpDetailCtrl]);

	function HelpDetailCtrl($scope,$stateParams,$interval,helpListService,helpDetailService,$ionicScrollDelegate,$timeout,$mdBottomSheet) {
	/*	var curId=0;
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
*/
	var helpId=$stateParams.helpId;

	
	$scope.curUser='fenwang';
	var messageCheckTimer;
	var viewScroll = $ionicScrollDelegate.$getByHandle('userMessageScroll');
    var footerBar; // gets set in $ionicView.enter
    var scroller;
    var txtInput; // ^^^

	$scope.$on('$ionicView.enter', function() {
      	console.log('UserMessages $ionicView.enter');
      	helpDetailService.getMessages(helpId).then(function(resp) {
      		$scope.messages=resp.data
      	});
      
      	$timeout(function() {
	        footerBar = document.body.querySelector('#userMessagesView .bar-footer');
	        scroller = document.body.querySelector('#userMessagesView .scroll-content');
	        txtInput = angular.element(footerBar.querySelector('textarea'));
	    }, 0);

      	messageCheckTimer = $interval(function() {
        	// here you could check for new messages if your app doesn't use push notifications or user disabled them
      	}, 20000);
    });

    $scope.$on('$ionicView.leave', function() {
      	console.log('leaving UserMessages view, destroying interval');
      	// Make sure that the interval is destroyed
      	if (angular.isDefined(messageCheckTimer)) {
        	$interval.cancel(messageCheckTimer);
        	messageCheckTimer = undefined;
      	}
    });


	$scope.sendMessage=function(msgForm) {
      	var message = {
      		date:new Date(),
        	username:$scope.curUser,
        	text: $scope.input_message
      	};
      	keepKeyboardOpen();
      // if you do a web service call this will be needed as well as before the viewScroll calls
      // you can't see the effect of this in the browser it needs to be used on a real device
      // for some reason the one time blur event is not firing in the browser but does on devices
      //keepKeyboardOpen();
      
      //MockService.sendMessage(message).then(function(data) {
      $scope.input_message = '';

      helpDetailService.send(message).then(function(resp) {
      	  $scope.messages=resp.data;
      	  $timeout(function() {
        	keepKeyboardOpen();
        	viewScroll.scrollBottom(true);
     	  }, 0);

      });

 
    }; //$scope.sendMessage

    $scope.actions=[{
      "name":"+10",
      "icon":"fa-plus-circle"
    }];
    $scope.showGridBottomSheet = function($event) {
        $scope.alert = '';
        $mdBottomSheet.show({
          templateUrl: 'templates/helpDetail_action.html',
          controller: 'helpDetailCtrl',
          targetEvent: $event
        }).then(function(clickedItem) {
          $scope.alert = clickedItem.name + ' clicked!';
        });
      };


     // this keeps the keyboard open on a device only after sending a message, it is non obtrusive
    function keepKeyboardOpen() {
      console.log('keepKeyboardOpen');
      txtInput.one('blur', function() {
        console.log('textarea blur, focus back on it');
        txtInput[0].focus();
      });
    } //keepKeyboardOpen
	

		
} //HelpDetailCtrl


})();