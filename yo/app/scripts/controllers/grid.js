define(['angular','ui-grid'], function (angular) {
  'use strict';

  /**
   * @ngdoc function
   * @name wftoolsApp.controller:MainCtrl
   * @description
   * # MainCtrl
   * Controller of the wftoolsApp
   */
angular.module('wftoolsApp.controllers.GridCtrl', [
  'ui.grid','ui.grid.edit','ui.grid.selection'])
    .controller('GridCtrl',['$scope', GridCtrl]);

function GridCtrl($scope) {
  var data=[{
    "id":"1","name":"fenwang"
  },{
    "id":"2","name":"edchen"
  }];

  $scope.gridOptions = {
    showGridFooter: true,
    showColumnFooter: true,
    enableFiltering: true,
    columnDefs : [
         { name: 'id' },
         { name: 'name'},
         { name: 'ShowScope',
           cellTemplate:'<button class="btn primary" ng-click="grid.appScope.showMe()">Click Me</button>' 
          }],
    data:data

  };

  



};



///END 
});
