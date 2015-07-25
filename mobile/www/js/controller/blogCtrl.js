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

        $scope.openCreateModal=function() {
            console.info("try to open");
            modelScope=$scope.modal_create_blog.scope;
            modelScope.init();
            $scope.modal_create_blog.show();
        }


        //------Modals-------
        //modal for create blog
       $ionicModal.fromTemplateUrl('create_blog.html', {
            scope:null,
            animation: 'slide-in-up'
        }).then(function(modal) {

            // invite more friend to help
            $scope.modal_create_blog = modal;
            modal.scope.init=function(){
                 var modalScope=modal.scope;
                 modelScope.data={
                    "title":""
                 };
                
            };
              
            modal.scope.submit=function() {
                var modalScope=modal.scope;
                $ionicLoading.show({template: 'Loading...'});
                blogService.createArticle(modalScope.data.title).then(function SUCCESS(resp){
                    $scope.articles=resp.data;
                }).finally(function() {
                    $ionicLoading.hide();
                    modal.hide();
                });
            }   
            modal.scope.close=function() {
                modal.hide(); 
            }
            
        }); //modal for close help

  
    }//BlogCtrl

})();



