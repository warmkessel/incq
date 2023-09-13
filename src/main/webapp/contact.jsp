<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page import="com.incq.constants.*"%>
<%@ page import="javax.mail.*"%>
<%@ page import="javax.mail.internet.*"%>
<%@ page import="com.google.cloud.datastore.*"%>

<%
UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();

Language lang = Language.ENGLISH;

String langString = (String) request.getParameter(JspConstants.LANGUAGE);
if (null != langString && langString.length() > 0) {
	lang = Language.findByCode(langString);
}

//Get the subject and body parameters from the request
String subject = request.getParameter(JspConstants.SUBJECT);
String from = request.getParameter(JspConstants.FROM);
String body = request.getParameter(JspConstants.BODY);
String pageurl = request.getParameter(JspConstants.PAGEURL);


String emailResp = null;
if ((null != currentUser) && subject != null && body != null) {
    Properties props = new Properties();
    Session mailSession = Session.getDefaultInstance(props, null);

    try {
        // Create a new email message
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(currentUser.getEmail(), currentUser.getNickname()));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("comment@incq.com", "INCQ"));
        msg.setSubject("SOLS:" + subject);
        msg.setText(body + "\r" + pageurl);

        // Send the email
        Transport.send(msg);

        emailResp = "We got it!";
    } catch (AddressException e) {
        //out.println("Error: " + e.getMessage());
        emailResp = "Error: " + e.getMessage();

    } catch (MessagingException e) {
        emailResp = "Error: " + e.getMessage();
    }
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
<link rel="stylesheet" href="assets/css/sols.css?v=1">
<title>INCQ Reviews</title>

</head>
<body data-spy="scroll" data-target=".navbar" data-offset="40" id="">
	<!-- First Navigation -->
	<nav class="navbar nav-first navbar-dark bg-dark">
		<div class="container">
			<a class="navbar-brand" href="<%=JspConstants.INDEX%>?la=<%=lang.code%>"><img
				src="assets/imgs/logo-sm.jpg" height="55px" width="55px" alt="INCQ">
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
					<li class="nav-item"><a class="nav-link" href="<%=JspConstants.INDEX%>?la=<%=lang.code%>">Home</a></li>
					<li class="nav-item"><a class="nav-link" href="<%=JspConstants.AUTHORS%>?la=<%=lang.code%>">Authors</a></li>
					<li class="nav-item"><a class="nav-link" href="<%=JspConstants.CONTACT%>?la=<%=lang.code%>">Contact Us</a></li>
				</ul>
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><form action="<%=JspConstants.INDEX%>?la=<%=lang.code%>" method="get" id="languageForm">
            			<select name="la" onchange="document.getElementById('languageForm').submit();">
      				<% for (Language langEnum : Language.values()) {%>
      				        <option value="<%=langEnum.code%>" <%= langEnum.equals(lang) ? "selected" : "" %>><%=langEnum.flagUnicode%> <%=langEnum.name%></option>
					<%}%>
    </select></form></li>
				</ul>
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
			</div>
		</div>
	</nav>
	<!-- End Of Second Navigation -->
	<!-- Menu Section --> 
	<!-- Contact Section -->


<%
						if (currentUser != null) {
						%> 
	<!-- Contact Section -->
	<section class="bg-white">
		<div class="container">
			<div class="row align-items-center">
				<div class="col-md-6 d-none d-md-block">
					<img src="assets/imgs/logo.jpg"
						alt="INCQ Review Contact"
						class="w-100 rounded shadow">
				</div>
				<div class="col-md-6">
					<h3 class="section-title mb-5 text-center" id="reponse"></h3>
					<form action="<%= JspConstants.CONTACT %>" method="get"
						enctype="text/plain">
						<input type="hidden" name="pageurl"
							value="<%=request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString())%>">
						<div class="form-group">
							<input type="text" class="form-control" id="name"
								aria-describedby="emailHelp" placeholder="Subject"
								name="subject">
							<textarea class="form-control" id="message"
								aria-describedby="emailHelp"
								placeholder="Your comment or question" name="body" rows="4"
								cols="50"></textarea>
						</div>
						<button type="submit" class="btn btn-primary btn-block">Share
							your thoughts</button>
					 <small class="form-text text-muted mt-3">We appreciate
							your interest. Check our <a
							href="<%=JspConstants.PRIVACY%>">Privacy
								Policy</a>
						</small>

						<%if(null != emailResp){ %>
						<script>
							var modal = document.getElementById('reponse');
							modal.innerHTML = "<%=emailResp%>";
						</script>
						<%}%>
					</form>
				</div>
			</div>
		</div>
	</section>
	<!-- End OF Contact Section -->
	<%
						} else {
						%>
							<section class="bg-white">
		<div class="container">
			<div class="row align-items-center">
				<div class="col-md-6 d-none d-md-block">
					<img src="assets/imgs/contact.jpg"
						alt="INCQ Landing Page"
						class="w-100 rounded shadow">
				</div>
				<div class="col-md-6">				
					<h3 class="section-title mb-5 text-center" id="reponse">We'd love to hear your thoughts. Kindly login so we can receive your message.</h3>
					<a href="<%=userService.createLoginURL(JspConstants.CONTACT)%>"
						class="btn btn-primary btn-sm">Login</a> 
						<br>
						<small class="form-text text-muted mt-3">We appreciate
							your interest. Check our <a
							href="<%=JspConstants.PRIVACY%>">Privacy
								Policy</a>
						</small>

				</div>
			</div>
		</div>
	</section>
	<%} %>
	<!-- End OF Contact Section -->
	<!-- End of Menu Section -->
	<!-- Prefooter Section  -->
	<div
		class="py-4 border border-lighter border-bottom-0 border-left-0 border-right-0 bg-dark">
		<div class="container">
			<div
				class="row justify-content-between align-items-center text-center">
				<div class="col-md-3 text-md-left mb-3 mb-md-0">
					<a href="<%=JspConstants.INDEX%>?la=<%=lang.code%>"><img src="assets/imgs/logo.jpg" height=100px width=100px alt="INCQ"
						class="mb-0"></a>
				</div>
				<div class="col-md-9 text-md-right">
					<a href="<%=JspConstants.INDEX%>?la=<%=lang.code%>" class="px-3"><small class="font-weight-bold">Home</small></a>
					<a href="<%=JspConstants.AUTHORS%>?la=<%=lang.code%>" class="px-3"><small class="font-weight-bold">Authors</small></a>
					<a href="<%=JspConstants.CONTACT%>?la=<%=lang.code%>" class="pl-3"><small class="font-weight-bold">Contact</small></a>
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