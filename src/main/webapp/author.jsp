<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="org.json.*"%>
<%@ page import="com.incq.entity.*"%>
<%@ page import="com.incq.datastore.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page import="com.incq.constants.*"%>

<%
String userAgent = request.getHeader("User-Agent");
boolean isMobile = userAgent.matches(".*Mobile.*");

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
else{
	String name = (String) request.getParameter(JspConstants.NAME);
	if (null != name && name.length() > 0) {
		author.loadFromEntity(AuthorList.fetchAuthor(name, Language.findByCode(lang)));
	}
	else{
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
<html lang="en">
<head>
<!-- Google tag (gtag.js) -->
<script async=true src="https://www.googletagmanager.com/gtag/js?id=G-PMGYN3L4QF"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-PMGYN3L4QF');
</script>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="Welcome to the INCQ Reviews">
<meta name="author" content="Incq.com">

<!-- Bootstrap + SOLS main styles -->
<link rel="stylesheet" href="assets/css/sols.css">
<title>INCQ Reviews - Authors</title>
</head>
<body data-spy="scroll" data-target=".navbar" data-offset="40" id="home">
	<!-- First Navigation -->
	<nav class="navbar nav-first navbar-dark bg-dark">
		<div class="container">
			<a class="navbar-brand" href="<%=JspConstants.INDEX%>"> <img
				src="assets/imgs/logo-sm.jpg" alt="INCQ">
			</a>
			<div class="d-none d-md-block">
				<h6 class="mb-0">
					<a href="https://www.facebook.com/groups/915527066379136/"
						class="px-2" target="_blank"><i class="ti-facebook"></i></a> <a
						href="https://twitter.com/shrinesecrets" class="px-2"
						target="_blank"><i class="ti-twitter"></i></a>
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
					<li class="nav-item"><a class="nav-link" href="<%=JspConstants.INDEX%>">Home</a></li>
					<li class="nav-item"><a class="nav-link" href="<%=JspConstants.AUTHORS%>">Authors</a></li>
					<li class="nav-item"><a class="nav-link" href="<%=JspConstants.CONTACT%>">Contact
							Us</a></li>
				</ul>
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><form action="<%=JspConstants.REVIEW%>" method="get" id="languageForm">
            			<select name="la" onchange="document.getElementById('languageForm').submit();">
      				<% for (Language langEnum : Language.values()) {%>
      				        <option value="<%=langEnum.code%>" <%= langEnum.code.equals(lang) ? "selected" : "" %>><%=langEnum.flagUnicode%> <%=langEnum.name%></option>
					<%}%>
    </select><input type=hidden name=id value="<%=idLong%>"></form></li>
				</ul>
			</div>
		</div>
	</nav>
	<!-- End Of Second Navigation -->
	<section id="">
		<div class="container">

			<div class="card bg-light">
				<div class="card-body px-4 pb-4 text-center">
						<h4><%= author.getName()%></h4>
						<p>
						<%= author.getLongDescription() %>
						</p>
				</div>
			</div>
		</div>
	</section>
	<!-- End OF Pray Section -->
	<!-- Prefooter Section  -->
	<div
		class="py-4 border border-lighter border-bottom-0 border-left-0 border-right-0 bg-dark">
		<div class="container">
			<div
				class="row justify-content-between align-items-center text-center">
				<div class="col-md-3 text-md-left mb-3 mb-md-0">
					<a href="<%=JspConstants.INDEX%>"><img src="assets/imgs/logo-sm.jpg" width="100" alt="INCQ"
						class="mb-0"></a>
				</div>
				<div class="col-md-9 text-md-right">
					<a href="<%=JspConstants.INDEX%>" class="px-3"><small class="font-weight-bold">Home</small></a>
					<a href="<%=JspConstants.AUTHORS%>" class="px-3"><small class="font-weight-bold">Authors</small></a>
					<a href="<%=JspConstants.CONTACT%>" class="pl-3"><small class="font-weight-bold">Contact</small></a>
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
						, INCQ All rights reserved - As an Amazon Associate we earn from qualifying purchases. - <%=Constants.VERSION%>
					</p>
				</div>
				<div class="d-none d-md-block">
					<h6 class="small mb-0">
						<a href="https://www.facebook.com/groups/915527066379136/"
							class="px-2" target="_blank"><i class="ti-facebook"></i></a> <a
							href="https://twitter.com/shrinesecrets" class="px-2"
							target="_blank"><i class="ti-twitter"></i></a>
					</h6>
				</div>
			</div>
		</div>

	</footer>
	<!-- End of Page Footer -->
</body>
</html>