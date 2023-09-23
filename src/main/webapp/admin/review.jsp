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

String id = (String) request.getParameter(JspConstants.ID);
if (null != id && id.length() > 0 && !id.equals("0")) {
	review.loadEvent(new Long(id).longValue(), lang, true);
} else {
	// Get the full URL and query string
	String requestURL = request.getRequestURL().toString();
	String requestURI = request.getRequestURI();
	String contextPath = request.getContextPath();

	String prefix = contextPath + JspConstants.ADMINREVIEWSEO;
	if (requestURI.startsWith(prefix)) {
		String slug = requestURI.substring(prefix.length());
		// URL decode to convert any '+' to ' ' and '%' encoding to characters
		slug = URLDecoder.decode(slug, "UTF-8");
		review.loadFromEntity(ReviewList.fetchReview(slug), lang);
	}
}
long reviewDate = 0;

Set<String> tag = new HashSet<String>();
Set<String> meta = new HashSet<String>();
String name = (String) request.getParameter(JspConstants.NAME);
String desc = (String) request.getParameter(JspConstants.DESC);
String slug = (String) request.getParameter(JspConstants.SLUG);
String link = (String) request.getParameter(JspConstants.LINK);
String call = (String) request.getParameter(JspConstants.CALL);
String score = (String) request.getParameter(JspConstants.SCORE);
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

if (Language.ENGLISH.equals(lang) && null != request.getParameter(JspConstants.TAGS)
		&& request.getParameter(JspConstants.TAGS).length() > 0) {
	review.setTags(request.getParameter(JspConstants.TAGS));
}
if (Language.ENGLISH.equals(lang) && null != request.getParameter(JspConstants.META)
		&& request.getParameter(JspConstants.META).length() > 0) {
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
	if (Language.ENGLISH.equals(lang)) {
		deleted = Boolean.valueOf(request.getParameter(JspConstants.DELETED));
		review.setDeleted(deleted);
	}
	deleted = Boolean.valueOf(request.getParameter(JspConstants.DELETED));
	review.getReviewDetails().setDeleted(deleted);
	dirty = true;
}

if (Language.ENGLISH.equals(lang) && null != request.getParameter(JspConstants.BOOKMARKED)
		&& request.getParameter(JspConstants.BOOKMARKED).length() > 0) {
	bookmarked = Boolean.valueOf(request.getParameter(JspConstants.BOOKMARKED));
	review.setBookmarked(bookmarked);
	dirty = true;
}
if (Language.ENGLISH.equals(lang) && null != request.getParameter(JspConstants.SCORE)
		&& request.getParameter(JspConstants.SCORE).length() > 0) {
	review.setScore(score);
	dirty = true;
}

if (Language.ENGLISH.equals(lang) && null != request.getParameter(JspConstants.MEDIA)
		&& request.getParameter(JspConstants.MEDIA).length() > 0) {
	List<String> list = Arrays.asList(request.getParameterValues(JspConstants.MEDIA));
	for (String str : list) {
		String[] split = str.split(" ");
		for (int x = 0; x < split.length; x++) {
	media.add(split[x]);
		}
	}
	review.setMedia(media);
}
if (Language.ENGLISH.equals(lang) && null != request.getParameter(JspConstants.SLUG)
		&& request.getParameter(JspConstants.SLUG).length() > 0) {
	slug = (String) request.getParameter(JspConstants.SLUG);
	review.setSlug(slug);
	dirty = true;
}
if (null != request.getParameter(JspConstants.CALL) && request.getParameter(JspConstants.CALL).length() > 0) {
	call = (String) request.getParameter(JspConstants.CALL);
	review.getReviewDetails().setCall(call);
	dirty = true;
}
if (Language.ENGLISH.equals(lang) && null != request.getParameter(JspConstants.LINK)
		&& request.getParameter(JspConstants.LINK).length() > 0) {
	link = (String) request.getParameter(JspConstants.LINK);
	review.setLink(link);
	dirty = true;
}
if (null != request.getParameter(JspConstants.SUMMARY) && request.getParameter(JspConstants.SUMMARY).length() > 0) {
	summary = (String) request.getParameter(JspConstants.SUMMARY);
	review.getReviewDetails().setSummary(summary);
	dirty = true;
}
if (null != request.getParameter(JspConstants.NAME) && request.getParameter(JspConstants.NAME).length() > 0) {
	name = (String) request.getParameter(JspConstants.NAME);
	review.getReviewDetails().setName(name);
	dirty = true;
}
if (null != request.getParameter(JspConstants.DESC) && request.getParameter(JspConstants.DESC).length() > 0) {
	desc = (String) request.getParameter(JspConstants.DESC);
	review.getReviewDetails().setDesc(desc);
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
	save = false;
}
langList = request.getParameterValues(JspConstants.LANGUAGELIST);

if (null != langList && langList.length > 0) {
	ReviewDetailsList.expandReviewDetails(review.getKeyLong(), langList);
}
%>
</head>
<body>
	<h1>
		Review ID: <a
			href="<%=JspConstants.ADMINREVIEW%>?id=<%=review.getKeyLong()%>"><%=review.getKeyLong()%></a>
	</h1>
	<br>

	<form method=post
		action="<%=JspConstants.ADMINREVIEW%><%=review.getKeyLong().equals(0l) ? "" : "?" + JspConstants.ID + "=" + review.getKeyLong()%>">
		<input type="hidden" name="<%=JspConstants.LANGUAGE%>"
			value="<%= Language.ENGLISH.code%>"> <input type="hidden"
			name="<%=JspConstants.ID%>" value="<%=review.getKeyLong()%>">
		<table>
			<%
			Map<Language, Boolean> state = ReviewDetailsList.checkReviewDetailsLanguages(review.getKeyLong());
			Map<Language, Boolean> ready = ReviewDetailsList.checkReviewDetailsReady(review.getKeyLong());
			for (Language langEnum : Language.values()) {
			%>
			<tr>
				<td>
					<%if (state.get(langEnum)) {%><a
					href="<%=JspConstants.ADMINREVIEW%>?<%=JspConstants.ID%>=<%=review.getKeyLong()%>&<%=JspConstants.LANGUAGE%>=<%=langEnum.code%>">
						<%}%><%=langEnum.name%> <%
 if (state.get(langEnum)) {
 %>
				</a> <%}%>
				</td>
				<td><%=state.get(langEnum) ? "Instantiated" : ""%></td>
				<td><%=ready.get(langEnum) ? "Ready" : ""%></td>
				<td><input name="<%=JspConstants.LANGUAGELIST%>" type=checkbox
					value="<%=langEnum.code%>" <%=!state.get(langEnum) ? "" : ""%>></td>
			</tr>
			<%}%>
		</table>
		<br> Toggle Checkboxes<input type=button value=toggle
			id="toggleButton"> <br><input type=hidden
			name=save value="save"> Deleted:<input type="radio"
			name="<%=JspConstants.DELETED%>" value="true"
			<%=review.isDeleted() ? "checked" : ""%>> True <input
			type="radio" name="<%=JspConstants.DELETED%>" value="false"
			<%=!review.isDeleted() ? "checked" : ""%>> False<br>
		Bookmark:<input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="true" <%=review.isBookmarked() ? "checked" : ""%>>
		True <input type="radio" name="<%=JspConstants.BOOKMARKED%>"
			value="false" <%=!review.isBookmarked() ? "checked" : ""%>>
		False<br> Link:<input type="text" name="<%=JspConstants.LINK%>"
			value="<%=review.getLink()%>" size="50"><br> Slug:<input
			type="text" name="<%=JspConstants.SLUG%>"
			value="<%=review.getSlug()%>" size="50">
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step
			Step 11 - Determine Slug"
			onclick="appendToUrlAndFetch('step11')">
		<%}%><br> Score:<input type="text" name="<%=JspConstants.SCORE%>"
			value="<%=review.getScore()%>" size="10">
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step
			Step 13 - Determine Score"
			onclick="appendToUrlAndFetch('step13')">
		<%}%><br>Source:
		<textarea name="<%=JspConstants.SOURCE%>" rows="20" cols="80"><%=review.getSource()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 1 - Fetch the Source"
			onclick="appendToUrlAndFetch('step1')">
		<%}%><br> Author:<input type="text" name="<%=JspConstants.AUTHOR%>"
			value="<%=review.getAuthor()%>" size="50"> <br> Media
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
		<%}%>
		<br>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%>
		<input type=submit value="save">
		<%
		}
		%>
	</form>
	<hr>
	<form method=post
		action="<%=JspConstants.ADMINREVIEW%><%=review.getKeyLong().equals(0l) ? "" : "?" + JspConstants.ID + "=" + review.getKeyLong()%>">
		<h1>
			Review Details ID: <a
				href="<%=JspConstants.ADMINREVIEW%>?id=<%=review.getKeyLong()%>"><%=review.getReviewDetails().getKeyLong()%></a>
		</h1>
		<h1>
			Language:<%=lang.flagUnicode%><%=lang.name%></h1>
		<input type="hidden" name="<%=JspConstants.LANGUAGE%>"
			value="<%=lang.code%>"><input type=hidden name=id value="<%=review.getKeyLong()%>">
		<input type="hidden" name="<%=JspConstants.ID%>"
			value="<%=review.getKeyLong()%>"> Deleted:<input type="radio"
			name="<%=JspConstants.DELETED%>" value="true"
			<%=review.getReviewDetails().isDeleted() ? "checked" : ""%>>
		True <input type="radio" name="<%=JspConstants.DELETED%>"
			value="false"
			<%=!review.getReviewDetails().isDeleted() ? "checked" : ""%>>
		False<br> Name:<input type="text" name="<%=JspConstants.NAME%>"
			value="<%=review.getReviewDetails().getName()%>" size="30">
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 10 - Write Name"
			onclick="appendToUrlAndFetch('step10')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 6 - Translate Name"
			onclick="appendToUrlAndFetch('step6')">
		<%}%><br>Call:<input type="text" name="<%=JspConstants.CALL%>"
			value="<%=review.getReviewDetails().getCall()%>" size="30">
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 14 - Write Call to Action"
			onclick="appendToUrlAndFetch('step14')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 7 - Translate Call"
			onclick="appendToUrlAndFetch('step7')">
		<%}%><br> Description:
		<textarea name="<%=JspConstants.DESC%>" rows="12" cols="80"><%=review.getReviewDetails().getDesc()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 12 - Write Description"
			onclick="appendToUrlAndFetch('step12')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 2 - Translate Description"
			onclick="appendToUrlAndFetch('step2')">
		<%}%><br> Title:<input type="text" name="<%=JspConstants.TITLE%>"
			value="<%=review.getReviewDetails().getTitle()%>" size="50">
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button"
			value="Step 3 - Extract Title and Media from source"
			onclick="appendToUrlAndFetch('step3')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 5 - Translate Title"
			onclick="appendToUrlAndFetch('step5')">
		<%}%>
		<br> Summary:
		<textarea name="<%=JspConstants.SUMMARY%>" rows="20" cols="80"><%=review.getReviewDetails().getSummary()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 9 - Write a summary"
			onclick="appendToUrlAndFetch('step9')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 4 - Translate Summary"
			onclick="appendToUrlAndFetch('step4')">
		<%}%>
		<br> Introduction:
		<textarea name="<%=JspConstants.INTRODUCTIONDESC%>" rows="20"
			cols="80"><%=review.getReviewDetails().getIntroduction()%></textarea>
		<%
		if (Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 7 - Write the introduction"
			onclick="appendToUrlAndFetch('step7')">
		<%}%>
		<%
		if (!Language.ENGLISH.equals(lang)) {
		%><input type="button" value="Step 1 - Translate Introduction"
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
		%><input type="button" value="Step 8 - Translate Review"
			onclick="appendToUrlAndFetch('step8')">
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
		%><input type="button" value="Step 3 - Translate Conclusion"
			onclick="appendToUrlAndFetch('step3')">
		<%}%><br>
		<input type=hidden
			name=save value="save">
		<input type=submit value="save">
		<br><br>
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