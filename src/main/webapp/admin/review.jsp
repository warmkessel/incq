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

<%
UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();
if (!userService.isUserLoggedIn() || !userService.isUserAdmin()) {
	RequestDispatcher requestDispatcher = request.getRequestDispatcher("/");
	requestDispatcher.forward(request, response);
	return; // Important to stop further page processing
}
%>
<!DOCTYPE html>
<html>
<head>
<%
Review review = new Review();

Language lang = Language.ENGLISH;

String langString = (String) request.getParameter(JspConstants.LANGUAGE);
if (null != langString && langString.length() > 0) {
	lang = Language.findByCode(langString);
}

String id = (String) request.getParameter(JspConstants.ID);
if (null != id && id.length() > 0) {
	review.loadEvent(new Long(id).longValue(), lang);
}
long idLong = 0L;
try {
	idLong = Long.parseLong(id);
} catch (NumberFormatException e) {
	idLong = 0L; // Set value to 0 in case of NumberFormatException
}
long reviewDate = 0;

Set<String> tag = new HashSet<String>();
Set<String> meta = new HashSet<String>();
String link = (String) request.getParameter(JspConstants.LINK);
String summary = (String) request.getParameter(JspConstants.SUMMARY);
Boolean deleted = Boolean.FALSE;
Boolean bookmarked = Boolean.FALSE;
Set<String> media = new HashSet<String>();

String title = (String) request.getParameter(JspConstants.TITLE);
String longDesc = (String) request.getParameter(JspConstants.LONGDESC);
String author = (String) request.getParameter(JspConstants.AUTHOR);

boolean dirty = false;
boolean save = false;

if (null != request.getParameter(JspConstants.DIRTY) && request.getParameter(JspConstants.DIRTY).length() > 0) {
	dirty = true;
}
if (null != request.getParameter(JspConstants.SAVE) && request.getParameter(JspConstants.SAVE).length() > 0) {
	save = true;
}

if (null != request.getParameter(JspConstants.TAGS) && request.getParameter(JspConstants.TAGS).length() > 0) {
	List<String> list = Arrays.asList(request.getParameterValues(JspConstants.TAGS));
	for (String str : list) {
		String[] split = str.split(" ");
		for (int x = 0; x < split.length; x++) {
	tag.add(split[x]);
		}
	}
	review.setTags(tag);
}
if (null != request.getParameter(JspConstants.META) && request.getParameter(JspConstants.META).length() > 0) {
	List<String> list = Arrays.asList(request.getParameterValues(JspConstants.META));
	for (String str : list) {
		String[] split = str.split(" ");
		for (int x = 0; x < split.length; x++) {
	meta.add(split[x]);
		}
	}
	review.setMeta(meta);
}
if (null != request.getParameter(JspConstants.DELETED) && request.getParameter(JspConstants.DELETED).length() > 0) {
	deleted = Boolean.valueOf(request.getParameter(JspConstants.DELETED));
	review.setDeleted(deleted);
	dirty = true;
}

if (null != request.getParameter(JspConstants.BOOKMARKED)
		&& request.getParameter(JspConstants.BOOKMARKED).length() > 0) {
	bookmarked = Boolean.valueOf(request.getParameter(JspConstants.BOOKMARKED));
	review.setBookmarked(bookmarked);
	dirty = true;
}

if (null != request.getParameter(JspConstants.MEDIA) && request.getParameter(JspConstants.MEDIA).length() > 0) {
	List<String> list = Arrays.asList(request.getParameterValues(JspConstants.MEDIA));
	for (String str : list) {
		String[] split = str.split(" ");
		for (int x = 0; x < split.length; x++) {
	media.add(split[x]);
		}
	}
	review.setMedia(media);
}
if (null != request.getParameter(JspConstants.LINK) && request.getParameter(JspConstants.LINK).length() > 0) {
	link = (String) request.getParameter(JspConstants.LINK);
	review.setLink(link);
	dirty = true;
}
if (null != request.getParameter(JspConstants.SUMMARY) && request.getParameter(JspConstants.SUMMARY).length() > 0) {
	summary = (String) request.getParameter(JspConstants.SUMMARY);
	review.getReviewDetails().setSummary(summary);
	dirty = true;
}
if (null != request.getParameter(JspConstants.TITLE) && request.getParameter(JspConstants.TITLE).length() > 0) {
	title = (String) request.getParameter(JspConstants.TITLE);
	review.getReviewDetails().setTitle(title);
	dirty = true;
}
if (null != request.getParameter(JspConstants.AUTHOR) && request.getParameter(JspConstants.AUTHOR).length() > 0) {
	author = (String) request.getParameter(JspConstants.AUTHOR);
	review.setAuthor(author);
	dirty = true;
}
if (null != request.getParameter(JspConstants.LONGDESC) && request.getParameter(JspConstants.LONGDESC).length() > 0) {
	longDesc = (String) request.getParameter(JspConstants.LONGDESC);
	review.getReviewDetails().setLongDesc(longDesc);
	dirty = true;
}
if (dirty && save) {
	review.save();
}
%>
</head>
<body>
	<h1>
		ID: <a href="<%=JspConstants.ADMINREVIEW%>?id=<%=idLong%>"><%=idLong%></a>
	</h1>
	Language:
	<form action="<%=JspConstants.ADMINREVIEW%>" method="get"
		id="languageForm">
		<select name="la"
			onchange="document.getElementById('languageForm').submit();">
			<%
			for (Language langEnum : Language.values()) {
			%>
			<option value="<%=langEnum.code%>"
				<%=langEnum.equals(lang) ? "selected" : ""%>><%=langEnum.flagUnicode%>
				<%=langEnum.name%></option>
			<%}%>
		</select><input type=hidden name=id value="<%=idLong%>">
	</form>
	<br>

	<form method=post action="./review.jsp">
		<input type="hidden" name="<%=JspConstants.ID%>" value="<%=idLong%>">
		Deleted:<input type="radio" name="<%=JspConstants.DELETED%>"
			value="true" <%=review.isDeleted() ? "checked" : ""%>> True <input
			type="radio" name="<%=JspConstants.DELETED%>" value="false"
			<%=!review.isDeleted() ? "checked" : ""%>> False<br>
		Bookmark:<input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="true" <%=review.isBookmarked() ? "checked" : ""%>>
		True <input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="false" <%=!review.isBookmarked() ? "checked" : ""%>>
		False<br> Link:<input type="text" name="<%=JspConstants.LINK%>"
			value="<%=review.getLink()%>" size="50"><br> Title:<input
			type="text" name="<%=JspConstants.TITLE%>"
			value="<%=review.getReviewDetails().getTitle()%>" size="50"><br>
		Title:<input type="text" name="<%=JspConstants.AUTHOR%>"
			value="<%=review.getAuthor()%>" size="50"><br> Summary:
		<textarea name="<%=JspConstants.SUMMARY%>" rows="20" cols="80"><%=review.getReviewDetails().getSummary()%></textarea>
		<br> Long Description:
		<textarea name="<%=JspConstants.LONGDESC%>" rows="20" cols="80"><%=review.getReviewDetails().getLongDesc()%></textarea>
		<br> Media
		<textarea name="<%=JspConstants.MEDIA%>" rows="20" cols="80"><%=review.getMediaString()%></textarea>
		<br> Tags
		<textarea name="<%=JspConstants.TAGS%>" rows="20" cols="80"><%=review.getTagsString()%></textarea>
		<br> Meta
		<textarea name="<%=JspConstants.META%>" rows="20" cols="80"><%=review.getMetaString()%></textarea>
		<br> <input type=hidden name=save value="save"> <input
			type=submit value="save">
	</form>
</body>