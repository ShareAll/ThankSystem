(function() {
	angular.module('thank.controllers.helpListCtrl', [])
		.controller('helpListCtrl', ['$rootScope','$scope','$timeout','$stateParams','helpListService','$ionicModal',HelpListCtrl]);

	function HelpListCtrl($rootScope,$scope,$timeout,$stateParams,helpListService,$ionicModal) {
		  $scope.helpList = [];
		  $rootScope.curGoal=null;
		  console.info("Load GOAL Control");
		  $scope.goalData={
		  	'goalText':''
		  };
          var lastRefreshTime=0;
          $scope.changeTab=function(mytab) {
            if(mytab) {
                $scope.view={
                    "tab_myhelp":true,
                    "title":"Help Me"
                };
            } else {
                $scope.view={
                    "tab_myhelp":false,
                    "title":"Help Others"
                };
            }
            refreshHelpList();
          };
          $scope.changeTab(true);
		  $scope.setCurGoal=function(help){
		  	$rootScope.curGoal=help;
		  };

		 function refreshHelpList() {
            var promise=null;
            if($scope.view.tab_myhelp) {
                promise=helpListService.list();
            } else {
                promise=helpListService.listBySubscriber();
            }
            promise.then(function SUCCESS(resp) {
                $scope.helpList=resp.data;
                $timeout(function() {
                    $('div.circliful:not(:has(span.circle-text))').circliful();
                },100);
                    //console.dir($scope.goals);    
            });                
         }

         $scope.$on('$ionicView.enter', function() {
            if(lastRefreshTime==0 || ($rootScope.lastUpdateTime && $rootScope.lastUpdateTime>lastRefreshTime)) {
                console.info("refresh list");
                refreshHelpList();
                lastRefreshTime=new Date().getTime();
            }
         });
         
		  

		  /**Open New Goal Window**/
		  $ionicModal.fromTemplateUrl('templates/newGoal.html', {
    		scope: $scope,
    		animation: 'slide-in-up'
  		  }).then(function(modal) {
    			$scope.goalModal = modal;
                $scope.goalData={friends:[],
                    experts:[
                     {"name":"expert1@google.com",selected:false},
                     {"name":"expert2@google.com",selected:false}
                    ]
                };
                $.each($rootScope.currentUser.friends,function(ind,val){
                    if(val!=$rootScope.currentUser.emailAddress) {
                        $scope.goalData.friends.push({
                            name:val,
                            selected:false
                        })

                    }
                });
                
  		  });
  		  $scope.openGoalModal = function() {
  		  	console.info("open modal");
    		$scope.goalModal.show();
  		  };
  		  $scope.submitGoal=function() {
            var payload={};
            payload.title=$scope.goalData.title;
            payload.subscribers=[];
            var subscribers={};
            $.each($scope.goalData.friends,function(ind,val){
                if(val.selected) {
                    subscribers[val.name]=true;
                }
            });
            $.each($scope.goalData.experts,function(ind,val){
                if(val.selected) {
                    subscribers[val.name]=true;
                }
            });
            $.each(subscribers,function(ind,val){
                payload.subscribers.push(ind);
            });
            console.dir(payload);
            
  		  	helpListService.add(payload).then(function() {
  		  		$scope.closeModal();
  		  		refreshHelpList();
  		  	});
  		  }
  		  $scope.closeModal = function() {
    		$scope.goalModal.hide();
  		  };
  			
  		  $scope.$on('$destroy', function() {
    			$scope.goalModal.remove();
  		  });
  
	}//GoalCtrl

})();



