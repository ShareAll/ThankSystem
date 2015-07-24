(function() {
    angular.module('thank.controllers.blogCtrl', [])
        .controller('blogCtrl', ['$q','$rootScope','$scope','$timeout','$state','$stateParams','blogService','$ionicModal','$ionicLoading',BlogCtrl]);

    function BlogCtrl($q,$rootScope,$scope,$timeout,$state,$stateParams,blogService,$ionicModal,$ionicLoading) {
        $scope.$on('$ionicView.enter', function() {
            $ionicLoading.show({
              template: 'Loading...'
            });
            blogService.getArticles().then(function SUCCESS(resp) {
                $scope.articles=resp.data;
            }).finally(function(){
                $ionicLoading.hide();
            });
        });



  
    }//BlogCtrl

})();



