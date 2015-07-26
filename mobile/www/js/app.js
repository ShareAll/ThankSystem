angular.module('jikarma', [
    'ionic','ngAria','ngMaterial','ngMessages',
    'thank.common',
    'thank.controllers','thank.services'
])
.constant('apiBase',"http://52.11.234.40:8080/ThankWeb/rest")
//.constant('apiBase',"http://127.0.0.1:8080/ThankWeb/rest")
.run(function($ionicPlatform,$ionicNavBarDelegate,$timeout,$interval,$rootScope,$ionicHistory,$state,loginService,profileService) {
    $rootScope.default_page="tab.helpMe";
    $rootScope.cacheId=new Date().getTime();
    $rootScope.helpTrack={};
    //check login state
    $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
       // var requireLogin = toState.data.requireLogin;
       /* if (toState.name.startsWith("tab") && typeof $rootScope.currentUser === 'undefined') {
            event.preventDefault();
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go('login');
        }*/
    });
    $rootScope.$on('$stateChangeSuccess', function() {

            //if(toState.name.indexOf('main') !== -1) {
                $ionicNavBarDelegate.showBackButton(true);
            //}

    });
    
/*
    $rootScope.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
        $rootScope.previousState = from.name;
        $rootScope.currentState = to.name;
        console.log('Previous state:'+$rootScope.previousState)
        console.log('Current state:'+$rootScope.currentState)
    });
*/
    $ionicPlatform.ready(function() {
        // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
        // for form inputs)
        if (window.cordova && window.cordova.plugins.Keyboard) {
            cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
            cordova.plugins.Keyboard.disableScroll(true);
        }
        if (window.StatusBar) {
            // org.apache.cordova.statusbar required
            StatusBar.styleLightContent();
            //StatusBar.styleDefault();
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
                $state.go('app.helpList', {}, {location:'replace'});
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
    }); //$ionicPlatform.ready
}) //run

.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider
    .state('tab', {
        url: "/tab",
        abstract: true,
        templateUrl: "templates/tabs.html"
    })
        .state('tab.blog',{
            url: '/blog',
            views: {
                'tab_blog':{
                    templateUrl:'templates/tab_blog.html',
                    controller:'blogCtrl'
                }
            }
        })
        .state('tab.helpMe', {
            url: '/helpMe',
            views: {
                'tab_helpMe': {
                    templateUrl: 'templates/tab_helpMe.html',
                    controller: 'helpListCtrl'
                }
            }
        })
        .state('tab.helpOthers', {
            url: '/helpOthers',
            views: {
                'tab_helpOthers': {
                    templateUrl: 'templates/tab_helpOthers.html',
                    controller: 'helpListCtrl'
                }
            }
        })
        .state('tab.friends', {
            url: "/friends",
            views: {
                'tab_friends': {
                    templateUrl: "templates/tab_friends.html",
                    controller: 'friendCtrl'
                }
            }
        })
        .state('tab.settings', {
            url: "/settings",
            views: {
                'tab_settings': {
                    templateUrl: "templates/tab_settings.html",
                    controller: 'settingCtrl'
                }
            }
        })
    .state('helpDetail', {
        url: "/helpList/:helpId",    
        templateUrl: "templates/helpDetail.html",
        controller: 'helpDetailCtrl'
    }) 
    .state('login', {
        url: '/login',
        templateUrl: 'templates/login.html',
        controller: 'loginCtrl'
    })
    .state('signup', {
        url:'/signup',
        templateUrl: 'templates/signup.html',
        controller: 'signupCtrl'
    })
   
    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise('login');

}) //configure







