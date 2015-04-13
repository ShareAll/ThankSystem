// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('starter', [
  'ionic','thank.common','starter.controllers',
  'thank.controllers','thank.services'
])
.constant('apiBase',"http://52.11.234.40:8080/ThankWeb/rest")
.run(function($ionicPlatform,$timeout,$rootScope,$ionicHistory,$state,loginService) {
  
  //check login state
  $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
    var requireLogin = toState.data.requireLogin;

    if (requireLogin && typeof $rootScope.currentUser === 'undefined') {
        event.preventDefault();
        $ionicHistory.nextViewOptions({
                disableBack: true
        });

        $state.go('app.login');
    }
    
  });

  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
        StatusBar.styleDefault();
    }

    //INIT LOG CONSOLE
    if (window.cordova && window.cordova.logger) {
            window.cordova.logger.__onDeviceReady();
    }
    //INIT NOTIFICATION
    if (window.plugin && window.plugin.notification) {

        window.plugin.notification.local.setDefaults({
            autoCancel: true
        });
        if (window.device && window.device.platform === 'iOS') {
            window.plugin.notification.local.registerPermission();
        }
        window.plugin.notification.local.on('click', function (notification) {
            console.log("click "+notification.id);
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go('app.todoList', {}, {location:'replace'});
            console.log("click 2 "+notification.id);
            //$timeout(function () {
            //  $rootScope.$broadcast('cordovaLocalNotification:click', notification);
            //});
        });

        window.plugin.notification.local.on('trigger', function (notification) {
            $timeout(function () {
                $rootScope.$broadcast('cordovaLocalNotification:trigger', notification);
            });
        });
        console.log("init notification plugin");
        
    }
    //INIT FACEBOOK
    if(window.facebookConnectPlugin) {
        var FACEBOOK_APP="441815479314051";
        facebookConnectPlugin.browserInit(FACEBOOK_APP_ID);
    }

    

  });
})

.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
  .state('app', {
    url: "/app",
    abstract: true,
    templateUrl: "templates/menu.html",
    //controller: 'loginCtrl',
    data: {
      requireLogin: false
    }
  })

 .state('app.login', {
    url: "/login",
    views: {
      'menuContent': {
        templateUrl: "templates/login.html",
        controller: 'loginCtrl'
      }
    },
    data: {
      requireLogin: false
    }
  })

  .state('app.search', {
    url: "/search",
    views: {
      'menuContent': {
        templateUrl: "templates/search.html"
      }
    },
    data: {
      requireLogin: false
    }
  })
/*
  .state('app.deviceCheck', {
    url: "/deviceCheck",
    views: {
      'menuContent': {
        templateUrl: "templates/deviceCheck.html",
        controller: 'deviceCheckCtrl'
      }
    },
    data: {
      requireLogin: false
    }
  })*/
  .state('app.helpList',{
      url:'/helpList',
      views: {
        'menuContent':{
          templateUrl:"templates/helpList.html",
          controller:'helpListCtrl'
        }
      },
      data: {
        requireLogin: false
      }
    })
    .state('app.helpDetail', {
      url: "/helpList/:helpId",
      views: {
        'menuContent': {
            templateUrl: "templates/helpDetail.html",
           controller: 'helpDetailCtrl'
        }
      },
      data: {
        requireLogin: false
      }
    })

    .state('app.todoList', {
      url: "/todoList",
      views: {
        'menuContent': {
          templateUrl: "templates/todoList.html",
          controller: 'todoCtrl'
        }
      },
      data: {
        requireLogin: true
      }
    })

  .state('app.todoDetail', {
    url: "/todoDetail/:todoId",
    views: {
      'menuContent': {
          templateUrl: "templates/todoPage.html",
          controller: 'todoDetailCtrl'
      }
    },
    data: {
      requireLogin: true
    }
  });
  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/app/helpList');
});
