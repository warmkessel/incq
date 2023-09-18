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

%><?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
<url> <loc><%=JspConstants.INCQ%></loc> <lastmod><%=formattedDate%></lastmod>
</url>
<%
 for (Language langEnum : Language.values()) {
 %>
<url>
	<loc><%=JspConstants.INCQ%><%=JspConstants.AUTHORS%>?la=<%=langEnum.code%></loc>
	<lastmod><%=formattedDate%></lastmod>
</url>
<%}%>
<%
for (Language langEnum : Language.values()) {
	ArrayList<Author> authors = AuthorList.fetchAuthorsSiteMap(langEnum);
	for (Author author : authors) {
 %>
<url>
	<loc><%=URLEncoder.encode(JspConstants.INCQ + JspConstants.AUTHORSEO + author.getName(), StandardCharsets.UTF_8.toString())%>?la=<%=langEnum.code%></loc>
	<lastmod><%=author.getUpdatedDateShort()%></lastmod>
</url>
<%}}%>
<%
for (Language langEnum : Language.values()) {
	ArrayList<Review> reviews = ReviewList.fetchReviewSiteMap(langEnum);
	for (Review review : reviews) {
 %>
<url>
	<loc><%=URLEncoder.encode(JspConstants.INCQ + JspConstants.REVIEWSEO + review.getSlug(), StandardCharsets.UTF_8.toString())%>?la=<%=langEnum.code%></loc>
	<lastmod><%=review.getUpdatedDateShort()%></lastmod>
</url>
<%}}%>
</urlset>