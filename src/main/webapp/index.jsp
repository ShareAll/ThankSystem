<%@ page import="com.thank.user.model.UserInfo" %>
<%
	UserInfo curUser=(UserInfo)session.getAttribute("CUR_USER");
%>
<html>
<body>
<h2>Welcome, <%=curUser.getName()%></h2>
<form action="rest/auth/logout" method="post">
	<button type="submit">Logout</button>
</form>
</body>
</html>
