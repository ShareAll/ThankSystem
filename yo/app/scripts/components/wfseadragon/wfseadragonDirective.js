define(['angular','openseadragon'], function (angular,openseadragon) {
  'use strict';

window.seadragon_Counter=1;
angular.module('wfseadragon.wfseadragonDirective',
  [])
.directive('wfseadragonDirective',['$window',WfseadragonDirective])
;

var context={
      svgMinHeight:450,
      svgMinWidth:320,
      model:null,
      hasCreationArea:true
};
function WfseadragonDirective($window) {
 //FUNCTION CONTENT END
  var $w = angular.element($window);
//  var style="height:"+$w.height()+";width:"+$w.width();
  var id="wfPhotoDiv_"+(window.seadragon_Counter++);
  var curViewer=null;
  return {
      replace:true,
      template:'<div id='+id+' ></div>',
      restrict:'EA',
      transclude:false,
      scope:{
        wfContext:'='
      },
      link: function($scope,elm,attrs) {
          //console.dir($scope.wfContext);
          $scope.$watch('wfContext.changed',openPhoto,true);
          var defaultContext={
            id:id,
            prefixUrl: "/bower_components/openseadragon/built-openseadragon/openseadragon/images/"
          };
              

          function openPhoto(newVal,oldVal) {
              console.dir($scope.wfContext);
              var newContext=angular.extend({},defaultContext,$scope.wfContext);
              console.dir(newContext);
              if(curViewer){
                curViewer.destroy();
              }
              curViewer=OpenSeadragon(newContext);
              
          };

         

          $w.bind("resize",function() {
              var elmWidth=$(elm).css("width");
              
              
          });

      }
   };


};
//END OF FUNC 




});
