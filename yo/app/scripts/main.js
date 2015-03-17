/*jshint unused: vars */
require.config({
  paths: {
    wf_reference: 'reference',
    jquery: '../../bower_components/jquery/dist/jquery',
    datatables: '../../bower_components/datatables/media/js/jquery.dataTables',
    angular: '../../bower_components/angular/angular',
    'angular-animate': '../../bower_components/angular-animate/angular-animate',
    'angular-aria': '../../bower_components/angular-aria/angular-aria',
    'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
    'angular-messages': '../../bower_components/angular-messages/angular-messages',
    'angular-resource': '../../bower_components/angular-resource/angular-resource',
    'angular-route': '../../bower_components/angular-route/angular-route',
    'angular-sanitize': '../../bower_components/angular-sanitize/angular-sanitize',
    'angular-touch': '../../bower_components/angular-touch/angular-touch',
    'angular-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'angular-mocks': '../../bower_components/angular-mocks/angular-mocks',
    'angular-scenario': '../../bower_components/angular-scenario/angular-scenario',
    'angular-material': '../../bower_components/angular-material/angular-material',
    tooltipster: '../../bower_components/tooltipster/js/jquery.tooltipster.min',
    d3: '../../bower_components/d3/d3',
    'ui-grid': '../../bower_components/angular-ui-grid/ui-grid',
    'angular-ui-grid': '../../bower_components/angular-ui-grid/ui-grid',
    bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
    select2: '../../bower_components/select2/select2',
    hammerjs: '../../bower_components/hammerjs/hammer',
    'datatables-bootstrap3': '../../bower_components/datatables-bootstrap3/BS3/assets/js/datatables',
    openseadragon: '../../bower_components/openseadragon/built-openseadragon/openseadragon/openseadragon.min',
    'angular-google-maps': '../../bower_components/angular-google-maps/dist/angular-google-maps',
    underscore: '../../bower_components/underscore/underscore',
    'angular-facebook': '../../bower_components/angular-facebook/lib/angular-facebook',
    'jssor-slider': '../../bower_components/jssor-slider/js/jssor.slider.mini'
  },
  shim: {
    wf_reference: {
      exports: 'wf_reference'
    },
    hammerjs: {
      exports: 'Hammer'
    },
    jquery: {
      exports: 'jquery'
    },
    bootstrap: [
      'jquery'
    ],
    datatables: [
      'jquery'
    ],
    d3: {
      exports: 'd3'
    },
    tooltipster: [
      'jquery'
    ],
    angular: {
      deps: [
        'jquery'
      ],
      exports: 'angular'
    },
    'angular-material': {
      deps: [
        'hammerjs',
        'angular',
        'angular-animate',
        'angular-aria'
      ],
      exports: 'ngMaterial'
    },
    'angular-animate': [
      'angular'
    ],
    'angular-aria': [
      'angular'
    ],
    'angular-cookies': [
      'angular'
    ],
    'angular-messages': [
      'angular'
    ],
    'angular-resource': [
      'angular'
    ],
    'angular-route': [
      'angular'
    ],
    'angular-sanitize': [
      'angular'
    ],
    'angular-touch': [
      'angular'
    ],
    'angular-bootstrap': [
      'angular'
    ],
    'angular-mocks': {
      deps: [
        'angular'
      ],
      exports: 'angular.mock'
    },
    'ui-grid': [
      'angular'
    ],
    'angular-google-maps': [
      'angular',
      'underscore'
    ],
    'angular-facebook': [
      'angular'
    ]
  },
  priority: [
    'angular'
  ],
  packages: [

  ]
});

//http://code.angularjs.org/1.2.1/docs/guide/bootstrap#overview_deferred-bootstrap
window.name = 'NG_DEFER_BOOTSTRAP!';


require([
  'jquery',
  'angular',
  'app',
  'hammerjs',
  'tooltipster',
  'd3',
  'ui-grid',
  'angular-material',
  'angular-route',
  'angular-cookies',
  'angular-sanitize',
  'angular-resource',
  'angular-animate',
  'angular-touch',
  'angular-aria',
  'angular-messages'
], function($,angular,app,hammer) {
  'use strict';
  // Ferry: fix hammerjs issue exports issue in requireJs
  window.Hammer=hammer;  
  /* jshint ignore:start */
  var $html = angular.element(document.getElementsByTagName('html')[0]);
  /* jshint ignore:end */
  angular.element().ready(function() {
    angular.resumeBootstrap([app.name]);
  });
});
