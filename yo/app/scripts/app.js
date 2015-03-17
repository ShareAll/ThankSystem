/*jshint unused: vars */
define(['angular', 'controllers/main','controllers/login','controllers/about','controllers/grid','controllers/canvas','controllers/photo','controllers/datatable',
  'services/auth',
  'components/wffix/wffix',
  'components/wfresourcegrid/wfresourcegrid','components/wfd3tree/wfd3tree',
  'components/wfloadmask/wfloadmask',
  'components/wfauth/wfauth',
  'components/wfscrollcontainer/wfscrollcontainer',
  'components/wfdatatable/wfdatatable',
  'components/wfcanvas/wfcanvas',
  'components/wfseadragon/wfseadragon',
  'angular-google-maps',
  'bootstrap',
  'angular-facebook',
  'jssor-slider','components/wfslide/wfslide',
  'controllers/index'
  ]/*deps*/, 
function (angular, MainCtrl, AboutCtrl,Grid2Ctrl,wfresourcegrid,wfd3tree)/*invoke*/ {
  'use strict';
  return angular
    .module('wftoolsApp', [
      'wftoolsApp.controllers.MainCtrl',
      'wftoolsApp.controllers.LoginCtrl',
      'wftoolsApp.controllers.AboutCtrl',
      'wftoolsApp.controllers.GridCtrl',
      'wftoolsApp.controllers.datatableCtrl',
      'wftoolsApp.controllers.canvasCtrl',
      'wftoolsApp.controllers.photoCtrl',
      'wftoolsApp.services.authService',
      'wfresourcegrid',
      'wfd3tree',
      'wfcanvas',
      'wfscrollcontainer',
      'wfseadragon',
      'wfloadmask',
      'wfdatatable',
      'uiGmapgoogle-maps',
/*angJSDeps*/
    'ngCookies',
    'ngAria',
    'ngMaterial',
    'ngMessages',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngAnimate',
    'ngTouch',
    'facebook',
    'wfslide',
    'wftoolsApp.controllers.IndexCtrl'
  ])
    .constant('apiBase',window.THANKWEB_ENV['apiBase'])
    //.constant('apiBase','http://localhost:8080/ThankWeb/rest')
    .config(function ($routeProvider) {
      $routeProvider
        .when('/', {
          templateUrl: 'views/main.html',
          controller: 'MainCtrl'
        })

        .when('/resourcegrid',{
          templateUrl:'views/resourcegrid.html',
          controller:'MainCtrl'
        })
        .when('/d3tree',{
          templateUrl:'views/d3tree.html',
          controller:'MainCtrl'
        })
        .when('/grid',{
          templateUrl:'views/grid.html',
          controller:'GridCtrl'
        })
        .when('/canvas',{
          templateUrl:'views/canvas.html',
          controller:'CanvasCtrl'
        })
        .when('/photo',{
          templateUrl:'views/photo.html',
          controller:'PhotoCtrl'
        })
        .when('/about', {
          templateUrl: 'views/about.html',
          controller: 'AboutCtrl'
        })
        .when('/datatables',{
          templateUrl:'views/datatables.html',
          controller:'DatatableCtrl'
        }) 
        .when('/map',{
          templateUrl:'views/map.html',
          controller:'MainCtrl'
        }) 

        .otherwise({
          redirectTo: '/'
        });
    });
});
