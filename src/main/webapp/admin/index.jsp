<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.*"%>

<%
UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();
if(!userService.isUserLoggedIn() || !userService.isUserAdmin()){
%><jsp:forward page="/" /><%
}
%>
<!DOCTYPE html>
<html>
<head>
<title>Links</title>
</head>
<body>
	<ul>
		<li><a href="https://analytics.google.com/analytics/web/">https://analytics.google.com/analytics/web/</a></li>
		<li><ul>
				<li><a href="https://twitter.com/shrinesecrets">https://twitter.com/shrinesecrets</a></li>
				<li><a href="https://www.facebook.com/ShrineofLostSecrets">https://www.facebook.com/ShrineofLostSecrets</a></li>
			</ul></li>
	</ul>
</body>
</html>