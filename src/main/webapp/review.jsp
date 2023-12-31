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
if (null != review.getAuthor() && review.getAuthor().length() > 0) {
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
	content="<%=lang.reviews%>: <%= HtmlHelper.truncate(review.getReviewDetails().getTitle(), 130) %>">
<meta name="author" content="INCQ: <%=author.getTranslatedName()%>">
<meta name="keywords" content="<%=review.getReviewDetails().getMetaString()%>">

<!-- Bootstrap + SOLS main styles -->
<link rel="stylesheet" href="/assets/css/sols.css">
<title><%=lang.review%> <%=HtmlHelper.truncate(review.getReviewDetails().getName(), 60)%></title>
<script type="application/ld+json">
   {
	  "@context": "https://schema.org",
	  "@type": "Product",
	  "aggregateRating": {
	    "@type": "AggregateRating",
	    "ratingValue": "4.5",
	    "reviewCount": "21"
	  },
	  "description": "<%=review.getReviewDetails().getSummary()%>",
	  "name": "<%=review.getReviewDetails().getName()%>",
	  "image": "<%=review.getMediaList().size() > 0? review.getMediaList().get(0) : "" %>",
	  "url": "<%=review.getLink()%>",
	  "keywords": "<%=review.getReviewDetails().getMetaString()%>",
	  "positiveNotes": <%=HtmlHelper.convertLongJSON(review.getReviewDetails().getReviewBody())%>
	  },
	  "review": [
	    {
	      "@type": "<%=lang.reviews%>",
	      "author": "<%=review.getAuthor()%>",
	      "datePublished": "<%=Review.getFormatted(review.getUpdatedDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")%>",
	      "reviewBody": "<%=review.getReviewDetails().getDesc()%>",
	      "name": "<%=review.getReviewDetails().getDesc()%>",
	      "isFamilyFriendly": "true",
	      "reviewRating": {
	        "@type": "Rating",
	        "bestRating": "5",
	        "ratingValue": "<%=review.getScore()%>",
	        "worstRating": "1"
	      }
	    }
	  ]
	}
    </script>
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
				src="/assets/imgs/logo-sm.jpg" height="55" width="55" alt="INCQ">
			</a>
			<div class="d-none d-md-block">
				<h6 class="mb-0">
					<a href="https://www.facebook.com/INCQreviews/"
						class="px-2" target="_blank" aria-label="Facebook"><i
						class="ti-facebook"></i></a> <a
						href="https://twitter.com/incqReviews" aria-label="Twitter"
						class="px-2" target="_blank"><i class="ti-twitter"></i></a>
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
						target="_blank">Ad Author</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=JspConstants.ADMINREVIEWSEO%><%=review.getSlug()%>"
						target="_blank">Ad Review</a></li>
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
	<section>
		<div class="container">

			<div class="card bg-light">
				<div class="card-body px-4 pb-4 text-center">
					<h6>| <a 
						href="<%=JspConstants.INDEX%>" aria-label="><%=lang.home %>"><%=lang.home %></a> | <a 
						href="<%=JspConstants.CATEGORY +review.getCategory()%>" aria-label="<%=CaseControl.capFirstLetter(review.getCategory())%>"><%=CaseControl.capFirstLetter(review.getReviewDetails().getCategory())%></a> |</h6>
					<h4>
						<a href="<%=review.getLink()%>" target="_blank"
							aria-label="<%=review.getReviewDetails().getTitle()%>"><%=review.getReviewDetails().getTitle()%></a>
						by - <a href="<%=JspConstants.AUTHORSEO%><%=author.getName()%>"
							aria-label="<%=author.getTranslatedName()%>"><%=author.getTranslatedName()%></a>
					</h4>
					<a href="<%=review.getLink()%>" target="_blank"
						aria-label="<%=review.getReviewDetails().getTitle()%>"><img
						border="0" src="<%=review.getMediaList().size() > 0? review.getMediaList().get(0) : "" %>"
						alt="<%=review.getReviewDetails().getDesc()%>" height="250"
						width="250"></a>
					<h4><%=lang.introduction%></h4>
					<p><%=review.getReviewDetails().getIntroduction()%></p>
					<p><%=HtmlHelper.convertLongText(review.getReviewDetails().getReviewBody())%>
					<h4><%=lang.conclusion%></h4>
					<p><%=review.getReviewDetails().getConclusion()%></p>
					<h2>
						<a href="<%=review.getLink()%>"
							aria-label="<%=review.getReviewDetails().getCall()%>" target="_blank"><%=review.getReviewDetails().getCall()%></a>
					</h2>
					<br>
					<h1>
						<a href="<%=review.getLink()%>"
							aria-label="<%=lang.checkAmazon%>" target="_blank"><%=lang.checkAmazon%></a>
					</h1>
					<br>
					<h4>
						<a href="<%=JspConstants.AUTHORSEO%><%=author.getName()%>"
							aria-label="<%=author.getTranslatedName()%>"><%=author.getTranslatedName()%></a>
					</h4>
					<p><%=author.getShortDescription()%></p>
					<p>
						<%
						for (int x = 0; x < review.getTagsList().size(); x++) {
							String locTag = review.getTagsList().get(x);
							if(review.getTagsList().size() == review.getReviewDetails().getTagsList().size()){
								locTag = review.getReviewDetails().getTagsList().get(x);
							}
						%>
						| <a
							href="<%=JspConstants.CATEGORY%><%=review.getTagsList().get(x)%>"><%=locTag%></a>
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
					<a href="<%=JspConstants.HTTPS + JspConstants.INCQ%>"
						aria-label="Link to our Home Page"><img
						src="/assets/imgs/logo-sm.jpg" alt="INCQ" class="mb-0"
						height="100" width="100"></a>
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