<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.incq.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.incq.util.*"%>
<%@ page import="com.incq.entity.*"%>
<%@ page import="com.incq.datastore.*"%>
<%@ page import="com.google.cloud.datastore.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page import="com.incq.enqueue.*"%>
<%@ page import="com.incq.instantiate.helpers.*"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<%
UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();
if (!userService.isUserLoggedIn() || !userService.isUserAdmin()) {
%><jsp:forward page="/" />
<%}%>
<%
Review review = new Review();

Language lang = Language.ENGLISH;

String id = (String) request.getParameter(JspConstants.ID);
if (null != id && id.length() > 0 && !id.equals("0")) {
	review.loadEvent(new Long(id).longValue(), lang, true);
} else {
	// Get the full URL and query string
	String requestURL = request.getRequestURL().toString();
	String requestURI = request.getRequestURI();
	String contextPath = request.getContextPath();

	String prefix = contextPath + JspConstants.ADMINFACEBOOKSEO;
	if (requestURI.startsWith(prefix)) {
		String slug = requestURI.substring(prefix.length());
		// URL decode to convert any '+' to ' ' and '%' encoding to characters
		slug = URLDecoder.decode(slug, "UTF-8");
		review.loadFromEntity(ReviewList.fetchReview(slug), lang);
	}
}
boolean theReturn = FacebookHelper.postToFacebookGraphAPI(review);
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<form action="https://graph.facebook.com/v18.0/InternetConsumerQuality/feed/" method=post><input type=text name="message" value="test message"><br><input type=text name="accessToken" value="EAAMIfJFBq94BO9gZAEZCjCikQYqd04c9fG2o6hkdMy4SsG4fLVZCNxoNZA3KxHl4FmEShwWdhdKEi5TUikSnA4jD5ZC2vF6sjs2XOVRPf6KGSdd1aYggZA9GZBwb7rIZBGPshT1rEHLEdwzyDOuzVHbeJpPIkgGuxGpxz06LNTrDC99mOMQf5U9Oo6FyQZCZCqqzdeNDTJZBEYfAkn2wQGiXgrSt5UZD"><br><input type=submit></form></body>
</html>