<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.*"%>

<%
UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();
if (!userService.isUserLoggedIn() || !userService.isUserAdmin()) {
%><jsp:forward page="/" />
<%
}
%>
<!DOCTYPE html>
<html>
<head>
<title>Links</title>
</head>
<body>
	<ul>
		<li><a href="https://pagespeed.web.dev/">https://pagespeed.web.dev/</a></li>
		<li><a href="https://analytics.google.com/analytics/web/">https://analytics.google.com/analytics/web/</a></li>
		<li><a href="https://cloud.google.com/">https://cloud.google.com/</a>
			<ul>
				<li><a
					href="https://console.cloud.google.com/cloudtasks/queue/us-west1/Incq/tasks?hl=en&project=incq-397620">CloudTasks</a></li>
				<li><a
					href="https://console.cloud.google.com/appengine?hl=en&project=incq-397620">App
						Engine</a></li>
				<li><a
					href="https://console.cloud.google.com/datastore/databases/-default-/entities;kind=ReviewDetails;ns=__$DEFAULT$__/query/kind?hl=en&project=incq-397620">DataStore</a></li>

			</ul></li>
		<li><a
			href="https://developers.google.com/search/docs/fundamentals/seo-starter-guide#addstructureddata>SEO guide</a></li>
		<li><a
			href="https://developers.google.com/search/docs/fundamentals/seo-starter-guide#addstructureddataa href="site:incq.com">site:incq.com</a></li>
		<li><a
			href="https://search.google.com/search-console?resource_id=sc-domain:incq.com">Search.google.com</a></li>
		<li><a href="https://search.google.com/test/rich-results">https://search.google.com/test/rich-results</a></li>
		
		
		<li><ul>
				<li><a href="https://twitter.com/shrinesecrets">https://twitter.com/shrinesecrets</a></li>
				<li><a href="https://www.facebook.com/ShrineofLostSecrets">https://www.facebook.com/ShrineofLostSecrets</a></li>
			</ul></li>
	</ul>
</body>
</html>