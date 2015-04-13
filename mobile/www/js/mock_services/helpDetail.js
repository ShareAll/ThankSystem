(function() {

angular.module('thank.services.helpDetailService',[])
	.factory('helpDetailService',['$http','$interval','$timeout','$q','$location','apiBase','$ionicPlatform',HelpDetailService]);

function HelpDetailService($http,$interval,$timeout,$q,$location,apiBase,$ionicPlatform) {
	//mock data;
	var messages=[
		{'date':'2014-10-10T20:02:39.082Z','username':'fenwang','text':'It is my first goal'},
		{'date':'2014-10-10T21:50:39.082Z','username':'chao','text':'Keep it up'},
		{'date':'2015-01-20T10:50:00.000Z','username':'fenwang','text':'I completed 10%'},
		{'date':'2014-03-10T21:50:39.082Z','username':'chao','text':'Great, keep it up'}
	]
	  
	
	return {
		getMessages:getMessages,
		send:send
	};
	function getMessages(goalId) {
		return $q(function(resolve,reject) {
			$timeout(function() {
				resolve({
					data:messages
				})			
			},100);
		});
	}//end of listen

	function send(msg) {
		messages.push(msg);
		if(Math.random()>0.5) {
			$timeout(function() {
				messages.push({
					'date':new Date(),
					'username':'chao',
					'text':'It is really great'
				});
			},10000);
			if(Math.random()>0.5) {
				messages.push({
					'date':new Date(),
					'username':'chao',
					'text':'Expecting your next update'
				});

			}
		}
		return $q(function(resolve,reject) {
			resolve({
				'data':messages
			});
		});
	} //end of send



} //End of GoalService




})();