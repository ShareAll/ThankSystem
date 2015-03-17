var tests = [];
for (var file in window.__karma__.files) {
  if (window.__karma__.files.hasOwnProperty(file)) {
    // Removed "Spec" naming from files
    if (/Spec\.js$/.test(file)) {
      tests.push(file);
    }
  }
}

requirejs.config({
    // Karma serves files from '/base'
    baseUrl: '/base/app/scripts',

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
        'angular' : {'exports' : 'angular'},
        'angular-route': ['angular'],
        'angular-cookies': ['angular'],
        'angular-sanitize': ['angular'],
        'angular-resource': ['angular'],
        'angular-animate': ['angular'],
        'angular-touch': ['angular'],
        'angular-mocks': {
          deps:['angular'],
          'exports':'angular.mock'
        }
    },

    // ask Require.js to load these files (all our tests)
    deps: tests,

    // start test run, once Require.js is done
    callback: window.__karma__.start
});
