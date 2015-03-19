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
    var dataList=[
      ["belated","nofear","loved"],
      ["nofear","shucks","loved","proverbs"],
      ["god","belated","nofear","loved"]
    ];
    var index=0;
    $scope.appList=[];
    var dataMap={
      "belated":{"id":"belated","name":"Belated","icon":"images/card/birthday/cc_belated.80.tn.jpg"},
      "shucks":{"id":"shucks","name":"Shucks","icon":"images/card/birthday/shucks.80.tn.jpg"},
      "nofear":{"id":"nofear","name":"nofear","icon":"images/card/birthday/no_fear.jpg"},
      "loved":{"id":"loved","name":"loved","icon":"images/card/birthday/loved-sparkle.jpg"},
      "proverbs":{"id":"proverbs","name":"proverbs","icon":"images/card/birthday/proverbs.jpg"},
      "god":{"id":"god","name":"god","icon":"images/card/birthday/gods-masterpiece.jpg"}
    }
    $scope.reOrder=function() {
      var appList=[];
      $.each(dataList[index],function(ind,val) {
        appList.push(dataMap[val]);
      });
      $scope.appList=appList;
      
      index++
      index=(index)%dataList.length;
      console.info(index);
    }
    $scope.initCarousel=function() {
      $('.carousel').carousel();
    };

   
   
};


});
