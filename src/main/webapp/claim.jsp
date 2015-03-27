<%@ page import="com.thank.common.model.ClaimableTask" %>
<%@ page import="com.thank.common.dao.ClaimableTaskUtil" %>

<%
	String claimId=request.getParameter("claimId");
	String emailAddress=request.getParameter("emailAddress");
	int status=ClaimableTaskUtil.autoClaim(request,claimId, emailAddress);
	if(claimId==null) claimId="";
	if(emailAddress==null) emailAddress="";
	
%>

<html ng-app="thankApp">
<head>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
</head>
<body ng-controller="claimCtrl">
<h2>Claim</h2>
<div style="display:none" id="claimStatus"><%=status%></div>
<div style="display:none" id="claimId"><%=claimId%></div>
<div style="display:none" id="emailAddress"><%=emailAddress%></div>

<p ng-if="claimStatus=='-1'">ClaimId is NO Longer valid</p>

<div ng-if="claimStatus=='-2'">
	<form method="POST" ng-submit="submit()">
		<p ng-if="lastError">{{lastError}}</p>
		<input type="hidden" ng-model="claim.claimId">
		<input type="hidden" ng-model="claim.emailAddress">
		<p>You are NOT registered, please set your password and auto login</p>
		<input type="password" ng-model="claim.password" name="password" placeholder="Password">
		<button type="submit">SignUp</button>
	</form>

</div>
<script>
	angular.module('thankApp', [])
		.factory('authService',['$http',AuthService])
		.controller('claimCtrl', ['$scope','authService',ClaimCtrl]);

	function AuthService($http) {
		return {
			signup:signup
		};
		function signup(signUpModel) {
			return $http.post("/ThankWeb/rest/auth2/claimSignUp",signUpModel);
		}
	}
	function ClaimCtrl($scope,authService) {
		$scope.claimStatus=document.getElementById("claimStatus").innerText;
	
		if($scope.claimStatus==0) {
			window.location="index.jsp";
		} 
		$scope.claim={password:""};
		$scope.claim.claimId=document.getElementById("claimId").innerText;
		$scope.claim.emailAddress=document.getElementById("emailAddress").innerText;
		
		$scope.submit=function() {
			authService.signup($scope.claim).then(SUCCESS,FAIL);
			function SUCCESS(resp) {
				window.location="index.jsp";
			}
			function FAIL(resp) {
				$scope.lastError=resp.data.errorMsg;
			}
			
		}
	}
</script>
</body>
</html>
