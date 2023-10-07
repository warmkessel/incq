<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.incq.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.incq.util.*"%>
<%@ page import="com.incq.instantiate.*"%>
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
List<Author> theList = AuthorList.fetchAuthorsSiteMap(Language.ENGLISH);
%>
</head>
<body>
	<form>
		<table>
			<%
			for (AuthorStep step : AuthorStep.values()) {
				if (AuthorStep.FAIL != step) {
			%>
			<tr>
				<td><%=step.name%>: <%=step.desc%></td>
				<td><input name="<%=JspConstants.STEP%>" type=checkbox
					value="<%=step.name%>"></td>
			</tr>
			<%
			}
			}
			%>
		</table>


		<table>
			<%
			for (Author author : theList) {
			%>
			<tr>
				<td><a
					href="<%=JspConstants.ADMINAUTHOR%>?<%=JspConstants.ID%>=<%=author.getKeyLong()%>">
						<%=author.getName()%>
				</a></td>
				<td><input name="<%=JspConstants.AUTHORLIST%>" type=checkbox
					value="<%=author.getName()%>"></td>
			</tr>
			<%
			}
			%>
		</table>

		<table>
			<%
			for (Language langEnum : Language.values()) {
			%>
			<tr>
				<td><a
					href="<%=JspConstants.ADMINAUTHOR%>?<%=JspConstants.ID%>=&<%=JspConstants.LANGUAGE%>=<%=langEnum.code%>">
						<%=langEnum.name%>
				</a></td>
				<td><input name="<%=JspConstants.LANGUAGELIST%>" type=checkbox
					value="<%=langEnum.code%>"></td>
			</tr>
			<%
			}
			%>
		</table>
		<input type="button" id="submitButton" value="Submit" onclick="submitCheckedValues()">
	</form>

	<script>
    async function submitCheckedValues() {
        // Disable the submit button
        const submitButton = document.getElementById("submitButton");
        submitButton.disabled = true;

        const stepElements = document.querySelectorAll('input[name="<%=JspConstants.STEP%>"]:checked');
        const authorListElements = document.querySelectorAll('input[name="<%=JspConstants.AUTHORLIST%>"]:checked');
        const listElements = document.querySelectorAll('input[name="<%=JspConstants.LANGUAGELIST%>"]:checked');

        for (const author of authorListElements) {
            for (const list of listElements) {
                for (const step of stepElements) {
                    await appendToUrlAndFetch(step.value, author.value, list.value);
                }
            }
        }
        document.querySelector("form").reset();

        // Re-enable the submit button
        submitButton.disabled = false;
    }

    async function appendToUrlAndFetch(step, author, lang) {
        const newUrl = "/tasks/expandAuthor?cont=false&<%=JspConstants.STEP%>=";
        try {
            console.info(newUrl + step + "&<%=JspConstants.ID%>=" + author +"&<%=JspConstants.LANGUAGE%>=" + lang );
            
        	const response = await fetch(newUrl + step + "&<%=JspConstants.NAME%>=" + author +"&<%=JspConstants.LANGUAGE%>=" + lang );
            if (!response.ok) {
                console.error("Error fetching the URL:", response.status, response.statusText);
            }
        } catch (error) {
            console.error("There was a problem with the fetch operation:", error);
        }
    }
	</script>
</body>