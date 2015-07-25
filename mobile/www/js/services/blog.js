(function() {


angular.module('thank.services.blogService',[])
    .factory('blogService',['$http','$timeout','$q','$rootScope','$location','apiBase','$ionicPlatform',
        '$ionicHistory','$state',BlogService]);

function BlogService($http,$timeout,$q,$rootScope,$location,apiBase,$ionicPlatform,$ionicHistory,$state) {
    return {
        createArticle:createArticle,
        getArticles:getArticles
    };
    function getArticles() {
        return $http.get(apiBase+"/blog/list");
    }
    function createArticle(title) {
        var curUser=$rootScope.currentUser;
        var payload={
            title:title
        };
        return $http.post(apiBase+"/blog/create?user="+curUser.emailAddress,payload);
    }
    


} //End of DeviceCheckService




})();