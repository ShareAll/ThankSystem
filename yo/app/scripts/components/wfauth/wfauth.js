define(['angular'],function(angular) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wfauth',[])
.directive('wfAuth',['$http',WfAuth]);
      

function WfAuth($http) {
      return {
            restrict:'EA',
            link: function($scope,elm,attrs) { 
                  var failUrl=attrs["failUrl"];
                  var authUrl=attrs["authUrl"];
                  $http.get(authUrl).then(SUCCESS,FAIL);
                  function SUCCESS(resp) {
                        $scope.auth=resp.data;
                  }
                  function FAIL(resp) {
                        $scope.auth=resp.data;
                        window.location.href=failUrl;
                  }
            }
  	};
};


});