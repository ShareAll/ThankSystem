(function() {
	angular.module('thank.controllers.helpListCtrl', [])
		.controller('helpListCtrl', ['$q','$rootScope','$scope','$timeout','$state','$stateParams','helpListService','$ionicModal','$ionicLoading','loginService',HelpListCtrl]);

	function HelpListCtrl($q,$rootScope,$scope,$timeout,$state,$stateParams,helpListService,$ionicModal,$ionicLoading,loginService) {
		  $scope.helpList = [];
		  $rootScope.curGoal=null;
		  console.info("Load GOAL Control");
		 

      $scope.goalData={
		  	'goalText':'',
            'categoryText':"Select Category"
		};
      

      var lastRefreshTime=0;

     
         // $scope.changeTab(true);
		  $scope.setCurGoal=function(help){
		  	$rootScope.curGoal=help;
		  };

		 function refreshHelpList() {
            var promise=null;

            $ionicLoading.show({
              template: 'Loading...'
            });
            //always clear existing cache
            $scope.helpList=[];
            if($state.current.name=="tab.helpMe") {
                promise=helpListService.list();
            } else {
                promise=helpListService.listBySubscriber();
            }
            var userPromise=loginService.getCurrentUser();
            $q.all([promise, userPromise]).then(function(resps){
                var helpList=resps[0].data;
                console.dir($scope.helpTrack);
                $.each(helpList,function(ind,val){
                    var prevId=$rootScope.helpTrack[val.id];
                    if(!prevId) {
                        prevId=val.lastPos;
                        $rootScope.helpTrack[val.id]=prevId;
                    }
                    val.unread=val.lastPos-prevId;
                });

                $scope.helpList=helpList;
                $timeout(function() {
                    $('div.circliful:not(:has(span.circle-text))').circliful();
                },100);
                $rootScope.currentUser=resps[1].data;
                var map={};
                $.each($rootScope.currentUser.friends,function(ind,val) {
                    map[val.emailAddress]=val;
                });
                $rootScope.friendMap=map;
                
            }).finally(function() {
              $ionicLoading.hide();
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
                
                $scope.loadCategories=function() {
                    helpListService.listAllCategories().then(function SUCCESS(resp) {
                        $scope.categories=  resp.data;
                        console.info(resp);
                    });
                    
                };

                
  		  });
          /***Open Goal Selection Window**/
        $ionicModal.fromTemplateUrl('templates/categoryDlg.html', {
            scope: $scope,
            animation: 'slide-in-up'
          }).then(function(modal) {
                    
          });
          $scope.openCategoryModal=function() {
            $scope.categoryModal.show();
          }
          $scope.closeCategoryModal=function() {
            $scope.categoryModal.hide();
          }
          $scope.selectCategory=function(cat) {
            $scope.selectedCategory=cat;
            $scope.categoryModal.hide();
          }

  		  $scope.openGoalModal = function(help) {
            $scope.modal_newGoal.scope.init(help);
  		  	$scope.modal_newGoal.show();
    		//$scope.goalModal.show();
           
  		  };

  		  $scope.submitGoal=function() {
            var payload={};
            payload.title=$scope.goalData.title;
            payload.subscribers=[];
            payload.categoryId=$scope.goalData.categoryId;
            var subscribers={};
            $.each($scope.goalData.friends,function(ind,val){
                if(val.selected) {
                    subscribers[val.emailAddress]=true;
                }
            });
            $.each($scope.goalData.experts,function(ind,val){
                if(val.selected) {
                    subscribers[val.emailAddress]=true;
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
  			
        $scope.$on('$ionicView.enter', function() {
            refreshHelpList();
        });
  		$scope.$on('$destroy', function() {
    		$scope.goalModal.remove();
  		});
        var category_promise=helpListService.listAllCategories();
        category_promise.then(function SUCCESS(resp) {
            $scope.categories=[];
            $.each(resp.data,function(ind,val) {
                $scope.categories.push({
                    id:val.id,
                    text:val.name,
                    checked:false,
                    icon:null
                });
            });
            $ionicModal.fromTemplateUrl('templates/newGoal.html', {
                scope:null,
                animation: 'slide-in-up'
            }).then(function(modal) {
                
                $scope.modal_newGoal = modal;
                modal.scope.curHelp=null;   
                modal.scope.$watch("goalData.categoryText",function(newVal,oldVal) {
                    console.info("newVal="+newVal);
                });          
                modal.scope.init=function(help){
                    var modalScope=modal.scope;

                    modalScope.curHelp=help;
                    modalScope.categories=$scope.categories;
                    console.dir(modalScope.categories);

                    modalScope.goalData={
                        "title":"",
                        isPublic:true,
                        //privacy:0,
                        friends:[],
                        experts:[
                             {"name":"expert1@google.com",selected:false},
                             {"name":"expert2@google.com",selected:false}
                        ]
                    };
                    
                    $.each($rootScope.currentUser.friends,function(ind,val){
                        if(val!=$rootScope.currentUser.emailAddress) {
                            modalScope.goalData.friends.push({
                                name:val.name,
                                emailAddress:val.emailAddress,
                                selected:false
                            });
                        }
                    });

                    if(help) {
                        modalScope.title="Edit";
                        //modalScope.goalData.privacy=help.privacy;
                        modalScope.goalData.isPublic=(help.privacy==1);
                        modalScope.goalData.title=help.title;
                        modalScope.goalData.categoryId=help.categoryId;
                        $.each(modalScope.categories,function(ind,val){
                            if(val.id==help.categoryId) {
                                modalScope.goalData.categoryText=val.text;
                            }
                            
                        });
                        
                        var selectedMap={};
                        
                        $.each(help.subscribers || [],function(ind,val){
                            selectedMap[val]=val;
                        });
                        $.each(modalScope.goalData.friends || [],function(ind,val){
                            if(selectedMap[val.emailAddress]) {
                                val.selected=true;
                            }
                        });
                    } else {
                        modalScope.title="Create";
                        modalScope.goalData.categoryText="Select Cateogory";
                        modalScope.goalData.categoryId=0;
                    }
                    $timeout(function() {
                        modalScope.$apply();
                        console.info("apply");
                    },0)
                    console.dir(modalScope);
                };
                modal.scope.submit=function() {
                    var payload={};
                    var modalScope=modal.scope;
                    payload.title=modalScope.goalData.title;
                    payload.subscribers=[];
                    payload.categoryId=modalScope.goalData.categoryId;
                    payload.privacy=modalScope.goalData.isPublic?1:2;
                    var subscribers={};
                    $.each(modalScope.goalData.friends,function(ind,val){
                        if(val.selected) {
                            subscribers[val.emailAddress]=true;
                        }
                    });
                    $.each(modalScope.goalData.experts,function(ind,val){
                        if(val.selected) {
                            subscribers[val.emailAddress]=true;
                        }
                    });
                    $.each(subscribers,function(ind,val){
                        payload.subscribers.push(ind);
                    });
                    console.dir(payload);
                    if(modalScope.curHelp) {
                        helpListService.update(modalScope.curHelp.id,payload).then(function() {
                            modal.hide(); 
                            refreshHelpList();
                        }); 
                    } else {
                        helpListService.add(payload).then(function() {
                            modal.hide(); 
                            refreshHelpList();
                        });  
                    }
                    
                }; 
                modal.scope.close=function() {
                    console.info("close dlg");
                    modal.hide(); 
                } 
                  
            });//$ionicModal.fromTemplateUrl
           
        });//category_promise.then



  
	}//GoalCtrl

})();



