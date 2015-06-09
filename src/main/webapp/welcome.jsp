<html ng-app="thankApp">
<head>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
</head>
<body ng-controller="loginCtrl">
<img src="rest/photo/signup"></img>
<h2>Welcome, please login:</h2>
<form method="POST" ng-submit="submit()">
	<p ng-show="loginError">{{loginError}}</p>
	<input ng-model="login.name" type="text" name="userName" placeholder="User Name">
	<input ng-model="login.password" type="password" name="password" placeholder="Password">
	<button type="submit">Login</button>
</form>
<a href="signup.jsp">to signup</a>


<script>
	
	angular.module('thankApp', [])
		.factory('authService',['$http',AuthService])
		.controller('loginCtrl', ['$scope','authService',LoginCtrl]);

	function AuthService($http) {
		return {
			login:login
		};
		function login(loginModel) {
			return $http.post("/ThankWeb/rest/auth2/login",loginModel);
		}
	}
	function LoginCtrl($scope,authService) {
		$scope.login={name:"",password:""};
		$scope.submit=function() {
			authService.login($scope.login).then(SUCCESS,FAIL);
			function SUCCESS(resp) {
				window.location="index.jsp";
			}
			function FAIL(resp) {
				console.dir(resp);
				$scope.loginError=resp.data.errorMsg;
			}
		}
	}
</script>
</body>
</html>
