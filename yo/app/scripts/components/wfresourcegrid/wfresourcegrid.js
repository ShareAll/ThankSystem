define(['angular','angular-material','components/wfresourcegrid/wfresourcegridDirective','components/wfresourcegrid/wfresourcegridService'],
function(angular,material,resourcegridDirective,resourcegridService) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wfresourcegrid',[
	'wfresourcegrid.wfResourcegridDirective',
	'ngMaterial'
])
	.controller('wfResourcegridCtrl',['$scope','$mdSidenav','wfResourcegridService',WfResourcegridCtrl]);


function WfResourcegridCtrl($scope,$mdSidenav,wfResourcegridService) { 		
	//console.info("reset");
	$scope.wfresourcegrid={
		data:[],
		context:{
			showAsStack:false
		}
	};
    $scope.openStep=function(step) {   	
		if(step.snapshot) {
			$scope.wfresourcegrid.data=step.snapshot;			
		} else {
			$scope.wfresourcegrid.data=[];
		}
    }; 
	$scope.loadSteps=function() {
		wfResourcegridService.getSteps()
		.then(function(steps) {
			$scope.resourceGridSteps=steps;
			if(steps && steps.length>0 && steps[steps.length-1].snapshot) {
				$scope.openStep(steps[steps.length-1]);
	    	} else {
	        	$scope.openStep(null);
	    	}
	 		//NO DIGEST HERE because setResourceGridData already trigger digest.
		});
	};

	
	
  
	$scope.loadData=function() {
		wfResourcegridService.getData($scope.tenentId)
		.then(function(data) {
			$scope.wfresourcegrid.data=data;	
		})	
	};
	
	$scope.closeLeftMenu=function() {
      $mdSidenav('leftMenu').close().then(function(){
          console.info("Close Left menu");
      });
    };
    
    $scope.toggleNav=function() {
		$mdSidenav('leftMenu').toggle().then(function(){
			console.info("toggle Left is done");
	    });
 	};
};

/*****END DEFINITION***/
});
