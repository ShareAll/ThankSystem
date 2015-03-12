<%
	String lastError=(String)session.getAttribute("lastLoginError");
	//reset lastError
	session.setAttribute("lastLoginError","");
%>
<html>
<body>
<h2>Welcome, please login:</h2>
<form method="POST" action="rest/auth/login">
	<%if(lastError!=null && lastError.length()>0) { %>
		<p><%=lastError %></p>
	<%} %>
	<input type="text" name="userName" placeholder="User Name">
	<input type="password" name="password" placeholder="Password">
	<input type="hidden" name="forwardUrl" value="/ThankWeb/index.jsp">
	<button type="submit">Login</button>
</form>
<a href="signup.jsp">to signup</a>
</body>
</html>
