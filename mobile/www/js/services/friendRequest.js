(function() {

angular.module('thank.services.friendRequestService',[])
    .factory('friendRequestService',['$http','$timeout','$q','$location','$rootScope','apiBase','$ionicPlatform',FriendRequestService]);

function FriendRequestService($http,$timeout,$q,$location,$rootScope,apiBase,$ionicPlatform) {

    return {
        getFriends:getFriends,
        listByTarget:listByTarget,
        listByRequester:listByRequester,
        create:create,
        accept:accept
    };
    
    function listByTarget() {
        var curUser=$rootScope.currentUser;
        return $http.get(apiBase+"/friend/listByTarget?user="+curUser.emailAddress);
    }
    
    function listByRequester() {
        var curUser=$rootScope.currentUser;
        return $http.get(apiBase+"/friend/listByRequester?user="+curUser.emailAddress);
    }
    function getFriends() {
        var curUser=$rootScope.currentUser;
        return $http.get(apiBase+"/friend/list?user="+curUser.emailAddress);
    }
    function create(target) {
        var curUser=$rootScope.currentUser;
        var payload={
            "requester":curUser.emailAddress,
            "target":target
        };
        return $http.post(apiBase+"/friend/create?user="+curUser.emailAddress,payload);   
    }

    function accept(requester) {
        var curUser=$rootScope.currentUser;
        var payload={
            "requester":requester,
            "target":curUser.emailAddress
        };
        return $http.post(apiBase+"/friend/accept?user="+curUser.emailAddress,payload); 
    }
   


} //End of FriendRequestService




})();