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
		console.dir(payload);
		return $q(function(resolve,reject) {
			resolve({
				data:{"status":"success"}
			});
		});
	}//send(card)

	


} //End of CardService




})();