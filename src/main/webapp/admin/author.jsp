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

Language lang = Language.ENGLISH;

String langString = (String) request.getParameter(JspConstants.LANGUAGE);
if (null != langString && langString.length() > 0) {
	lang = Language.findByCode(langString);
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
Boolean deleted = Boolean.FALSE;
Boolean bookmarked = Boolean.FALSE;
String style = (String) request.getParameter(JspConstants.STYLE);
String longDesc = (String) request.getParameter(JspConstants.LONGDESC);
String shortDesc = (String) request.getParameter(JspConstants.SHORTDESC);

String[] langList = {};
boolean dirty = false;
boolean save = false;

if (null != request.getParameter(JspConstants.DIRTY) && request.getParameter(JspConstants.DIRTY).length() > 0) {
	dirty = true;
}
if (null != request.getParameter(JspConstants.SAVE) && request.getParameter(JspConstants.SAVE).length() > 0) {
	save = true;
}

if (null != request.getParameter(JspConstants.DELETED) && request.getParameter(JspConstants.DELETED).length() > 0) {
	deleted = Boolean.valueOf(request.getParameter(JspConstants.DELETED));
	author.setDeleted(deleted);
	dirty = true;
}

if (null != request.getParameter(JspConstants.BOOKMARKED)
		&& request.getParameter(JspConstants.BOOKMARKED).length() > 0) {
	bookmarked = Boolean.valueOf(request.getParameter(JspConstants.BOOKMARKED));
	author.setBookmarked(bookmarked);
	dirty = true;
}

if (null != request.getParameter(JspConstants.NAME) && request.getParameter(JspConstants.NAME).length() > 0) {
	name = (String) request.getParameter(JspConstants.NAME);
	author.setName(name);
	dirty = true;
}
if (null != request.getParameter(JspConstants.STYLE) && request.getParameter(JspConstants.STYLE).length() > 0) {
	style = (String) request.getParameter(JspConstants.STYLE);
	author.setStyle(style);
	dirty = true;
}
if (null != request.getParameter(JspConstants.SHORTDESC) && request.getParameter(JspConstants.SHORTDESC).length() > 0) {
	shortDesc = (String) request.getParameter(JspConstants.SHORTDESC);
	author.setShortDescription(shortDesc);
	dirty = true;
}
if (null != request.getParameter(JspConstants.LONGDESC) && request.getParameter(JspConstants.LONGDESC).length() > 0) {
	longDesc = (String) request.getParameter(JspConstants.LONGDESC);
	author.setLongDescription(longDesc);
	dirty = true;
}
if (null != request.getParameter(JspConstants.LANGUAGE) && request.getParameter(JspConstants.LANGUAGE).length() > 0) {
	lang = Language.findByCode((String) request.getParameter(JspConstants.LANGUAGE));
	author.setLanguage(lang);
	dirty = true;
}

if (dirty && save) {
	author.save();
}
langList = request.getParameterValues(JspConstants.LANGUAGELIST);

if (null != langList && langList.length > 0) {
	AuthorList.expandLanguage(name, langList);
}
%>
</head>
<body>
	<h1>
		ID: <a href="<%=JspConstants.ADMINAUTHOR%>?id=<%=idLong%>"><%=idLong%></a>
	</h1>
	<br>
	<form method=post name="auth" action="<%=JspConstants.ADMINAUTHOR%>">
		<input type=hidden name=id value="<%=idLong%>"> <select
			name="la">
			<%
			for (Language langEnum : Language.values()) {
			%>
			<option value="<%=langEnum.code%>"
				<%=langEnum.equals(lang) ? "selected" : ""%>><%=langEnum.flagUnicode%>
				<%=langEnum.name%></option>
			<%}%>
		</select><br> <input type="hidden" name="<%=JspConstants.ID%>"
			value="<%=idLong%>"> Deleted:<input type="radio"
			name="<%=JspConstants.DELETED%>" value="true"
			<%=author.isDeleted() ? "checked" : ""%>> True <input
			type="radio" name="<%=JspConstants.DELETED%>" value="false"
			<%=!author.isDeleted() ? "checked" : ""%>> False<br>
		Bookmark:<input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="true" <%=author.isBookmarked() ? "checked" : ""%>>
		True <input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="false" <%=!author.isBookmarked() ? "checked" : ""%>>
		False<br> Name:<input type="text" name="<%=JspConstants.NAME%>"
			value="<%=author.getName()%>" size="50"><br> Style:
		<textarea name="<%=JspConstants.STYLE%>" rows="20" cols="80"><%=author.getStyle()%></textarea>
		<br> Short Description:
		<textarea name="<%=JspConstants.SHORTDESC%>" rows="20" cols="80"><%=author.getShortDescription()%></textarea>
		<br> <br> Long Description:
		<textarea name="<%=JspConstants.LONGDESC%>" rows="20" cols="80"><%=author.getLongDescription()%></textarea>
		<br>

		<table>
			<%
			Map<Language, Boolean> state = AuthorList.checkAuthorLanguages(author.getName());
			for (Language langEnum : Language.values()) {
			%>
			<tr>
				<td><a href=""><%=langEnum.name%></a></td>
				<td><%=state.get(langEnum)%></td>
				<td><input name="<%=JspConstants.LANGUAGELIST%>" type=checkbox
					value="<%=langEnum.code%>" <%=!state.get(langEnum) ? "" : ""%>></td>
			</tr>
			<%}%>
		</table>
		<br>
		Toggle Checkboxes<input type=button value=toggle id="toggleButton">
		<br> <input type=hidden name=save value="save"> <input
			type=submit value="save"><br>
	</form>


	<script type="text/javascript">
	// Function to toggle checkboxes
	var state = true;
	function toggleCheckboxes() {
		// Define a list of values you want to toggle
		//const toggleList = ["en", "ar"];
		const toggleList = [<%for (Language langEnum : Language.values()) {%><%=state.get(langEnum) ? "" : "\"" + langEnum.code + "\", "%><%}%>];

		// Get all checkboxes with name="list"
		const checkboxes = document.querySelectorAll('input[name="list"]');

		// Iterate through each checkbox
		checkboxes.forEach((checkbox) => {
			if(state){
				if (toggleList.includes(checkbox.value)) {
					checkbox.checked = true;
				}
				else{
					checkbox.checked = false;

				}
			}
			else{
			checkbox.checked = false;
			}
		});
		state = !state;
	}

	// Attach the toggle function to the button click event
	document.getElementById('toggleButton').addEventListener('click', toggleCheckboxes);
</script>
</body>