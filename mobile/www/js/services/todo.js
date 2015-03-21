angular.module('thank.services',[])
	.factory('todoService',['$http','$timeout','$q','apiBase',

function TodoService($http,$timeout,$q,apiBase) {
	return {
		list:list,
		detail:detail
	};

	function list() {
		return $q(function(resolve,reject) {
			$timeout(function() {
				var data=[
					{'title':'Send thank','id':1},
					{'title':'Send thank','id':2},
					{'title':'Send thank','id':3}
				];
				resolve({
					data:data
				});
			},1000);
		});
	}; //function list

	function detail(id) {
		return $q(function(resolve,reject) {
			$timeout(function() {
				var data={'title':'Send thank '+id,'id':id};
				resolve({
					data:data
				})
				resolve({
					data:data
				});
			},1000);
		});

	}; //function detail


} //End of TodoService


]);
