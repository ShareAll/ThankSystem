(function() {

angular.module('thank.services.goalUpdateService',[])
	.factory('goalUpdateService',['$http','$interval','$timeout','$q','$location','apiBase','$ionicPlatform',GoalUpdateService]);

function GoalUpdateService($http,$interval,$timeout,$q,$location,apiBase,$ionicPlatform) {
//mock data;
	
	var updates=[
		{'id':0,'name':'fenwang','content':'Start'},
		{'id':1,'name':'fenwang','content':'Complete the first phase'},
		{'id':2,'name':'edchen','content':'Congradulation!'},
		{'id':3,'name':'edchen','content':'Please check the link for more help as http://www.google.com'},
		{'id':4,'name':'zhuliu','content':'expect your next update'}
	];

	
	return {
		listen:listen,
		send:send
	};
	function listen(curId) {
		return $q(function(resolve,reject) {
			$timeout(function() {
				if(curId<updates.length) {
					console.info("resolve:"+curId+":"+updates[curId].content);
					resolve({'data':updates[curId]});
				} else {
					resolve({'data':null});
				}			
			},100);
		});
	}//end of listen

	function send(msg) {
		var newId=updates.length;
		updates.push(msg);
		//console.info("add "+newId);
		return $q(function(resolve,reject) {
			resolve({
				'data':{'status':'success'}
			});
		});
	} //end of send



} //End of GoalService




})();