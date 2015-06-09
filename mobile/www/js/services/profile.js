(function() {


angular.module('thank.services.profileService',[])
    .factory('profileService',['$http','$timeout','$q','$location','apiBase','$ionicPlatform','$rootScope',
        '$ionicHistory','$state',ProfileService]);

function ProfileService($http,$timeout,$q,$location,apiBase,$ionicPlatform,$rootScope,$ionicHistory,$state) {

    return {
        update:update,
        getCurrent:getCurrent
    };
  
    function update(name,photoInBase64) {

        var curUser=$rootScope.currentUser;
        var payload={
            name:name,
            emailAddress:curUser.emailAddress,
            photoInBase64: photoInBase64
        };
        return $http.post(apiBase+"/profile/update",payload);
    }
    function getCurrent() {
        var curUser=$rootScope.currentUser;
        if(curUser) {
            return $http.get(apiBase+"/profile/getCurrentProfile?user="+curUser.emailAddress);
        }
    }



} //End of ProfileService




})();