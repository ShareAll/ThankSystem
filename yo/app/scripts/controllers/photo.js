define(['angular','openseadragon'], function (angular,openseadragon) {
  'use strict';

  
angular.module('wftoolsApp.controllers.photoCtrl', [])
    .controller('PhotoCtrl',['$scope', PhotoCtrl]);
function PhotoCtrl($scope) {
	var curViewer=null;
	$scope.wfPhotoContext={
		changed:1,
		//debugMode:  true,
		tileSources: "/photos/sample1/dzc_output.xml",
		overlays: [{
			            id: 'example-overlay',
			            x: 0.2, 
			            y: 0.2, 
			            width: 0.2, 
			            height: 0.2,
			            className: 'highlight'
			    }]
	};
	$scope.curPic=0;
	$scope.togglePicture=function() {
		if($scope.curPic==0) {
			$scope.wfPhotoContext={
				changed:1-$scope.wfPhotoContext.changed,
				//debugMode:  true,
				tileSources: "/photos/sample2/dzc_output.xml"
				

			};
		} else {
			$scope.wfPhotoContext={
				changed:1-$scope.wfPhotoContext.changed,
				//debugMode:  true,
				tileSources: "/photos/sample1/dzc_output.xml"
			};

		}
		$scope.curPic=1-$scope.curPic;
	};
   /* var viewer = OpenSeadragon({
        id: "openseadragon1",
        prefixUrl: "/bower_components/openseadragon/built-openseadragon/openseadragon/images/",
        tileSources: "/photos/sample/dzc_output.xml"
    });*/

};


///END 
});
