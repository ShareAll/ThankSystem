(function() {
	
	angular.module('thank.controllers.helpDetailCtrl', [])
    .filter('voterListFilter',[voterListFilter])
		.controller('helpDetailCtrl', ['$rootScope','$scope','$stateParams','$interval','helpListService','helpDetailService','$ionicScrollDelegate','$timeout','$mdBottomSheet',
      '$ionicLoading','$ionicModal','$mdDialog','$ionicHistory','$state',HelpDetailCtrl]);


  function voterListFilter() { 
      return function(votes) {
          if(!votes || votes.length==0) return "No one voted";
          if(votes.length==1) {
            return votes[0].substring(0,10) +" likes it"
          } 
          var ret=votes[0].substring(0,10)
          for(var i=1;i<votes.length && i<3;i++) {
            ret+=","+votes[i].substring(0,10)
          }
          if(votes.length>3) {
            ret+=" and other "+(votes.length-3)+" friends "
          }
          ret+=" like it"
          return ret;
      }
  }

	function HelpDetailCtrl($rootScope,$scope,$stateParams,$interval,helpListService,helpDetailService,$ionicScrollDelegate,$timeout,$mdBottomSheet,$ionicLoading,$ionicModal,$mdDialog,$ionicHistory,$state) {
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
  $scope.searchBar={
    txt:'',
    show:false
  }

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
   
    helpDetailService.listComment($rootScope.curGoal.owner,helpId,curUser,$rootScope.curGoal.privacy,lastCommentId).then(function(resp) {
        if(resp.data) {
        //  console.dir(resp.data);
          if(resp.data.comments) {
              $.each(resp.data.comments,function(ind,val) {
                if(val.id>lastCommentId) {
                  $scope.messages.push(val);
                  lastCommentId=val.id;
                  $rootScope.helpTrack[val.helpId]=val.pos;
                }
              });   
          }
          if(resp.data.users) {
            //rebuild friend map
            $rootScope.friendMap={};
            $.each(resp.data.users,function(ind,val) {
                $rootScope.friendMap[val.emailAddress]=val;
            });
          }
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

  $scope.toggleSearchBar=function($event) {
     $scope.searchBar.txt='';
     $scope.searchBar.show=!$scope.searchBar.show;
  }

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


    $scope.voteComment=function(message) {
      helpDetailService.voteComment($rootScope.currentUser.name,message).then(function SUCCESS(resp){
          message.voted=resp.data.voted;
      });
    }
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
      modelScope=$scope.modal_close_help.scope;
      modelScope.init();
      $scope.modal_close_help.show();
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
          //controller: 'helpDetailCtrl',
          scope:$scope,
          preserveScope:true,
        }).then(function() {
          console.dir($scope.messages);
          //console.info(clickedItem.name + ' clicked!');
        });

    }
    $scope.showGridBottomSheet = function($event) {
        $scope.alert = '';
        $mdBottomSheet.show({
          templateUrl: 'templates/helpDetail_action.html',
          scope:$scope,
          preserveScope:true,
          //controller: 'helpDetailCtrl',
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
	

    //init dialogs for invite more 
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
                  name:val.name,
                  emailAddress:val.emailAddress,
                  selected:invites[val.emailAddress]!=undefined
                })
            });
            
          };
          
          modal.scope.submit=function() {
              console.info("click submit");
              var subscribers=[];
              $.each(modal.scope.data.friends,function(ind,val){
                  if(val.selected) subscribers.push(val.emailAddress);
              })
              
              helpListService.updateInvitation(helpId,subscribers)
              .then(function SUCCESS(){
                $rootScope.curGoal.subscribers=subscribers;
                
              });
              //console.dir(modal.scope.data)
              modal.hide(); 
          };
          modal.scope.close=function() {
              console.info("close dlg");
              modal.hide(); 
          }
          //console.dir(modal);     
    }); //modal for invite more

  //modal for close help
   $ionicModal.fromTemplateUrl('close_help.html', {
        scope:null,
        animation: 'slide-in-up'
    }).then(function(modal) {
          // invite more friend to help
          $scope.modal_close_help = modal;
          modal.scope.init=function(){
            var modalScope=modal.scope;
            modalScope.messages=[];
            modalScope.data={
              conclusion:""
            };
            $.each($scope.messages,function(ind,val){
                var new_msg=angular.copy(val);
                new_msg.selected=false;
                new_msg.origin_index=ind;
                modalScope.messages.push(new_msg);
                modalScope.selectedCount=0;
            });
            
          };
         
          modal.scope.toggle_msg=function(msg) {
              var modalScope=modal.scope;
              var targetChecked=msg.selected;
              
              var index=-1;
              var msg_val=null;
              $.each(modalScope.messages,function(ind,val){
                  if(index==-1 && val.id==msg.id) {
                      index=ind;
                      msg_val=val;
                  }
              });
              if(index==-1) return;
              if(targetChecked) {
                  modalScope.messages.splice(index, 1);
                  modalScope.messages.splice(modalScope.selectedCount, 0,msg_val);
                  modalScope.selectedCount++;
              } else {
                  var newPos=modalScope.messages.length-1;
                  modalScope.messages.splice(index, 1);
                  for(var i=msg.origin_index;i<modalScope.messages.length;i++) {
                      var msgItem=modalScope.messages[i];
                      if(!msgItem.selected && msgItem.origin_index>msg.origin_index) {
                        newPos=i;
                        break;
                      }
                  }
                 
                  modalScope.messages.splice(newPos, 0,msg_val);
                  modalScope.selectedCount--;
              }

              var contents=[];
              $.each(modalScope.messages,function(ind,val){
                  if(val.selected) {
                      contents.push(val.content);
                  }
              });
              modalScope.data.conclusion=contents.join("\r\n");

          };
          modal.scope.completeHelp=function() {
              console.info("click completeHelp");
              $ionicLoading.show({template: 'Loading...'});
              helpListService.completeHelp(helpId,modal.scope.data.conclusion).then(function SUCCESS(resp) {
                  modal.hide(); 
                  $ionicHistory.nextViewOptions({
                      disableBack: true
                  });
                  $state.go('tab.helpMe', {}, {location:'replace'});
              }).finally(function() {
                  $ionicLoading.hide();
              });
              

          }   
          modal.scope.close=function() {
              console.info("close dlg");
              modal.hide(); 
          }
          //console.dir(modal);     
    }); //modal for close help


		
} //HelpDetailCtrl


})();