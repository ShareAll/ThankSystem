(function() {

angular.module('thank.services.helpDetailService',[])
	.factory('helpDetailService',['$http','$interval','$timeout','$q','$location','apiBase','$ionicPlatform',HelpDetailService]);

function HelpDetailService($http,$interval,$timeout,$q,$location,apiBase,$ionicPlatform) {
	
	return {
		listComment:listComment,
		sendComment:sendComment
	};
	function listComment(owner,helpId,user,lastCommentId) {
		
		return $http.get(apiBase+"/help/listComment",{
			params: {
				owner:owner,
            	user:user,
            	helpId: helpId,
            	lastCommentId:lastCommentId,

        	}
        });
		
	}//end of listComment

	function sendComment(helpId,content,user,userName) {
		//console.info("add "+newId);
		var payload={
			helpId:helpId,
			content:content,
			owner:user,
			ownerName:userName
			
		};
		return $http.post(apiBase+"/help/createComment?user="+user,payload);
	} //end of sendComment



} //End of GoalService




})();