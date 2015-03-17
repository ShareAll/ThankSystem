define(['angular'], function (angular) {
  'use strict';

  /**
   * @ngdoc function
   * @name wftoolsApp.controller:MainCtrl
   * @description
   * # MainCtrl
   * Controller of the wftoolsApp
   */
  angular.module('wftoolsApp.controllers.MainCtrl', [
    'uiGmapgoogle-maps'])
    .controller('MainCtrl', ['$scope','authService',MainCtrl]);

function MainCtrl($scope,authService) {
    $scope.auth=null;
    authService.getContext().then(function SUCCESS(resp){
        $scope.auth=resp.data;
    },function FAIL(resp) {
        window.location.href="index.html";
    });
    
   
   
};


});
