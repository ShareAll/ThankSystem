define(['angular'],function(angular) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wffix',[])
.directive('myTransclude',MyTransclude);

function MyTransclude() {
    return {
        restrict:'EAC',
        compile: function(tElement, tAttrs, transclude) {
            return function(scope, iElement, iAttrs) {
                transclude(scope.$new(), function(clone) {
                    iElement.append(clone);
                });
            };
        }
    };
};

})