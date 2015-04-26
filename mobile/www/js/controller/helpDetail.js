(function() {
	
	angular.module('thank.controllers.helpDetailCtrl', [])
		.controller('helpDetailCtrl', ['$rootScope','$scope','$stateParams','$interval','helpListService','helpDetailService','$ionicScrollDelegate','$timeout','$mdBottomSheet',
      '$ionicLoading','$ionicModal','$mdDialog',HelpDetailCtrl]);

	function HelpDetailCtrl($rootScope,$scope,$stateParams,$interval,helpListService,helpDetailService,$ionicScrollDelegate,$timeout,$mdBottomSheet,$ionicLoading,$ionicModal,$mdDialog) {
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
  var curUser=$rootScope.currentUser.emailAddress;
  var curUserName=$rootScope.currentUser.name;
  console.info("helpId="+helpId+",curUser="+curUser);
  $scope.curUser=curUser;
	var messageCheckTimer;
	var viewScroll = $ionicScrollDelegate.$getByHandle('userMessageScroll');
    var footerBar; // gets set in $ionicView.enter
    var scroller;
    var txtInput; // ^^^
    var lastCommentId="";
    $scope.messages=[];

    // init edit invitation dialog
   
      
      
    
     
 

  function refreshComment(fn) {
   
    helpDetailService.listComment($rootScope.curGoal.owner,helpId,curUser,lastCommentId).then(function(resp) {
        if(resp.data) {
          $.each(resp.data,function(ind,val) {
            $scope.messages.push(val);
            lastCommentId=val.id;
          });
        }
        
    }).finally(function(){
      if(fn) {
          fn();
        }
      
    });
    
  }
	$scope.$on('$ionicView.enter', function() {
      	//console.log('UserMessages $ionicView.enter');
        $ionicLoading.show({
          template: 'Loading...'
        });
        refreshComment(function() {
          $ionicLoading.hide();
        });
      
      	$timeout(function() {
	        footerBar = document.body.querySelector('#userMessagesView .bar-footer');
	        scroller = document.body.querySelector('#userMessagesView .scroll-content');
	        txtInput = angular.element(footerBar.querySelector('textarea'));
	    }, 0);

      	messageCheckTimer = $interval(function() {
          refreshComment();
        	
      	}, 3000);
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
      	
      	keepKeyboardOpen();
      // if you do a web service call this will be needed as well as before the viewScroll calls
      // you can't see the effect of this in the browser it needs to be used on a real device
      // for some reason the one time blur event is not firing in the browser but does on devices
      //keepKeyboardOpen();
      
      //MockService.sendMessage(message).then(function(data) {
     // $scope.input_message = '';
     if($scope.input_message && $scope.input_message.length>0) {
        helpDetailService.sendComment(helpId,$scope.input_message,curUser,curUserName).then(function(resp1) {
            $scope.input_message="";
            refreshComment(function() {
              $timeout(function() {
                  keepKeyboardOpen();
                  viewScroll.scrollBottom(true);
              }, 0);

            });
            
           
        });

     }

 
    }; //$scope.sendMessage

  

    $scope.updateProgress = function(incr) {
      if($rootScope.curGoal.completeness>=100) return;
      if($rootScope.curGoal.completeness) {
        $rootScope.curGoal.completeness+=incr;
      } else {
        $rootScope.curGoal.completeness=incr;
      }

      helpListService.updateProgress(helpId,$rootScope.curGoal.completeness);
      $rootScope.lastUpdateTime=new Date().getTime();
      $mdBottomSheet.hide();
    };

    

    $scope.closeHelp=function() {
      console.info("close help");
      $mdBottomSheet.hide();
    };
    $scope.editInvitation=function(ev) {
      console.info("Edit Invitation");
      $mdBottomSheet.hide();
      modelScope=$scope.modal_invite_more.scope;
      modelScope.init();
      $scope.modal_invite_more.show();
      
    };

    $scope.showListBottomSheet=function($event) {
        $mdBottomSheet.show({
          templateUrl: 'templates/helpDetail_top_action.html',
          controller: 'helpDetailCtrl',
          targetEvent: $event
        }).then(function() {
          //console.info(clickedItem.name + ' clicked!');
        });

    }
    $scope.showGridBottomSheet = function($event) {
        $scope.alert = '';
        $mdBottomSheet.show({
          templateUrl: 'templates/helpDetail_action.html',
          controller: 'helpDetailCtrl',
          targetEvent: $event
        }).then(function() {
          //console.info(clickedItem.name + ' clicked!');
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
	

    //init dialogs
    $ionicModal.fromTemplateUrl('invite_more_to_help.html', {
        scope:null,
        animation: 'slide-in-up'
    }).then(function(modal) {
          // invite more friend to help
          $scope.modal_invite_more = modal;
          modal.scope.init=function(){
            var modalScope=modal.scope;
            modalScope.data={
                friends:[]
            }
            var invites={};
            $.each($rootScope.curGoal.subscribers,function(ind,val){
                invites[val]=val;
            });
            $.each($rootScope.currentUser.friends,function(ind,val){
                modalScope.data.friends.push({
                  name:val,
                  selected:invites[val]!=undefined
                })
            });
          };
          modal.scope.submit=function() {
              console.info("click submit");
              var subscribers=[];
              $.each(modal.scope.data.friends,function(ind,val){
                  if(val.selected) subscribers.push(val.name);
              })
              
              helpListService.updateInvitation(helpId,subscribers)
              .then(function SUCCESS(){
                $rootScope.curGoal.subscribers=subscribers;
                
              });
              //console.dir(modal.scope.data)
              modal.hide(); 
          }   
          modal.scope.close=function() {
              console.info("close dlg");
              modal.hide(); 
          }
          console.dir(modal);     
    });


		
} //HelpDetailCtrl


})();