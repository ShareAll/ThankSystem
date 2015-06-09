(function() {

angular.module('thank.controllers.settingCtrl', [])
    .controller('settingCtrl', ['$scope','$rootScope','$state','$stateParams','photoService','profileService','$mdDialog','friendRequestService','$ionicLoading','apiBase','$filter',SettingCtrl]);

function SettingCtrl($scope,$rootScope,$state,$stateParams,photoService,profileService,$mdDialog,friendRequestService,$ionicLoading,apiBase,$filter) {
    $scope.update=function(ev) {
        console.info("name="+$scope.setting.name);
        //console.log("src="+$scope.photo_src);

        var curUser=$rootScope.currentUser;
        console.info(curUser.emailAddress);
        profileService.update($scope.setting.name,$scope.photo_src)
        .then(function SUCCESS() {
            curUser.name=$scope.setting.name;
            //regenerate cache
            $rootScope.cacheId=new Date().getTime();
            $mdDialog.show(
                $mdDialog.alert()
                .parent(angular.element(document.body))
                .title('Saved')
                .content("Your profile change was done")
                .ariaLabel('Save Profile')
                .ok('Ok')
                .targetEvent(ev)
            );
        });
    }
    
    $scope.logout=function(ev) {
        $state.go("login");
    }

    $scope.getPhoto=function(isCamera) {
        if(window.navigator && navigator.camera) {
            $ionicLoading.show({
                template: 'Loading...'
            });
            var sourceType=navigator.camera.PictureSourceType.PHOTOLIBRARY;
            if(isCamera) {
                sourceType=navigator.camera.PictureSourceType.CAMERA;
            }
            navigator.camera.getPicture(onSuccess,onFail,{
                quality: 5,
                destinationType:Camera.DestinationType.DATA_URL,
                sourceType: sourceType,
                targetHeight: 315,
                targetWidth: 320
            });

            function onSuccess(imageData) {
                $scope.errMessage="attached";
                //var image=angular.element(document.getElementById('myPhoto'));
                $scope.photo_src="data:image/jpeg;base64," + imageData;
                // image.attr('src', "data:image/jpeg;base64," + imageData);
                $scope.$apply();   
                $ionicLoading.hide();
            }
            function onFail(message) {
                $scope.errMessage=message;
                $ionicLoading.hide();
            }
        } else {
            $scope.errMessage="Camera is NOT ready";
            console.info("error");
        }
    }//getPhoto
   
    $scope.setting={};

    $scope.$on('$ionicView.enter', function() {
        var currentUser=$rootScope.currentUser;
        $scope.setting.name=currentUser.name;
        var path=$filter('f_photo')(currentUser.emailAddress);
        
        $scope.photo_src=path;

    });

    

};//SettingCtrl

})();



