define(['angular'], function (angular) {
  'use strict';

  /**
   * @ngdoc function
   * @name wftoolsApp.controller:MainCtrl
   * @description
   * # MainCtrl
   * Controller of the wftoolsApp
   */
  angular.module('wftoolsApp.controllers.IndexCtrl', [])
    .controller('IndexCtrl', ['$scope',IndexCtrl]);

function IndexCtrl($scope) {
    
    $scope.initCarousel=function() {
      $('.carousel').carousel();
    };

   
   
};


});
