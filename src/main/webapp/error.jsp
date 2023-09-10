<%@ page import="java.util.Properties"%>
<%@ page import="javax.mail.Message"%>
<%@ page import="javax.mail.MessagingException"%>
<%@ page import="javax.mail.Session"%>
<%@ page import="javax.mail.Transport"%>
<%@ page import="javax.mail.internet.AddressException"%>
<%@ page import="javax.mail.internet.InternetAddress"%>
<%@ page import="javax.mail.internet.MimeMessage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.google.appengine.api.users.*"%>
<%@ page import="com.incq.constants.*"%>

<%@ page isErrorPage="true"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="Welcome to the Shrine of Lost Secrets">
<meta name="author" content="warmkessel">
<!-- font icons -->
<link rel="stylesheet"
	href="assets/vendors/themify-icons/css/themify-icons.css">
<!-- Bootstrap + SOLS main styles -->
<link rel="stylesheet" href="assets/css/sols.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Abandon ship! We're experiencing an error.</title>
</head>
<body data-spy="scroll" data-target=".navbar" data-offset="40" id="home">

	<!-- First Navigation -->
	<nav class="navbar nav-first navbar-dark bg-dark">
		<div class="container">
			<a class="navbar-brand"
				href="">
				<img src="assets/imgs/logo-sm.jpg" alt="Shrine of Lost Secrets">
			</a>
			<div class="d-none d-md-block">
				<h6 class="mb-0"></h6>
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
						href="<%=JspConstants.INDEX%>">Home</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<!-- End Of Second Navigation -->
	<section class="bg-white">
		<div class="container">
			<div class="row align-items-center">
				<div class="col-md-6">
					<h3 class="section-title mb-5 text-center">Abandon ship! We're
						experiencing an error.</h3>
					<p>Womp womp, an error has crashed the party.</p>
					<form id="error-report-form" method="post" action="/contact.jsp">
						<input type="hidden" name="subject" value="Error Subject">
						<input type="hidden" name="error" value="true">
						<textarea name="errorMessage" rows="4" cols="80"
							placeholder="Is there any helpful information regarding the cause of this error? No information is necessary."></textarea>
						<br> <small class="form-text text-muted mt-3"><a
							href="#" onclick="toggleErrorReport()">Details:</a></small><br>
						<div id="error-report" style="display: none">
							<textarea name="body" rows="20" cols="80">Email Body</textarea>
						</div>
						<p>
							<button type="submit" class="btn btn-primary btn-block">Oh
								no! The gremlins are at it again. Press here to report the error
								to send them packing!</button>
						</p>
					</form>

					<script type="text/javascript">
						function toggleErrorReport() {
							var errorReport = document
									.getElementById("error-report");
							if (errorReport.style.display === "none") {
								errorReport.style.display = "block";
							} else {
								errorReport.style.display = "none";
							}
						}
					</script>
				</div>
			</div>
		</div>
	</section>
	<!-- Prefooter Section  -->
	<div
		class="py-4 border border-lighter border-bottom-0 border-left-0 border-right-0 bg-dark">
		<div class="container">
			<div
				class="row justify-content-between align-items-center text-center">
				<div class="col-md-3 text-md-left mb-3 mb-md-0">
					<img src="assets/imgs/logo-sm.jpg" width="100"
						alt="INCQ" class="mb-0">
				</div>
				<div class="col-md-9 text-md-right">
					<a
						href="/"
						class="px-3"><small class="font-weight-bold">Reviews</small></a>
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
						<script>
							document.write(new Date().getFullYear())
						</script>
						, INCQ All rights reserved
						<%=Constants.VERSION%>
					</p>
				</div>
				<div class="d-none d-md-block"></div>
			</div>
		</div>

	</footer>
	<!-- End of Page Footer -->
</body>
</html>

