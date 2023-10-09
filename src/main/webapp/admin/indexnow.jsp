<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<%@ page import="com.google.cloud.datastore.*"%>
<%@ page import="com.incq.constants.*"%>
<%@ page import="com.incq.datastore.*"%>
<%@ page import="com.incq.entity.*"%>
<%@ page import="org.json.JSONObject"%> <!-- Import the JSON library -->

<!DOCTYPE html>
<html>
<head>
    <title>Iterate and Submit URLs</title>
    <!-- Include jQuery library -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

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
    String requestURI = request.getRequestURI();
    String contextPath = request.getContextPath();
    String prefix = contextPath + JspConstants.ADMININDEXNOW;
    if (requestURI.startsWith(prefix)) {
        String slug = requestURI.substring(prefix.length());
        slug = URLDecoder.decode(slug, "UTF-8");
        review.loadFromEntity(ReviewList.fetchReview(slug), lang);
    }
}

LocalDate currentDate = LocalDate.now();

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String formattedDate = currentDate.format(formatter);
%>

<!-- Add a form with a "Go" button to trigger URL submission -->
<form id="submitForm">
    <input type="submit" value="Go">
</form>

<!-- Script to handle form submission -->
<script>
    $(document).ready(function () {
        var urls = [
            <% for (Language langEnum : Language.values()) {
                if (null != ReviewDetailsList.fetchEventDetails(review.getKeyLong(), langEnum, false)) { %>
                    "<%= JspConstants.HTTPS + (Language.ENGLISH.equals(langEnum) ? JspConstants.WWW : langEnum.code) + JspConstants.INCQP + JspConstants.REVIEWSEO + URLEncoder.encode(review.getSlug(), StandardCharsets.UTF_8.toString()) %>",
                <% }
            } %>
        ];

        var currentIndex = 0;

        // Function to submit the URL and handle response
        function submitUrl() {
            if (currentIndex < urls.length) {
                var url = urls[currentIndex];
                $.ajax({
                    type: "GET",
                    url: "https://www.bing.com/indexnow?url=" + url + "&key=88500517616d495992167e3b11892346",
                    success: function (response) {
                        if (response.status == 200) {
                            // Continue to the next URL
                            currentIndex++;
                            submitUrl();
                        } else {
                            console.log("Failed to submit URL: " + url);
                        }
                    },
                    error: function () {
                        console.log("Error submitting URL: " + url);
                    }
                });
            }
        }

        // Submit the URLs when the "Go" button is clicked
        $("#submitForm").submit(function (event) {
            event.preventDefault();
            currentIndex = 0; // Reset the index
            submitUrl();
        });
    });
</script>

</body>
</html>