(function() {

angular.module('thank.controllers.friendCtrl', [])
    .controller('friendCtrl', ['$scope','$rootScope','$stateParams','deviceCheckService','$mdDialog','friendRequestService','apiBase',FriendCtrl]);

function FriendCtrl($scope,$rootScope,$stateParams,deviceCheckService,$mdDialog,friendRequestService,apiBase) {

    $scope.addFriend=function(ev,friend_email) {
        friendRequestService.create(friend_email).then(function() {
            $mdDialog.show(
                $mdDialog.alert()
                .parent(angular.element(document.body))
                .title('Sent')
                .content('Friend request to '+friend_email+" was sent")
                .ariaLabel('Send Friend Request')
                .ok('Ok')
                .targetEvent(ev)
            );
        });
    }//add Friend
    
    $scope.getFriendRequests=function() {
        friendRequestService.listByTarget().then(function(resp) {
            $scope.friend_requests=resp.data;
          
        });
    } //getFriendRequests

    $scope.getFriends=function() {
        friendRequestService.getFriends().then(function(resp) {
            $scope.friends=resp.data;
          
        });

    } //getFriends

    $scope.accept=function(requester) {
        friendRequestService.accept(requester).then(function() {
            $scope.getFriendRequests();    
            $scope.getFriends();
        });
    }

    $scope.refreshTab=function(isReqTab) {
        if(isReqTab) {
            $scope.getFriendRequests();  
        } else {
            $scope.getFriends();
        }
    
    };

    $scope.$on('$ionicView.enter', function() {
        $scope.friend_requests=[];
        $scope.friends=[];
        $scope.getFriendRequests();    
        $scope.getFriends();
    });

};//FriendCtrl

})();



