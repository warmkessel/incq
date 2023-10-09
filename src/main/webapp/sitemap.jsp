<%@ page language="java" contentType="application/xml; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ page import="java.time.LocalDate"%><%@ page
	import="java.time.format.DateTimeFormatter"%><%@ page
	import="java.net.*"%><%@ page
	import="java.util.*"%><%@ page
	import="java.nio.charset.StandardCharsets"%><%@ page
	import="com.google.cloud.datastore.*"%><%@ page import="com.incq.constants.*"%><%@ page import="com.incq.datastore.*"%><%@ page import="com.incq.entity.*"%><%
LocalDate currentDate = LocalDate.now();
	
// Define the date format
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// Convert the current date to the desired format
String formattedDate = currentDate.format(formatter);

Language lang = Language.ENGLISH;

String requestUrl = request.getRequestURL().toString();
URL url = new URL(requestUrl);
String subDomain = url.getHost().split(JspConstants.SPLIT)[0];
if (0 == subDomain.length() || JspConstants.WWW.equals(subDomain) || JspConstants.LOCALHOST.equals(subDomain)) {
	String langString = (String) request.getParameter(JspConstants.LANGUAGE);
	if (null != langString && langString.length() > 0) {
		lang = Language.findByCode(langString);
	}
} else {
	lang = Language.findByCode(subDomain);
}
%><?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
<url><loc><%=JspConstants.HTTPS + (Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code) + JspConstants.INCQP + JspConstants.INDEX%></loc><lastmod><%=formattedDate%></lastmod></url>
<url><loc><%=JspConstants.HTTPS + (Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code) + JspConstants.INCQP + JspConstants.AUTHORS%></loc><lastmod><%=formattedDate%></lastmod></url>
<url><loc><%=JspConstants.HTTPS + (Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code) + JspConstants.INCQP + JspConstants.PRIVACY%></loc><lastmod><%=PrivacyPolicyConstants.PRIVACYDATE%></lastmod></url>
<url><loc><%=JspConstants.HTTPS + (Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code) + JspConstants.INCQP + JspConstants.CONTACT%></loc><lastmod><%=formattedDate%></lastmod></url>
<%
	ArrayList<Author> authors = AuthorList.fetchAuthorsSiteMap(lang);
	for (Author author : authors) {
 %>
<url><loc><%=JspConstants.HTTPS + (Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code) +JspConstants.INCQP + JspConstants.AUTHORSEO + URLEncoder.encode(author.getName(), StandardCharsets.UTF_8.toString())%></loc><lastmod><%=author.getUpdatedDateShort()%></lastmod></url>
<%}%>
<%
	List<Review> reviews = ReviewList.fetchReviewSiteMap(lang);
	for (Review review : reviews) {
		if (null != ReviewDetailsList.fetchEventDetails(review.getKeyLong(), lang, false)){
 %>
<url><loc><%=JspConstants.HTTPS+(Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code)+JspConstants.INCQP + JspConstants.REVIEWSEO + URLEncoder.encode(review.getSlug(), StandardCharsets.UTF_8.toString())%></loc><lastmod><%=review.getUpdatedDateShort()%></lastmod></url>
<%}}%>
</urlset>