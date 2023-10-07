<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page import="com.incq.constants.*"%>
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
		<li><a href="<%=JspConstants.ADMINREVIEWBATCH%>">
				Admin Review Batch</a></li>
		<li><a href="https://affiliate-program.amazon.com/">
				https://affiliate-program.amazon.com/</a></li>
		<li>SEO
			<ul>
				<li><a href="https://pagespeed.web.dev/" target="_blank">
						https://pagespeed.web.dev/</a></li>

				<li><a href="site:incq.com" target="_blank">site:incq.com</a></li>


				<li><a
					href="https://search.google.com/search-console?resource_id=sc-domain:incq.com"
					target="_blank">Search.google.com</a></li>
				<li><a href="https://search.google.com/test/rich-results"
					target="_blank">https://search.google.com/test/rich-results</a></li>
				<li><a
					href="https://developers.google.com/search/docs/fundamentals/seo-starter-guide#addstructureddata"
					target="_blank">SEO guide</a></li>
				<li><a
					href="https://www.bing.com/webmasters/indexnow?siteUrl=https://incq.com/"
					target="_blank">https://www.bing.com/webmasters/indexnow?siteUrl=https://incq.com/</a></li>

			</ul>
		</li>
		<li>Analytics
			<ul>
				<li><a href="https://analytics.google.com/analytics/web/"
					target="_blank">https://analytics.google.com/analytics/web/</a></li>
				<li><a
					href="https://tagmanager.google.com/#/container/accounts/6197150470/containers/166432478/workspaces/3"
					target="_blank">https://tagmanager.google.com/#/container/accounts/6197150470/containers/166432478/workspaces/3</a></li>
			</ul>
		</li>
		<li><a href="https://cloud.google.com/" target="_blank">https://cloud.google.com/</a>
			<ul>
				<li><a
					href="https://console.cloud.google.com/cloudtasks/queue/us-west1/Incq/tasks?hl=en&project=incq-397620"
					target="_blank">CloudTasks</a></li>
				<li><a
					href="https://console.cloud.google.com/appengine?hl=en&project=incq-397620"
					target="_blank">App Engine</a></li>
				<li><a
					href="https://console.cloud.google.com/datastore/databases/-default-/entities;kind=ReviewDetails;ns=__$DEFAULT$__/query/kind?hl=en&project=incq-397620"
					target="_blank">DataStore</a></li>

			</ul></li>

		<li>Social Media
			<ul>
				<li><a href="https://twitter.com/shrinesecrets" target="_blank">https://twitter.com/shrinesecrets</a></li>
				<li><a href="https://www.facebook.com/ShrineofLostSecrets"
					target="_blank">https://www.facebook.com/ShrineofLostSecrets</a></li>
			</ul>
		</li>
		<li>Facebook
			<ul>
				<li><a href="https://www.facebook.com/internetConsumerQuality"
					target="_blank">https://www.facebook.com/internetConsumerQuality</a></li>
				<li><a href="https://developers.facebook.com/tools/explorer/"
					target="_blank">https://developers.facebook.com/tools/explorer/</a></li>
				<li><a
					href="https://business.facebook.com/latest/?asset_id=131287300859823&business_id=107579500498827&nav_ref=manage_page_ap_plus_left_nav_mbs_button"
					target="_blank">https://business.facebook.com/latest/?asset_id=131287300859823&amp;business_id=107579500498827&amp;nav_ref=manage_page_ap_plus_left_nav_mbs_button</a></li>
			</ul>
		</li>
	</ul>
</body>
</html>