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
if (null != id && id.length() > 0 && !id.equals("0")) {
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
String introductiondesc = (String) request.getParameter(JspConstants.INTRODUCTIONDESC);
String reviewbodydesc = (String) request.getParameter(JspConstants.REVIEWBODYDESC);
String conclusiondesc = (String) request.getParameter(JspConstants.CONCLUSIONDESC);
String author = (String) request.getParameter(JspConstants.AUTHOR);
String source = (String) request.getParameter(JspConstants.SOURCE);

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
		String[] split = str.split(",");
		for (int x = 0; x < split.length; x++) {
	tag.add(split[x]);
		}
	}
	review.setTags(tag);
}
if (null != request.getParameter(JspConstants.META) && request.getParameter(JspConstants.META).length() > 0) {
	List<String> list = Arrays.asList(request.getParameterValues(JspConstants.META));
	for (String str : list) {
		String[] split = str.split(",");
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
if (null != request.getParameter(JspConstants.SOURCE) && request.getParameter(JspConstants.SOURCE).length() > 0) {
	source = (String) request.getParameter(JspConstants.SOURCE);
	review.setSource(source);
	dirty = true;
}
if (null != request.getParameter(JspConstants.INTRODUCTIONDESC)
		&& request.getParameter(JspConstants.INTRODUCTIONDESC).length() > 0) {
	introductiondesc = (String) request.getParameter(JspConstants.INTRODUCTIONDESC);
	review.getReviewDetails().setIntroduction(introductiondesc);
	dirty = true;
}
if (null != request.getParameter(JspConstants.REVIEWBODYDESC)
		&& request.getParameter(JspConstants.REVIEWBODYDESC).length() > 0) {
	reviewbodydesc = (String) request.getParameter(JspConstants.REVIEWBODYDESC);
	review.getReviewDetails().setReviewBody(reviewbodydesc);
	dirty = true;
}
if (null != request.getParameter(JspConstants.CONCLUSIONDESC)
		&& request.getParameter(JspConstants.CONCLUSIONDESC).length() > 0) {
	conclusiondesc = (String) request.getParameter(JspConstants.CONCLUSIONDESC);
	review.getReviewDetails().setConclusion(conclusiondesc);
	dirty = true;
}
if (dirty && save) {
	review.save();
}
langList = request.getParameterValues(JspConstants.LANGUAGELIST);

if (null != langList && langList.length > 0) {
	ReviewDetailsList.expandLanugage(review.getKeyLong(), langList);
}
%>
</head>
<body>
	<h1>
		ID: <a
			href="<%=JspConstants.ADMINREVIEW%>?id=<%=review.getKeyLong()%>"><%=review.getKeyLong()%></a>
	</h1>
	Language:
	<form action="<%=JspConstants.ADMINREVIEW%><%=review.getKeyLong().equals(0l) ? "" : "?" + JspConstants.ID + "=" + review.getKeyLong()%><%=Language.ENGLISH.equals(lang) ? "" : "?" + JspConstants.LANGUAGE + "=" + lang.code%>" method="get"
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
		</select><input type=hidden name=id value="<%=review.getKeyLong()%>">
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
			value="<%=review.getReviewDetails().getTitle()%>" size="50">
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button"
			value="Step 3 - Extract Title and Media from source"
			onclick="appendToUrlAndFetch('step3')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button"
			value="Step 5 - Translate Title"
			onclick="appendToUrlAndFetch('step5')">
		<%}%>
		<br> Author:<input type="text" name="<%=JspConstants.AUTHOR%>"
			value="<%=review.getAuthor()%>" size="50">
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step
			Step 2 - Determine Author"
			onclick="appendToUrlAndFetch('step2')">
		<%}%><br> Source:
		<textarea name="<%=JspConstants.SOURCE%>" rows="20" cols="80"><%=review.getSource()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 1 - Fetch the Source"
			onclick="appendToUrlAndFetch('step1')">
		<%}%><br> Summary:
		<textarea name="<%=JspConstants.SUMMARY%>" rows="20" cols="80"><%=review.getReviewDetails().getSummary()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 9 - Write a summary"
			onclick="appendToUrlAndFetch('step9')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button"
			value="Step 4 - Translate Summary"
			onclick="appendToUrlAndFetch('step4')">
		<%}%>
		<br> Introduction:
		<textarea name="<%=JspConstants.INTRODUCTIONDESC%>" rows="20"
			cols="80"><%=review.getReviewDetails().getIntroduction()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 7 - Write the introductions"
			onclick="appendToUrlAndFetch('step7')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button"
			value="Step 1 - Translate Introduction"
			onclick="appendToUrlAndFetch('step1')">
		<%}%>
		<br> ReviewBody:
		<textarea name="<%=JspConstants.REVIEWBODYDESC%>" rows="20" cols="80"><%=review.getReviewDetails().getReviewBody()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 6 - Write the Review Body"
			onclick="appendToUrlAndFetch('step6')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button"
			value="Step 2 - Translate Review"
			onclick="appendToUrlAndFetch('step2')">
		<%}%>
		<br> Conclusion:
		<textarea name="<%=JspConstants.CONCLUSIONDESC%>" rows="20" cols="80"><%=review.getReviewDetails().getConclusion()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 8 - Write the Conclusion"
			onclick="appendToUrlAndFetch('step8')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button"
			value="Step 3 - Translate Conclusion"
			onclick="appendToUrlAndFetch('step3')">
		<%}%>
		<br> Media
		<textarea name="<%=JspConstants.MEDIA%>" rows="20" cols="80"><%=review.getMediaString()%></textarea>
		<br> Tags
		<textarea name="<%=JspConstants.TAGS%>" rows="20" cols="80"><%=review.getTagsString()%></textarea>
		<%
		if (Language.ENGLISH.equals(review.getReviewDetails().getLanguage())) {
		%><input type="button" value="Step 5 - Determine the Tags"
			onclick="appendToUrlAndFetch('step5')">
		<%}%><br> Meta
		<textarea name="<%=JspConstants.META%>" rows="20" cols="80"><%=review.getMetaString()%></textarea>
		<%
		if (Language.ENGLISH.equals(review.getReviewDetails().getLanguage())) {
		%><input type="button" value="Step 4 - Determine the Meta"
			onclick="appendToUrlAndFetch('step4')">
		<%}%><br>

		<table>
			<%
			Map<Language, Boolean> state = ReviewDetailsList.checkReviewDetailsLanguages(idLong);
			Map<Language, Boolean> ready = ReviewDetailsList.checkReviewDetailsReady(idLong);
			for (Language langEnum : Language.values()) {
			%>
			<tr>
				<td><a href="<%=JspConstants.ADMINREVIEW%>?<%=JspConstants.ID%>=<%=review.getKeyLong()%>&<%=JspConstants.LANGUAGE%>=<%=langEnum.code%>"><%=langEnum.name%></a></td>
				<td><%=state.get(langEnum) ? "Instantiated" : ""%></td>
				<td><%=ready.get(langEnum) ? "Ready" : ""%></td>
				<td><input name="<%=JspConstants.LANGUAGELIST%>" type=checkbox
					value="<%=langEnum.code%>" <%=!state.get(langEnum) ? "" : ""%>></td>
			</tr>
			<%}%>
		</table>
		<br>
		<button id="toggleButton">Toggle Checkboxes</button>
		<br> <br> <input type=hidden name=save value="save">
		<input type=submit value="save">
	</form>

	<script type="text/javascript">
	// Function to toggle checkboxes
	var state =0;
	function toggleCheckboxes() {
		
		// Define a list of values you want to toggle
		const toggleList = ["en", "ar"];
		//const toggleList = [<%for (Language langEnum : Language.values()) {%><%=state.get(langEnum) ? "" : "\"" + langEnum.code + "\", "%><%}%>];

		// Get all checkboxes with name="list"
		const checkboxes = document.querySelectorAll('input[name="list"]');

		// Iterate through each checkbox
		checkboxes.forEach((checkbox) => {
			switch(state){
			case 0:
				if (toggleList.includes(checkbox.value)) {
					checkbox.checked = false;
				}
				else{
					checkbox.checked = true;

				}
			break;
			case 1:
			checkbox.checked = false;
			break;
			case 2:
			checkbox.checked = true;
			break;
			}
			/* // If the checkbox value is in the toggleList
			if (toggleList.includes(checkbox.value)) {
				// Toggle the checkbox state
				checkbox.checked = !checkbox.checked;
			} else {
				// If it's not in the toggleList, then make sure it's unchecked
				checkbox.checked = false;
			} */
		});
		state = state + 1;
		if(state > 2){
			state = 0;
		}
	}

	// Attach the toggle function to the button click event
	document.getElementById('toggleButton').addEventListener('click', toggleCheckboxes);
	
	async function appendToUrlAndFetch(str) {
		  // Append the string to the current URL
		  <%if (Language.ENGLISH.equals(lang)) {%>
		const newUrl = "<%=JspConstants.EXPANDREVIEW%>?<%=JspConstants.ID%>=<%=review.getKeyLong()%>&<%=JspConstants.LANGUAGE%>=<%=review.getReviewDetails().getLanguage().code%>&<%=JspConstants.CONTINUE%>=false&<%=JspConstants.STEP%>=";
		<%} else {%>
			const newUrl = "<%=JspConstants.EXPANDREVIEWDETAILS%>?<%=JspConstants.ID%>=<%=review.getKeyLong()%>&<%=JspConstants.LANGUAGE%>=<%=lang.code%>&<%=JspConstants.CONTINUE%>=false&<%=JspConstants.STEP%>=";
		<%}%>
			try {
		    // Perform an asynchronous HTTP request
		    const response = await fetch(newUrl+str);
		    // Check if the request was successful
		    if (response.ok) {
		    	 setTimeout(() => {
		    	        window.location.reload();
		    	      }, 5000); // 5000 milliseconds = 5 seconds
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