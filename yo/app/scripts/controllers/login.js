define(['angular','angular-material'], function (angular) {
  'use strict';

  angular.module('wftoolsApp.controllers.LoginCtrl', ['ngMaterial','ngMessages'])
    .controller('loginCtrl', ['$scope','$timeout','$location','authService',LoginCtrl]);

function LoginCtrl($scope,$timeout,$location,authService) {
    
    $scope.login={
      display:false,
      leaving:false,
      isSignUp:false,
      userName:'',
      password:'',
      emailAddress:'',
      state:0,
      repeatPassword:''
    };
    
    /*$scope.loginForm={
      loginName:"",
      data:{},
      display:false,
      leaving:false
    };*/
    $scope.login=function() {
        $scope.login.state=1;
        authService.login($scope.login)
        .then(SUCCESS,FAIL);
        function SUCCESS(resp) {
           // $scope.auth.curUser=resp.data.userName;
            $scope.login.state=0;
            window.location.href="welcome.html";
            //$location.path("../index2.html");
        }
        function FAIL(resp) {
          $scope.login.state=0;
          $scope.login.lastError=resp.data.errorMsg;
        }

    
    }
    $scope.signUp=function() {
        authService.signUp($scope.login)
        .then(SUCCESS,FAIL);
        function SUCCESS(resp) {
            window.location.href="welcome.html";
        }
        function FAIL(resp) {
          $scope.login.lastError=resp.data.errorMsg; 
        }
    }


    $scope.hideLoginForm=function() {
      /*
      $scope.loginForm.leaving=true;
      $timeout(function() {
        if($scope.loginForm.leaving) {
          $scope.loginForm.display=false;
        } 
      },500);
      */
    };
    

}; //End of LoginCtrl


});
