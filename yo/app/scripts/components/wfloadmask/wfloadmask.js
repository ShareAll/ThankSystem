define(['angular'],function(angular) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wfloadmask',[])
.directive('wfloadmask',[Wfloadmask]);

function Wfloadmask() {
	return {
      restrict:'EA',
      template:
      		"<div class=\"test\">"+
      			"<div ng-show='inLoading'>Loading</div>"+
            	"<ng-transclude></ng-transclude>"+
            "</div>",
      transclude:true,
      link: function($scope,elm,attrs) {
      	
      	$scope.loadmask=function() {
      		$scope.inLoading=true;
      	};
      	$scope.unloadmask=function() {
      		$scope.inLoading=false;
      	};
      }
  	};
};


});