<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="org.json.*"%>
<%@ page import="com.incq.util.*"%>
<%@ page import="com.incq.entity.*"%>
<%@ page import="com.incq.datastore.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page import="com.incq.constants.*"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<%
String userAgent = request.getHeader("User-Agent");
boolean isMobile = userAgent.matches(".*Mobile.*");
Language lang = Language.ENGLISH;

Author author = new Author();

UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();
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

if (null != id && id.length() > 0) {
	author.loadAuthor(new Long(id).longValue());
} else {
	String name = (String) request.getParameter(JspConstants.NAME);
	if (null == name || name.length() == 0) {
		// Get the full URL and query string
		String requestURL = request.getRequestURL().toString();
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();

		// Extract the part after "/author/"
		String prefix = contextPath + JspConstants.AUTHORSEO;
		if (requestURI.startsWith(prefix)) {
	name = requestURI.substring(prefix.length());
	// URL decode to convert any '+' to ' ' and '%' encoding to characters
	name = URLDecoder.decode(name, "UTF-8");
		}
	}

	if (null != name && name.length() > 0) {
		author.loadFromEntity(AuthorList.fetchAuthor(name, lang));
	} else {
		author.loadAuthor(AuthorConstants.DEFAULTID);
	}
}
long idLong = 0L;
try {
	idLong = Long.parseLong(id);
} catch (NumberFormatException e) {
	idLong = 0L; // Set value to 0 in case of NumberFormatException
}
%><!DOCTYPE html>
<html lang="<%=lang.code%>">
<head>
<!-- Google Tag Manager -->
<script>
	(function(w, d, s, l, i) {
		w[l] = w[l] || [];
		w[l].push({
			'gtm.start' : new Date().getTime(),
			event : 'gtm.js'
		});
		var f = d.getElementsByTagName(s)[0], j = d.createElement(s), dl = l != 'dataLayer' ? '&l='
				+ l
				: '';
		j.async = true;
		j.src = 'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
		f.parentNode.insertBefore(j, f);
	})(window, document, 'script', 'dataLayer', 'GTM-5CJH64QP');
</script>
<!-- End Google Tag Manager -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description"
	content="Reviews by: <%=author.getTranslatedName()%> <%= HtmlHelper.truncate(author.getShortDescription(), 130) %>">
<meta name="author" content="INCQ: <%=author.getTranslatedName()%>">

<!-- Bootstrap + SOLS main styles -->
<link rel="stylesheet" href="/assets/css/sols.css">
<title>INCQ <%=lang.reviews%> - <%=lang.authors %></title>
</head>
<body data-spy="scroll" data-target=".navbar" data-offset="40">
	<!-- Google Tag Manager (noscript) -->
	<noscript>
		<iframe src="https://www.googletagmanager.com/ns.html?id=GTM-5CJH64QP"
			height="0" width="0" style="display: none; visibility: hidden"></iframe>
	</noscript>
	<!-- First Navigation -->
	<nav class="navbar nav-first navbar-dark bg-dark">
		<div class="container">
			<a class="navbar-brand"
				href="<%=JspConstants.HTTPS + JspConstants.INCQ%>"
				aria-label="Link to our Home Page"> <img
				src="/assets/imgs/logo-sm.jpg" alt="INCQ">
			</a>
			<div class="d-none d-md-block">
				<h6 class="mb-0">
					<a href="https://www.facebook.com/INCQreviews/"
						class="px-2" target="_blank" aria-label="Facebook"><i
						class="ti-facebook"></i></a> <a
						href="https://twitter.com/incqReviews" class="px-2"
						aria-label="Twitter" target="_blank"><i class="ti-twitter"></i></a>
				</h6>
			</div>
		</div>
	</nav>
	<!-- End of First Navigation -->
	<!-- Second Navigation -->
	<nav
		class="nav-second navbar custom-navbar navbar-expand-sm navbar-dark bg-dark sticky-top">
		<div class="container">
			<button class="navbar-toggler ml-auto" type="button"
				data-toggle="collapse" data-target="#navbarSupportedContent"
				aria-controls="navbarSupportedContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav mr-auto">
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.INDEX%>" aria-label="><%=lang.home%>"><%=lang.home%></a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.AUTHORS%>" aria-label="><%=lang.authors%>"><%=lang.authors%></a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.CONTACT%>" aria-label="><%=lang.contactUs%>"><%=lang.contactUs%></a></li>
					<%
					if (userService.isUserLoggedIn() && userService.isUserAdmin()) {
					%>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.ADMININDEX%>" target="_blank">Admin</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.ADMINAUTHORSEO%><%=author.getName()%>"
						target="_blank">Admin Author</a></li>
					<%}%>
				</ul>
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><form id="languageForm">
							<select name="la" aria-label="Language" id="languageSelect">
								<%
								for (Language langEnum : Language.values()) {
								%>
								<option value="<%=langEnum.getCode()%>"
									<%=langEnum.equals(lang) ? "selected" : ""%>><%=langEnum.flagUnicode%>
									<%=langEnum.name%></option>
								<%
								}
								%>
							</select>
						</form> <script>
    					const languageSelect = document.getElementById("languageSelect");
    					languageSelect.addEventListener("change", function () {
        					const selectedValue = ("<%=Language.ENGLISH.code%>"==languageSelect.value?"www":languageSelect.value);
        					window.location.href = "<%=JspConstants.HTTPS%>" + selectedValue + "<%=JspConstants.INCQP + url.getPath()%>";
						});
						</script></li>
				</ul>
				<%
				if (currentUser != null) {
				%>
				<a href="<%=userService.createLogoutURL(JspConstants.INDEX)%>"
					class="btn btn-primary btn-sm"
					aria-label="<%=lang.welcome %> <%=currentUser.getNickname()%>"><%=lang.welcome %> <%=currentUser.getNickname()%></a>
				<%
				} else {
				%>
				<a href="<%=userService.createLoginURL(JspConstants.INDEX)%>"
					class="btn btn-primary btn-sm" aria-label="<%=lang.login %>"><%=lang.login %></a>
				<%}%>
			</div>
		</div>
	</nav>
	<!-- End Of Second Navigation -->
	<!-- Menu Section -->
	<section>
		<div class="container">
			<div class="card bg-light">
				<div class="card-body px-4 pb-4 text-center">
					<h1 class="section-title mb-6 text-center"><%=author.getTranslatedName()%></h1>
					<p><%=HtmlHelper.convertLongText(author.getLongDescription())%></p>
					<p>
						<%
						for (int x = 0; x < author.getTagsList().size(); x++) {
							String locTag = author.getTagsList().get(x);
							if(author.getTagsList().size() == author.getTagsTranslatedList().size()){
								locTag = author.getTagsTranslatedList().get(x);
							}
						%>
						| <a
							href="<%=JspConstants.CATEGORY%><%=author.getTagsList().get(x)%>"><%=locTag%></a>
						<%}%>
						|
					</p>
				</div>
			</div>
		</div>
	</section>
	<!-- End OF Menu Section -->
	<!-- Prefooter Section  -->
	<div
		class="py-4 border border-lighter border-bottom-0 border-left-0 border-right-0 bg-dark">
		<div class="container">
			<div
				class="row justify-content-between align-items-center text-center">
				<div class="col-md-3 text-md-left mb-3 mb-md-0">
					<a href="<%=JspConstants.HTTPS + JspConstants.INCQ%>"
						aria-label="Link to our Home Page"><img
						src="/assets/imgs/logo-sm.jpg" width="100" alt="INCQ" class="mb-0"></a>
				</div>
				<div class="col-md-9 text-md-right">
					<a href="<%=JspConstants.INDEX%>" aria-label="<%=lang.home %>" class="px-3"><small
						class="font-weight-bold"><%=lang.home %></small></a> <a
						href="<%=JspConstants.AUTHORS%>" aria-label="<%=lang.authors %>" class="px-3"><small
						class="font-weight-bold"><%=lang.authors %></small></a> <a
						href="<%=JspConstants.CONTACT%>" aria-label="<%=lang.contactUs %>" class="pl-3"><small
						class="font-weight-bold"><%=lang.contactUs %></small></a>
				</div>
			</div>
		</div>
	</div>
	<!-- End of PreFooter Section -->

	<!-- Page Footer -->
	<footer
		class="border border-dark border-left-0 border-right-0 border-bottom-0 p-4 bg-dark">
		<div class="container">
			<div class="row align-items-center text-center text-md-left">
				<div class="col">
					<p class="mb-0 small">
						&copy;
						<%=Constants.YEAR%>
						, <%=lang.arr %> - <%=lang.amazon %> -
						<%=Constants.VERSION%>
					</p>
				</div>
				<div class="d-none d-md-block">
					<h6 class="small mb-0">
						<a href="https://www.facebook.com/INCQreviews/"
							class="px-2" target="_blank" aria-label="Facebook"><i
							class="ti-facebook"></i></a> <a
							href="https://twitter.com/incqReviews" class="px-2"
							target="_blank" aria-label="Twitter"><i class="ti-twitter"></i></a>
					</h6>
				</div>
			</div>
		</div>

	</footer>
	<!-- End of Page Footer -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js"
		integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+"
		crossorigin="anonymous"></script>
</body>
</html>