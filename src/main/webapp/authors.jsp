<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.incq.util.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page import="com.incq.constants.*"%>
<%@ page import="com.incq.datastore.*"%>
<%@ page import="com.incq.entity.*"%>
<%@ page import="com.google.cloud.datastore.*"%>

<%
UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();
String requestUrl = request.getRequestURL().toString();
Language lang = Language.ENGLISH;

URL url = new URL(requestUrl);
String firstPart = url.getHost().split("\\.")[0];
String subDomain = url.getHost().split(JspConstants.SPLIT)[0];
if (0 == subDomain.length() || JspConstants.WWW.equals(subDomain) || JspConstants.LOCALHOST.equals(subDomain)) {
	String langString = (String) request.getParameter(JspConstants.LANGUAGE);
	if (null != langString && langString.length() > 0) {
		lang = Language.findByCode(langString);
	}
} 
else {
	lang = Language.findByCode(subDomain);
}
boolean all = true;
if(userService.isUserLoggedIn() && userService.isUserAdmin() && null != request.getParameter(JspConstants.ALL) && Boolean.TRUE.toString().equals(request.getParameter(JspConstants.ALL).toLowerCase())) {
	all = false;
}
ArrayList<Author>  theList = AuthorList.fetchAuthors(lang, all);

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
<meta name="description" content="<%=lang.authDesc%>">
<meta name="author" content="INCQ <%=lang.authors%>">
<!-- Bootstrap + SOLS main styles -->
<link rel="stylesheet" href="/assets/css/sols.css">
<title>INCQ <%=lang.reviews%> - <%= lang.authors%></title>

</head>
<body data-spy="scroll" data-target=".navbar" data-offset="40" >
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
				aria-label="Link to our Home Page" aria-label="<%=lang.linkHome %>"><img
				src="/assets/imgs/logo-sm.jpg" height="55" width="55" alt="INCQ">
			</a>
			<div class="d-none d-md-block">
				<h6 class="mb-0">
					<a href="https://www.facebook.com/INCQreviews/" class="px-2"
						target="_blank" aria-label="Facebook"><i class="ti-facebook"></i></a>
					<a href="https://twitter.com/incqReviews" class="px-2"
						target="_blank" aria-label="Twitter"><i class="ti-twitter"></i></a>
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
						href="<%=JspConstants.AUTHORS + "?"+ JspConstants.ALL+"=true" %>" target="_blank">All Authors</a></li>
					<%
					}
					%>
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
					aria-label="<%=lang.welcome%> <%=currentUser.getNickname()%>"><%=lang.welcome%>
					<%=currentUser.getNickname()%></a>
				<%
				} else {
				%>
				<a href="<%=userService.createLoginURL(JspConstants.INDEX)%>"
					class="btn btn-primary btn-sm" aria-label="<%=lang.login%>"><%=lang.login%></a>
				<%}%>
			</div>
		</div>
	</nav>
	<!-- End Of Second Navigation -->
	<!-- Menu Section -->
	<section class="has-img-bg" id="insite">
		<div class="container">
			<h6 class="section-subtitle text-center"><%=lang.someAuthors %></h6>
			<h1 class="section-title mb-6 text-center">INCQ Reviews</h1>
			<div class="card bg-light">
				<div class="card-body px-4 pb-4 text-center">
					<div class="row text-left">
						<%
						for (int x = 0; x < theList.size(); x++) {
						%>
						<div class="col-md-6 my-4">
							<div class="d-flex">
								<div class="flex-grow-1">
									<a
										href="<%=JspConstants.AUTHORSEO%><%=theList.get(x).getName()%>"
										class="pb-3 mx-3 d-block text-dark text-decoration-none border border-left-0 border-top-0 border-right-0"
										aria-label="<%=theList.get(x).getTranslatedName()%>"><%=theList.get(x).getTranslatedName()%></a>
									<p class="mt-1 mb-0">
										<a
											href="<%=JspConstants.AUTHORSEO%><%=theList.get(x).getName()%>"><%=HtmlHelper.convertLongText(theList.get(x).getShortDescription())%></a>
									</p>
								</div>
							</div>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- End of Menu Section -->
	<!-- Prefooter Section  -->
	<div
		class="py-4 border border-lighter border-bottom-0 border-left-0 border-right-0 bg-dark">
		<div class="container">
			<div
				class="row justify-content-between align-items-center text-center">
				<div class="col-md-3 text-md-left mb-3 mb-md-0">
					<a href="<%=JspConstants.HTTPS + JspConstants.INCQ%>"
						aria-label="Link to our Home Page"><img
						src="/assets/imgs/logo.jpg" height=100 width=100 alt="INCQ"
						class="mb-0"></a>
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
						,
						<%=lang.arr%>
						-
						<%=lang.amazon%>
						-
						<%=Constants.VERSION%>
						<%
						if (currentUser != null) {
						%>
						<a
							href="<%=userService.createLogoutURL(JspConstants.INDEX + "?la=" + lang)%>"
							class="btn btn-primary btn-sm">Welcome <%=currentUser.getNickname()%></a>
						<%
						} else {
						%>
						<a
							href="<%=userService.createLoginURL(JspConstants.INDEX + "?la=" + lang)%>"
							class="btn btn-primary btn-sm">Login/Register</a>
						<%}%>
					</p>
				</div>
				<div class="d-none d-md-block">
					<h6 class="small mb-0">
						<a href="https://www.facebook.com/INCQreviews/" class="px-2"
							aria-label="Facebook" target="_blank"><i class="ti-facebook"></i></a>
						<a href="https://twitter.com/incqReviews" class="px-2"
							aria-label="Twitter" target="_blank"><i class="ti-twitter"></i></a>
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