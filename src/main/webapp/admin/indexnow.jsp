<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ page import="java.time.LocalDate"%><%@ page
	import="java.time.format.DateTimeFormatter"%><%@ page
	import="java.net.*"%><%@ page import="java.util.*"%><%@ page
	import="java.nio.charset.StandardCharsets"%><%@ page
	import="com.google.cloud.datastore.*"%><%@ page
	import="com.incq.constants.*"%><%@ page import="com.incq.datastore.*"%><%@ page
	import="com.incq.entity.*"%>
<%
LocalDate currentDate = LocalDate.now();
	
// Define the date format
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// Convert the current date to the desired format
String formattedDate = currentDate.format(formatter);

%>
<form acton=api.indexnow.org method=post>
	<input type=text name=host value="incq.com"> <input type=text
		name=key value="88500517616d495992167e3b11892346"> <input
			type=text name=keyLocation
			value="http://incq.com/88500517616d495992167e3b11892346.txt">
				<textarea name=urlList rows="20" cols="99">
[<%
 int x=0;
for (Language langEnum : Language.values()) {
	List<Review> reviews = ReviewList.fetchReviewSiteMap(langEnum);
	for (Review review : reviews) {
		if (null != ReviewDetailsList.fetchEventDetails(review.getKeyLong(), langEnum, false)){
 %>"<%=JspConstants.HTTPS+(Language.ENGLISH.equals(langEnum) ? JspConstants.WWW : langEnum.code)+JspConstants.INCQP + JspConstants.REVIEWSEO + URLEncoder.encode(review.getSlug(), StandardCharsets.UTF_8.toString())%>",
<%x = x + 1;
if(x > 99)break;}
		if(x > 99)break;}
	if(x > 99)break;}%>
]</textarea> <input type=submit>
</form>