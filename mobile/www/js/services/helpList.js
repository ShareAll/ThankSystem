(function() {

angular.module('thank.services.helpListService',[])
	.factory('helpListService',['$http','$timeout','$q','$location','$rootScope','apiBase','$ionicPlatform',HelpListService]);

function HelpListService($http,$timeout,$q,$location,$rootScope,apiBase,$ionicPlatform) {
//mock data;

	return {
		listAllCategories:listAllCategories,
		add:add,
		update:update,
		listBySubscriber:listBySubscriber,
		list:list,
		updateProgress:updateProgress,
		updateInvitation:updateInvitation,
		completeHelp:completeHelp

	};
	function listAllCategories() {
		return $http.get(apiBase+"/category/list");
	}

	function add(goalData) {
		
		var curUser=$rootScope.currentUser;
		var payload={
			"title": goalData.title,
			"privacy":goalData.privacy,
			"categoryId":goalData.categoryId,
  			"subscribers": goalData.subscribers
		};
		return $http.post(apiBase+"/help/createHelp?user="+curUser.emailAddress,payload);	
	}	
	function update(helpId,goalData) {	
		var curUser=$rootScope.currentUser;
		var payload={
			"id": helpId,
			"title": goalData.title,
			"privacy":goalData.privacy,
			"categoryId":goalData.categoryId,
  			"subscribers": goalData.subscribers
		};
		console.dir(payload);
		return $http.post(apiBase+"/help/updateHelp?user="+curUser.emailAddress,payload);	
	}	
	function listBySubscriber() {
		var curUser=$rootScope.currentUser;
		return $http.get(apiBase+"/help/listBySubscriber?user="+curUser.emailAddress);

	}
	function list() {
		var curUser=$rootScope.currentUser;
		return $http.get(apiBase+"/help/list?user="+curUser.emailAddress);
		
	}//end of list

	function updateProgress(helpId,incr) {
		var curUser=$rootScope.currentUser;

		var payload= {
			id:helpId,
			completeness:incr
		};
		return $http.post(apiBase+"/help/updateHelpProgress?user="+curUser.emailAddress+"&name="+curUser.name,payload);
		
	}
	function updateInvitation(helpId,subscribers) {
		var curUser=$rootScope.currentUser;
		var payload={
			id:helpId,
			subscribers:subscribers
		};
		return $http.post(apiBase+"/help/updateHelpInvitation?user="+curUser.emailAddress,payload);

	}

	function completeHelp(helpId,conclusion) {
		var curUser=$rootScope.currentUser;
		var payload={
			id:helpId,
			conclusion:conclusion
		};
		return $http.post(apiBase+"/help/completeHelp?user="+curUser.emailAddress,payload);
	}

} //End of GoalService




})();