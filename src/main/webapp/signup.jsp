<%
	String lastError=(String)session.getAttribute("lastLoginError");
	//reset lastError
	session.setAttribute("lastLoginError","");
%>
<html>
<body>
<h2>Signup</h2>
<form method="POST" action="rest/auth/signup">
	<%if(lastError!=null && lastError.length()>0) { %>
		<p><%=lastError %></p>
	<%} %>
	<input type="text" name="userName" placeholder="User Name">
	<input type="text" name="emailAddress" placeholder="email Address">
	<input type="password" name="password" placeholder="Password">
	<input type="hidden" name="forwardUrl" value="/ThankWeb/index.jsp">
	<button type="submit">SignUp</button>
</form>
</body>
</html>
