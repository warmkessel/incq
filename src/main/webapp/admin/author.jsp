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
%><jsp:forward page="/" />
<%
}
%>
<!DOCTYPE html>
<html>
<head>
<%
Author author = new Author();

String lang = Language.ENGLISH.code;

String langString = (String) request.getParameter(JspConstants.LANGUAGE);
if (null != langString && langString.length() > 0) {
	lang = Language.findByCode(langString).code;
}

String id = (String) request.getParameter(JspConstants.ID);
if (null != id && id.length() > 0) {
	author.loadAuthor(new Long(id).longValue());
}
long idLong = 0L;
try {
	idLong = Long.parseLong(id);
} catch (NumberFormatException e) {
	idLong = 0L; // Set value to 0 in case of NumberFormatException
}
long authorDate = 0;

String name = (String) request.getParameter(JspConstants.NAME);
String deleted = (String) request.getParameter(JspConstants.DELETED);
String bookmarked = (String) request.getParameter(JspConstants.BOOKMARKED);
String longDesc = (String) request.getParameter(JspConstants.LONGDESC);
String shortDesc = (String) request.getParameter(JspConstants.SHORTDESC);

boolean dirty = false;
boolean save = false;

if (null != request.getParameter(JspConstants.DIRTY) && request.getParameter(JspConstants.DIRTY).length() > 0) {
	dirty = true;
}
if (null != request.getParameter(JspConstants.SAVE) && request.getParameter(JspConstants.SAVE).length() > 0) {
	save = true;
}

if (null != request.getParameter(JspConstants.BOOKMARKED)
		&& request.getParameter(JspConstants.BOOKMARKED).length() > 0) {
	bookmarked = (String) request.getParameter(JspConstants.BOOKMARKED);
	author.setBookmarked(bookmarked);
	dirty = true;
}

if (null != request.getParameter(JspConstants.DELETED) && request.getParameter(JspConstants.DELETED).length() > 0) {
	deleted = (String) request.getParameter(JspConstants.DELETED);
	author.setDeleted(deleted);
	dirty = true;
}

if (null != request.getParameter(JspConstants.NAME) && request.getParameter(JspConstants.NAME).length() > 0) {
	name = (String) CaseControl.cleanHTML(request.getParameter(JspConstants.NAME));
	author.setName(name);
	dirty = true;
}
if (null != request.getParameter(JspConstants.SHORTDESC) && request.getParameter(JspConstants.SHORTDESC).length() > 0) {
	shortDesc = (String) CaseControl.cleanHTML(request.getParameter(JspConstants.SHORTDESC));
	author.setShortDescription(shortDesc);
	dirty = true;
}
if (null != request.getParameter(JspConstants.LONGDESC) && request.getParameter(JspConstants.LONGDESC).length() > 0) {
	longDesc = (String) CaseControl.cleanHTML(request.getParameter(JspConstants.LONGDESC));
	author.setLongDescription(longDesc);
	dirty = true;
}

if (dirty && save) {
	author.save();
}
%>
</head>
<body>
	<h1>
		ID: <a href="<%=JspConstants.ADMINAUTHOR%>?id=<%=idLong%>"><%=idLong%></a>
	</h1>
	Language:
	<form action="<%=JspConstants.ADMINREVIEW%>" method="get" id="languageForm">
		<select name="la"
			onchange="document.getElementById('languageForm').submit();">
			<%
			for (Language langEnum : Language.values()) {
			%>
			<option value="<%=langEnum.code%>"
				<%=langEnum.code.equals(lang) ? "selected" : ""%>><%=langEnum.flagUnicode%>
				<%=langEnum.name%></option>
			<%}%>
		</select><input type=hidden name=id value="<%=idLong%>">
	</form>
	<br>

	<form method=post action="./author.jsp">
		<input
			type="hidden" name="<%=JspConstants.ID%>" value="<%=idLong%>">
		Deleted:<input type="checkbox" name="<%=JspConstants.DELETED%>"
			<%=author.isDeleted() ? "checked" : ""%> value="true"><br>
		Bookmark:<input type="checkbox" name="<%=JspConstants.BOOKMARKED%>"
			<%=author.isBookmarked() ? "checked" : ""%> value="true"><br>
		Name:<input type="text" name="<%=JspConstants.NAME%>"
			value="<%=author.getName()%>" size="50"><br>
			
		Short Description:
		<textarea name="<%=JspConstants.SHORTDESC%>" rows="20" cols="80"><%=author.getShortDescription()%></textarea>
		<br> Long Description:
		<textarea name="<%=JspConstants.LONGDESC%>" rows="20" cols="80"><%=author.getLongDescription()%></textarea>
				<br> <input type=hidden name=save value="save"> <input
			type=submit value="save">
	</form>
</body>