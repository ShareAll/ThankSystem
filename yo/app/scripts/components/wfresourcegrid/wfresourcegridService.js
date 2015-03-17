define(['angular'], function(angular) {
'use strict';

angular.module('wfresourcegrid.wfResourcegridService',[])
	.factory('wfResourcegridService',['$http','$q',WfResourcegridService]);

function WfResourcegridService($http,$q) {
	function getData(tenentId) {
		return $q(function(resolve,reject) {
			setTimeout(function() {
				resolve([{"id":"1","name":"instance 1","type":"Middle","category":"OnDemand","value":10,"cloud":"aws"},
				      	{"id":"2","name":"instance 2","type":"Middle","category":"OnDemand","value":5,"cloud":"aws"},
				      	{"id":"3","name":"instance 3","type":"Middle","category":"OnDemand","value":20,"cloud":"aws"},
				      	{"id":"4","name":"instance 4","type":"Middle","category":"OnDemand","value":70,"cloud":"aws"},
				      	{"id":"5","name":"instance 5","type":"Middle","category":"OnDemand","value":69,"cloud":"aws"},
				      	{"id":"6","name":"instance 6","type":"Middle","category":"OnDemand","value":81,"cloud":"aws"},
				      	{"id":"7","name":"instance 7","type":"Large","category":"OnDemand","value":73,"cloud":"aws"},
				      	{"id":"8","name":"instance 8","type":"Large","category":"OnDemand","value":10,"cloud":"aws"},
				      	{"id":"9","name":"instance 9","type":"Large","category":"OnDemand","value":5,"cloud":"aws"}]
				      	);
			},1000);
		});
	};
	function getDetail(resourceId) {
		return $q(function(resolve,reject) {
			setTimeout(function() {
				var ret={"name":"demo","type":"tenant"};
				resolve(ret);
			},100);
		});
	};
	function getSteps() {
		return $q(function(resolve,reject) {
			resolve(SAMPLE_STEP_DATA);
		});
	}

	return {
		getData:getData,
		getDetail:getDetail,
		getSteps:getSteps
	};
}

});


var SAMPLE_STEP_DATA=[
    {
        "name": "0.Begin",
        "notes": "Initial state before optimization",
        "snapshot":[
        	{"id":"1","name":"instance 1","type":"Middle","category":"OnDemand","value":10,"cloud":"aws"},
	      	{"id":"2","name":"instance 2","type":"Middle","category":"OnDemand","value":5,"cloud":"aws"},
	      	{"id":"3","name":"instance 3","type":"Middle","category":"OnDemand","value":20,"cloud":"aws"},
	      	{"id":"4","name":"instance 4","type":"Middle","category":"OnDemand","value":70,"cloud":"aws"},
	      	{"id":"5","name":"instance 5","type":"Middle","category":"OnDemand","value":69,"cloud":"aws"},
	      	{"id":"6","name":"instance 6","type":"Middle","category":"OnDemand","value":81,"cloud":"aws"},
	      	{"id":"7","name":"instance 7","type":"Large","category":"OnDemand","value":73,"cloud":"aws"},
	      	{"id":"8","name":"instance 8","type":"Large","category":"OnDemand","value":10,"cloud":"aws"},
	      	{"id":"9","name":"instance 9","type":"Large","category":"OnDemand","value":5,"cloud":"aws"}
        ]
    },
    {
        "name": "1.Process Centralization for PROD env",
        "notes": "Merge the idle processes together with minimal computes",
        "snapshot":[
        	{"id":"1","name":"instance 1","type":"Middle","category":"OnDemand","value":35,"cloud":"aws"},
	      	{"id":"2","name":"instance 2","type":"Middle","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"3","name":"instance 3","type":"Middle","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"4","name":"instance 4","type":"Middle","category":"OnDemand","value":70,"cloud":"aws"},
	      	{"id":"5","name":"instance 5","type":"Middle","category":"OnDemand","value":69,"cloud":"aws"},
	      	{"id":"6","name":"instance 6","type":"Middle","category":"OnDemand","value":81,"cloud":"aws"},
	      	{"id":"7","name":"instance 7","type":"Large","category":"OnDemand","value":88,"cloud":"aws"},
	      	{"id":"8","name":"instance 8","type":"Large","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"9","name":"instance 9","type":"Large","category":"OnDemand","value":0,"cloud":"aws"}

        ]
    },
    {
        "name": "2.Compute Type Change",
        "notes": "Change compute type",
        "snapshot":[
        	{"id":"1","name":"instance 1","type":"Middle","category":"OnDemand","value":35,"cloud":"aws"},
	      	{"id":"2","name":"instance 2","type":"Middle","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"3","name":"instance 3","type":"Large","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"4","name":"instance 4","type":"Middle","category":"OnDemand","value":70,"cloud":"aws"},
	      	{"id":"5","name":"instance 5","type":"Middle","category":"OnDemand","value":69,"cloud":"aws"},
	      	{"id":"6","name":"instance 6","type":"Middle","category":"OnDemand","value":81,"cloud":"aws"},
	      	{"id":"7","name":"instance 7","type":"Large","category":"OnDemand","value":88,"cloud":"aws"},
	      	{"id":"8","name":"instance 8","type":"Large","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"9","name":"instance 9","type":"Large","category":"OnDemand","value":0,"cloud":"aws"}

        ]
    },

    {
        "name": "2.PROD cost effective",
        "notes": "Based on AWS cost model, analysis reserved/ondemand",
        "snapshot":[
        	{"id":"1","name":"instance 1","type":"Middle","category":"OnDemand","value":35,"cloud":"aws"},
	      	{"id":"2","name":"instance 2","type":"Middle","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"3","name":"instance 3","type":"Large","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"4","name":"instance 4","type":"Middle","category":"Reserved","value":70,"cloud":"aws"},
	      	{"id":"5","name":"instance 5","type":"Middle","category":"Reserved","value":69,"cloud":"aws"},
	      	{"id":"6","name":"instance 6","type":"Middle","category":"Reserved","value":81,"cloud":"aws"},
	      	{"id":"7","name":"instance 7","type":"Large","category":"Reserved","value":88,"cloud":"aws"},
	      	{"id":"8","name":"instance 8","type":"Large","category":"OnDemand","value":0,"cloud":"aws"},
	      	{"id":"9","name":"instance 9","type":"Large","category":"OnDemand","value":0,"cloud":"aws"}

        ]
    }

];