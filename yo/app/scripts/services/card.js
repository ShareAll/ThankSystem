define(['angular'], function(angular) {
'use strict';

angular.module('wftoolsApp.services.cardService',[])
	.factory('cardService',['$http','$q','apiBase',CardService]);

function CardService($http,$q,apiBase) {
	return {
		send:send
	};
	function send(card) {
		return $http.post(apiBase+'/card/send',{
			fromEmail:card.fromEmail,
			recipientEmail:card.recipientEmail,
			subject:card.subject,
			content:card.content,
			templateName:card.templateName
		});
	};
	
};//END AuthService

});
