(function() {


angular.module('thank.services.cardService',[])
	.factory('cardService',['$http','$rootScope','$timeout','$q','$location','apiBase','$ionicPlatform',
		'$ionicHistory','$state',CardService]);

function CardService($http,$rootScope,$timeout,$q,$location,apiBase,$ionicPlatform,$ionicHistory,$state) {
	return {
		send:send
	};
	function send(card) {
		var curUser=$rootScope.currentUser;
		var payload={
			fromEmail:curUser.emailAddress,
			recipientEmail:card.recipientEmail,
			subject: "Thank You from "+curUser.name,
			content: card.content,
			templateName:card.templateName
		};
		return $http.post(apiBase+"/card/send",payload);
	}//send(card)

	


} //End of CardService




})();