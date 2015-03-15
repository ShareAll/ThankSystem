<html ng-app="thankApp">
<head>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
</head>
<body ng-controller="mainCtrl">
<div ng-if="auth"> 
	<h2>Welcome, {{auth.name}}</h2>
	<form action="rest/auth/logout" method="post">
		<button type="submit">Logout</button>
	</form>

</div>
<script>
angular.module('thankApp', [])
	.factory('authService',['$http',AuthService])
	.controller('mainCtrl', ['$scope','authService',MainCtrl]);

function AuthService($http) {
	return {
		getContext:getContext
	};
	function getContext() {
		return $http.get("/ThankWeb/rest/auth2");
	}
}


function MainCtrl($scope,authService) {
	$scope.auth=null;
	authService.getContext().then(function SUCCESS(resp) {
		
		$scope.auth=resp.data;
	},function FAIL(resp) {
		window.location="welcome.jsp";
	});
}

</script>
</body>
</html>
