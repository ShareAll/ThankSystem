define(['angular'], function (angular) {
  'use strict';

  /**
   * @ngdoc function
   * @name wftoolsApp.controller:MainCtrl
   * @description
   * # MainCtrl
   * Controller of the wftoolsApp
   */
angular.module('wftoolsApp.controllers.datatableCtrl', [])
    .controller('DatatableCtrl',['$scope', DatatableCtrl]);

function DatatableCtrl($scope) {
  var data=[{
    "id":"1","name":"fenwang"
  },{
    "id":"2","name":"edchen"
  }];
  $scope.wfDatatable_data=data;
  $scope.doAction=function(ind,aData) {
    console.info("Reach Scope with "+aData.id);
  };
  $scope.doAction2=function(ind,aData) {
    console.info("DoAction 2");
  }
};



///END 
});
