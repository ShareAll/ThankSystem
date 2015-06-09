(function() {


angular.module('thank.services.photoService',[])
    .factory('photoService',['$http','$timeout','$q','$location','apiBase','$ionicPlatform',
        '$ionicHistory','$state',PhotoService]);

function PhotoService($http,$timeout,$q,$location,apiBase,$ionicPlatform,$ionicHistory,$state) {

    return {
        getPhoto:getPhoto
    };
  
    function getPhoto() {
        console.log("GET PHOTO");
        return $q(function(resolve,reject) {
            $ionicPlatform.ready(function() {
                var ret=[];
               
                if(window.navigator && window.navigator.camera) {     
                    navigator.camera.getPicture(function SUCCESS(imageURI) {
                        console.log("GET PHOTO!!");
                        var options = new FileUploadOptions();
                        options.fileKey="file";
                        options.fileName=imageURI.substr(imageURI.lastIndexOf('/')+1);
                        options.mimeType="image/jpeg";
 
                        var params = new Object();
                        params.value1 = "test";
                        params.value2 = "param";
 
                        options.params = params;
                        options.chunkedMode = false;

                        console.log(JSON.stringify(options));
                        /*var ft = new FileTransfer();
                        ft.upload(imageURI, "http://localhost:8080/", win, fail, options);
                    */
                    }, function FAIL(message) {
                        console.log("NO PHOTO:"+message);
                            
                    },{
                            quality: 50, 
                            destinationType: navigator.camera.DestinationType.FILE_URI,
                            sourceType: navigator.camera.PictureSourceType.PHOTOLIBRARY
                    });
                }
                resolve({
                    data:ret
                });
            
            });
        });
    }
    



} //End of PhotoService




})();