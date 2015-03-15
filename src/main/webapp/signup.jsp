<html ng-app="thankApp">
<head>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
</head>
<body ng-controller="signupCtrl">
<h2>Signup</h2>
<form method="POST" ng-submit="submit()">
	<p ng-show="loginError"> {{loginError}}</p>
	<input type="text" ng-model="login.name" name="userName" placeholder="User Name">
	<input type="text" ng-model="login.emailAddress" name="emailAddress" placeholder="email Address">
	<input type="password" ng-model="login.password" name="password" placeholder="Password">
	<button type="submit">SignUp</button>
</form>
<script>
	angular.module('thankApp', [])
		.factory('authService',['$http',AuthService])
		.controller('signupCtrl', ['$scope','authService',SignupCtrl]);

	function AuthService($http) {
		return {
			signup:signup
		};
		function signup(loginModel) {
			return $http.post("/ThankWeb/rest/auth2/signup",loginModel);
		}
	}
	function SignupCtrl($scope,authService) {
		$scope.login={name:"",password:""};
		$scope.submit=function() {
			authService.signup($scope.login).then(SUCCESS,FAIL);
			function SUCCESS(resp) {
				window.location="index.jsp";
			}
			function FAIL(resp) {
				
				$scope.loginError=resp.data.errorMsg;
			}
		}
	}
</script>
</body>
</html>
