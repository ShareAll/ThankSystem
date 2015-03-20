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
    .controller('MainCtrl', ['$scope','authService','cardService',MainCtrl]);

function MainCtrl($scope,authService,cardService) {
    $scope.gallerySelection=0;
    $scope.card={
       state:0
    };
    $scope.auth=null;
    authService.getContext().then(function SUCCESS(resp){
        $scope.auth=resp.data;
    },function FAIL(resp) {
        window.location.href="index.html";
    });
    
  
   var dataList=[
      ["belated","nofear","loved","belated1","belated2","belated3"],
      ["nofear","shucks","loved","proverbs","belated1","belated2","belated3"],
      ["god","belated","nofear","loved","belated1","belated2","belated3"]
    ];
    //var index=0;
    
    var dataMap={
      "belated3":{"id":"belated3","name":"Belated3","icon":"images/card/birthday/cc_belated.80.tn.jpg"},
      "belated2":{"id":"belated2","name":"Belated2","icon":"images/card/birthday/cc_belated.80.tn.jpg"},
      "belated1":{"id":"belated1","name":"Belated1","icon":"images/card/birthday/cc_belated.80.tn.jpg"},
      "belated":{"id":"belated","name":"Belated","icon":"images/card/birthday/cc_belated.80.tn.jpg"},
      "shucks":{"id":"shucks","name":"Shucks","icon":"images/card/birthday/shucks.80.tn.jpg"},
      "nofear":{"id":"nofear","name":"nofear","icon":"images/card/birthday/no_fear.jpg"},
      "loved":{"id":"loved","name":"loved","icon":"images/card/birthday/loved-sparkle.jpg"},
      "proverbs":{"id":"proverbs","name":"proverbs","icon":"images/card/birthday/proverbs.jpg"},
      "god":{"id":"god","name":"god","icon":"images/card/birthday/gods-masterpiece.jpg"}
    }
    $scope.appList=getAppListByIndex(0);
    function getAppListByIndex(index) {
        var appList=[];
        $.each(dataList[index],function(ind,val) {
          appList.push(dataMap[val]);
        });
        return appList;      
    }
    $scope.reOrder=function(index) { 
        $scope.appList=getAppListByIndex(index);
        $scope.gallerySelection=index;
    } ;
    
    $scope.openSendDlg=function(app) {
      $scope.selectedApp=app;
      $('#dlgSendCard').modal();
    }
    $scope.sendCard=function() {
      $scope.card.state=1;
      $scope.card.fromEmail=$scope.auth.emailAddress;
      $scope.card.templateName=$scope.selectedApp.name;
      cardService.send($scope.card)
        .success(function(resp) {
           $scope.card.state=0;
            $('#dlgSendCard').modal("hide");  
        })
        .error(function(resp){
            $scope.card.state=0;
            $scope.card.lastError=resp.errorMsg; 
            
        });
      
    }
};


});
