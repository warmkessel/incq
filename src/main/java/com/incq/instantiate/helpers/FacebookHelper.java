package com.incq.instantiate.helpers;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.incq.constants.FacebookKey;
import com.incq.constants.JspConstants;
import com.incq.constants.Language;
import com.incq.entity.Review;

public class FacebookHelper {
	static Logger logger = Logger.getLogger(FacebookHelper.class.getName());

	private static final String URL_STRING = "https://graph.facebook.com/v18.0/InternetConsumerQuality";
	private static final String URL_STRING2 = "/feed/";

	public static void main(String[] args) {
		try {
			// Replace these placeholders with the actual data
			String message = "";
			String accessToken = "";
			String link = "";
			String urlString = "";

			postToFacebookGraphAPI(message, accessToken, link, urlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean postToFacebookGraphAPI(long key, Language lang) {
		Review review = new Review();
		review.loadEvent(key, lang, true);
		return postToFacebookGraphAPI(review);
	}

	public static boolean postToFacebookGraphAPI(Review review) {
		Language lang = review.getReviewDetails().getLanguage();
		return postToFacebookGraphAPI(buildMessage(review), FacebookKey.findByLang(lang).facebookKey, review.getLink(),
				getURLString(lang));
	}

	public static String buildMessage(Review review) {
		StringBuffer theReturn = new StringBuffer();
		try {
			Language lang = review.getReviewDetails().getLanguage();

			theReturn.append("https://").append(Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code)
					.append(JspConstants.INCQP).append(JspConstants.REVIEWSEO)
					.append(URLEncoder.encode(review.getSlug(), StandardCharsets.UTF_8.toString()));
			theReturn.append("\n");
			theReturn.append(review.getReviewDetails().getDesc());
			theReturn.append("\n");
			List<String> tags = review.getReviewDetails().getTagsList();
			
			if(tags.size() > 0) {
				theReturn.append("#").append(tags.get(0).trim().replaceAll(" ", "").replaceAll("\\+", ""));
			}
			if(tags.size() > 1) {
				theReturn.append(" | ").append("#").append(tags.get(1).trim().replaceAll(" ", "").replaceAll("\\+", ""));
			}
			if(tags.size() > 2) {
				theReturn.append(" | ").append("#").append(tags.get(2).trim().replaceAll(" ", "").replaceAll("\\+", ""));
			}
			
			theReturn.append("\n");
			theReturn.append("Read the Full Review");
			theReturn.append("\n");
			theReturn.append("https://").append(Language.ENGLISH.equals(lang) ? JspConstants.WWW : lang.code)
					.append(JspConstants.INCQP).append(JspConstants.REVIEWSEO)
					.append(URLEncoder.encode(review.getSlug(), StandardCharsets.UTF_8.toString()));
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Facebook Failed " + e.getLocalizedMessage());

		}

		return theReturn.toString();
	}

	public static String getURLString(Language lang) {
		if (Language.ENGLISH.equals(lang)) {
			return URL_STRING + URL_STRING2;
		} else {
			return URL_STRING + lang.code.toUpperCase() + URL_STRING2;

		}
	}

	public static boolean postToFacebookGraphAPI(String message, String accessToken, String link, String urlString) {
	    boolean theReturn = false;

	    try {
	        if (message != null && !message.isEmpty() && accessToken != null && !accessToken.isEmpty()) {

	            URL url = new URL(urlString);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	            // Set up the request headers
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	            conn.setDoOutput(true);

	            // Construct the post data
	            String postData = String.format(
	                    "message=%s&access_token=%s&link=%s",
	                    URLEncoder.encode(message, "UTF-8"),
	                    URLEncoder.encode(accessToken, "UTF-8"),
	                    URLEncoder.encode(link, "UTF-8")
	            );

	            // Send the request
	            try (OutputStream os = conn.getOutputStream()) {
	                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
	                os.write(input, 0, input.length);
	            }

	            int responseCode = conn.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                logger.log(Level.INFO, "Facebook Posted " + link);
	                theReturn = true;
	            } else {
	                logger.log(Level.SEVERE, "Facebook Failed " + link + " " + conn.getResponseMessage());
	            }

	        }
	    } catch (Exception e) {
	        logger.log(Level.SEVERE, "Facebook Failed " + link + " " + e.getLocalizedMessage());
	    }

	    return theReturn;
	}
}