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
String name = (String) request.getParameter(JspConstants.NAME);

if (null != id && id.length() > 0 && !id.equals("0")) {
	author.loadAuthor(new Long(id).longValue());
} else if (null != name && name.length() > 0) {
	author.loadAuthor(name, lang);
}
long authorDate = 0;

Boolean deleted = Boolean.FALSE;
Boolean bookmarked = Boolean.FALSE;
String style = (String) request.getParameter(JspConstants.STYLE);
String longDesc = (String) request.getParameter(JspConstants.LONGDESC);
String shortDesc = (String) request.getParameter(JspConstants.SHORTDESC);
Set<String> tag = new HashSet<String>();

String[] langList = {};
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
	author.setTags(tag);
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
		ID: <a
			href="<%=JspConstants.ADMINAUTHOR%>?id=<%=author.getKeyLong()%>"><%=author.getKeyLong()%></a>
	</h1>
	<br>
	<form method=post name="auth"
		action="<%=JspConstants.ADMINAUTHOR%><%=author.getKeyLong().equals(0l) ? "" : "?id=" + author.getKeyLong()%>">
		<input type=hidden name=<%=JspConstants.ID%>
			value="<%=author.getKeyLong()%>"> <select name="la">
			<%
			for (Language langEnum : Language.values()) {
			%>
			<option value="<%=langEnum.code%>"
				<%=langEnum.equals(author.getLanguage()) ? "selected" : ""%>><%=langEnum.flagUnicode%>
				<%=langEnum.name%></option>
			<%}%>
		</select><br> Deleted:<input type="radio" name="<%=JspConstants.DELETED%>"
			value="true" <%=author.isDeleted() ? "checked" : ""%>> True <input
			type="radio" name="<%=JspConstants.DELETED%>" value="false"
			<%=!author.isDeleted() ? "checked" : ""%>> False<br>
		Bookmark:<input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="true" <%=author.isBookmarked() ? "checked" : ""%>>
		True <input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="false" <%=!author.isBookmarked() ? "checked" : ""%>>
		False<br> Name:<input type="text" name="<%=JspConstants.NAME%>"
			value="<%=author.getName()%>" size="50"> <input type="button"
			value="Step
			1 - Select a User Name"
			onclick="appendToUrlAndFetch('step1')"><br> Style:
		<textarea name="<%=JspConstants.STYLE%>" rows="20" cols="80"><%=author.getStyle()%></textarea>
		<input type="button" value="Step
			2 - Suggest a Style"
			onclick="appendToUrlAndFetch('step2')"><br> Tags
		<textarea name="<%=JspConstants.TAGS%>" rows="20" cols="80"><%=author.getTagsString()%></textarea>
		<input type="button" value="Step
			3 - Suggest some Tags"
			onclick="appendToUrlAndFetch('step3')"> <br>
			 Short Description:
		<textarea name="<%=JspConstants.SHORTDESC%>" rows="20" cols="80"><%=author.getShortDescription()%></textarea>
		<input type="button" value="Step
			5 - Suggest Short Description"
			onclick="appendToUrlAndFetch('step5')">
			<input type="button" value="Step
			6 - translate Short Description"
			onclick="appendToUrlAndFetch('step6')">
			<br> Long Description:
		<textarea name="<%=JspConstants.LONGDESC%>" rows="20" cols="80"><%=author.getLongDescription()%></textarea>
		<input type="button" value="Step
			4 - Suggest Long Description"
			onclick="appendToUrlAndFetch('step4')">
			<input type="button" value="Step
			7 - translate Long Description"
			onclick="appendToUrlAndFetch('step7')"><br>

		<table>
			<%
			Map<Language, Boolean> state = AuthorList.checkAuthorLanguages(author.getName());
			for (Language langEnum : Language.values()) {
			%>
			<tr>
				<td><a
					href="<%=JspConstants.ADMINAUTHOR%>?<%=JspConstants.NAME%>=<%=author.getName()%>&<%=JspConstants.LANGUAGE%>=<%=langEnum.code%>"><%=langEnum.name%></a></td>
				<td><%=state.get(langEnum)%></td>
				<td><input name="<%=JspConstants.LANGUAGELIST%>" type=checkbox
					value="<%=langEnum.code%>" <%=!state.get(langEnum) ? "" : ""%>></td>
			</tr>
			<%}%>
		</table>
		<br> Toggle Checkboxes<input type=button value=toggle
			id="toggleButton"> <br> <input type=hidden name=save
			value="save"> <input type=submit value="save"><br>
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
	
	async function appendToUrlAndFetch(str) {
		  // Append the string to the current URL
		  const newUrl = "<%=JspConstants.EXPANDAUTHOR%>?<%=JspConstants.ID%>=<%=author.getKeyLong()%>&<%=JspConstants.LANGUAGE%>=<%=author.getLanguage().code%>&<%=JspConstants.STEP%>=";
		  try {
		    // Perform an asynchronous HTTP request
		    const response = await fetch(newUrl+str);
		    // Check if the request was successful
		    if (response.ok) {
		      window.location.reload();
		    } else {
		      console.error("Error fetching the URL:", response.status, response.statusText);
		    }
		  } catch (error) {
		    // Handle network errors or other issues
		    console.error("There was a problem with the fetch operation:", error);
		  }
		}
	
</script>
</body>