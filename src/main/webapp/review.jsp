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

<%
UserService userService = UserServiceFactory.getUserService();
User currentUser = userService.getCurrentUser();

Review review = new Review();
Author author = new Author();
Language lang = Language.ENGLISH;

String requestUrl = request.getRequestURL().toString();
URL url = new URL(requestUrl);
String subDomain = url.getHost().split(JspConstants.SPLIT)[0];
if(0 == subDomain.length() || JspConstants.WWW.equals(subDomain)|| JspConstants.LOCALHOST.equals(subDomain)){
	String langString = (String) request.getParameter(JspConstants.LANGUAGE);
	if (null != langString && langString.length() > 0) {
		lang = Language.findByCode(langString);
	}
}
else{
	lang = Language.findByCode(subDomain);
}

String id = (String) request.getParameter(JspConstants.ID);
if (null != id && id.length() > 0) {
	review.loadEvent(new Long(id).longValue(), lang);
} else {
	// Get the full URL and query string
	String requestURL = request.getRequestURL().toString();
	String requestURI = request.getRequestURI();
	String contextPath = request.getContextPath();

	String prefix = contextPath + JspConstants.REVIEWSEO;
	if (requestURI.startsWith(prefix)) {
		String slug = requestURI.substring(prefix.length());
		// URL decode to convert any '+' to ' ' and '%' encoding to characters
		slug = URLDecoder.decode(slug, "UTF-8");
		review.loadFromEntity(ReviewList.fetchReview(slug), lang);
	}
}
if(null != review.getAuthor() && review.getAuthor().length() > 0){
	author.loadFromEntity(AuthorList.fetchAuthor(review.getAuthor(), lang));
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
<!-- Google tag (gtag.js) -->
<script async=true
	src="https://www.googletagmanager.com/gtag/js?id=G-PMGYN3L4QF"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag() {
		dataLayer.push(arguments);
	}
	gtag('js', new Date());

	gtag('config', 'G-PMGYN3L4QF');
</script>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description"
	content="Reviews:<%=review.getReviewDetails().getDesc()%>">
<meta name="author" content="INCQ: <%=review.getAuthor()%>">
<meta name="keywords" content="<%=review.getMetaString()%>">

<!-- Bootstrap + SOLS main styles -->
<link rel="stylesheet" href="/assets/css/sols.css">
<title><%=review.getReviewDetails().getName()%> review</title>
<script type="application/ld+json">
   {
	  "@context": "https://schema.org",
	  "@type": "Product",
	  "aggregateRating": {
	    "@type": "AggregateRating",
	    "ratingValue": "3.5",
	    "reviewCount": "11"
	  },
	  "description": "<%=review.getReviewDetails().getSummary()%>",
	  "name": "<%=review.getReviewDetails().getName()%>",
	  "image": "<%=review.getMediaList().get(0)%>",
	  "url": "<%=review.getMetaString()%>",
	  "keywords": "<%=review.getLink()%>",
	  "positiveNotes": <%=HtmlHelper.convertLongJSON(review.getReviewDetails().getReviewBody())%>
	  },
	  "review": [
	    {
	      "@type": "Review",
	      "author": "<%=review.getAuthor()%>",
	      "datePublished": "<%=Review.getFormatted(review.getUpdatedDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")%>",
	      "reviewBody": "<%=review.getReviewDetails().getDesc()%>",
	      "name": "<%=review.getReviewDetails().getDesc()%>",
	      "isFamilyFriendly": "true",
	      "reviewRating": {
	        "@type": "Rating",
	        "bestRating": "5",
	        "ratingValue": "4.5",
	        "worstRating": "1"
	      }
	    }
	  ]
	}
    </script>
</head>
<body data-spy="scroll" data-target=".navbar" data-offset="40" id="home">
	<!-- First Navigation -->
	<nav class="navbar nav-first navbar-dark bg-dark">
		<div class="container">
			<a class="navbar-brand" href="<%=JspConstants.HTTPS + JspConstants.INCQ%>"> <img
				src="/assets/imgs/logo-sm.jpg" alt="INCQ">
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
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.INDEX%>">Home</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.AUTHORS%>">Authors</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.CONTACT%>">Contact Us</a></li>
					<%
					if (userService.isUserLoggedIn() && userService.isUserAdmin()) {
					%>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.ADMININDEX%>" target="_blank">Admin</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.ADMINAUTHORSEO%><%=author.getName()%>" target="_blank">Ad Author</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.ADMINREVIEWSEO%><%=review.getSlug()%>" target="_blank">Ad Review</a></li>
					<%}%>
				</ul>
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><form action="<%=JspConstants.REVIEWSEO%><%=review.getSlug()%>"
							method="get" id="languageForm">
							<select name="la"
								onchange="document.getElementById('languageForm').submit();">
								<%
								for (Language langEnum : Language.values()) {
								%>
								<option value="<%=langEnum.code%>"
									<%=langEnum.equals(lang) ? "selected" : ""%>><%=langEnum.flagUnicode%>
									<%=langEnum.name%></option>
								<%}%>
							</select>
						</form></li>
				</ul>
				<%
				if (currentUser != null) {
				%>
				<a
					href="<%=userService.createLogoutURL(JspConstants.INDEX)%>"
					class="btn btn-primary btn-sm">Welcome <%=currentUser.getNickname()%></a>
				<%
				} else {
				%>
				<a
					href="<%=userService.createLoginURL(JspConstants.INDEX)%>"
					class="btn btn-primary btn-sm">Login/Register</a>
				<%}%>
			</div>
		</div>
	</nav>
	<!-- End Of Second Navigation -->
	<section id="">
		<div class="container">

			<div class="card bg-light">
				<div class="card-body px-4 pb-4 text-center">
					<h4>
						<a href="<%=review.getLink()%>" target="_blank"><%=review.getReviewDetails().getTitle()%></a>
						by - <a
							href="<%=JspConstants.AUTHORSEO%><%=author.getName()%>"><%=author.getName()%></a>
					</h4>
					<img border="0" src="<%=review.getMediaList().get(0)%>"
						alt="<%=review.getReviewDetails().getDesc()%>">
					<h4>Introduction</h4>
					<p><%=review.getReviewDetails().getIntroduction()%></p>
					<p><%=HtmlHelper.convertLongText(review.getReviewDetails().getReviewBody())%>
					<h4>Conclusion</h4>
					<p><%=review.getReviewDetails().getConclusion()%></p>
					<h4>
						<a href="<%=JspConstants.AUTHOR%>?id=<%=author.getKeyLong()%>">Author
							- <%=author.getName()%></a>
					</h4>
					<p><%=author.getShortDescription()%></p>
					<p>
						<%
						for (int x = 0; x < review.getTagsList().size(); x++) {
						%>
						| <a href=""><%=review.getTagsList().get(x)%></a>
						<%}%>
						|
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
					<a href="<%=JspConstants.HTTPS + JspConstants.INCQ%>"><img
						src="/assets/imgs/logo-sm.jpg" width="100" alt="INCQ" class="mb-0"></a>
				</div>
				<div class="col-md-9 text-md-right">
					<a href="<%=JspConstants.INDEX%>" class="px-3"><small
						class="font-weight-bold">Home</small></a> <a
						href="<%=JspConstants.AUTHORS%>" class="px-3"><small
						class="font-weight-bold">Authors</small></a> <a
						href="<%=JspConstants.CONTACT%>" class="pl-3"><small
						class="font-weight-bold">Contact</small></a>
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
						, INCQ All rights reserved - As an Amazon Associate we earn from
						qualifying purchases. -
						<%=Constants.VERSION%>
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
		<script src="/assets/vendors/jquery/jquery-3.4.1.js"></script>
	<script src="/assets/vendors/bootstrap/bootstrap.bundle.js"></script>

	<!-- bootstrap affix -->
	<script src="/assets/vendors/bootstrap/bootstrap.affix.js"></script>
	
</body>
</html>