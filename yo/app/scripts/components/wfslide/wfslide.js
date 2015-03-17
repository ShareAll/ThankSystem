define(['angular','jssor-slider'],function(angular) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wfslide',[])
.directive('wfSlide',[WfSlide]);
      

function WfSlide() {
      return {
            restrict:'EA',
            link: function($scope,elm,attrs) {
                  var id=attrs['id'];
                  if(!id) throw "must need set id for wf-slide";
                  console.info(id);
                  new $JssorSlider$(id, {
                        $AutoPlay: true,
                        $DragOrientation: 1    
                  });
            }
  	};
};


});