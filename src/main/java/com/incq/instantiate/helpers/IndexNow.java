package com.incq.instantiate.helpers;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.incq.constants.Constants;
import com.incq.constants.JspConstants;
import com.incq.constants.Language;
import com.incq.constants.SEOConstants;
import com.incq.entity.Review;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndexNow {
	static Logger logger = Logger.getLogger(IndexNow.class.getName());


	public static void postIndexNow(long reviewId, Language lang) {
		Review review = new Review();
		review.loadEvent(reviewId, lang);
		postIndexNow(review);
	}

	public static void postIndexNow(Review review) {
		try {

				String url = buildIndexNowUrl(review);		
				post(SEOConstants.INDEXNOWURL + url);
				post(SEOConstants.BINGURL + url);
				post(SEOConstants.NAVERURL + url);
				post(SEOConstants.SEZNAM + url);
				post(SEOConstants.YANDEX + url);
				post(SEOConstants.GOOGLE + review.getReviewDetails().getLanguage().getCode() + SEOConstants.GOOGLE2);

		} catch (Exception e) {
	        logger.log(Level.SEVERE, "IndexNow Failed " + e.getLocalizedMessage());

		}
	}
	private static void post(String url) {
		int responseCode = sendHttpRequest(url);

		if (responseCode == 200) {
			logger.log(Level.INFO, "Successfully indexed URL: " + url);
		} else {
			logger.log(Level.SEVERE, "Failed to index URL: " + url + ", Response Code: " + responseCode);
		}
	}
	

	private static String buildIndexNowUrl(Review review) throws UnsupportedEncodingException {
		String langCode = Language.ENGLISH.equals(review.getReviewDetails().getLanguage()) ? "www" : review.getReviewDetails().getLanguage().code;
		String slug = URLEncoder.encode(review.getSlug(), StandardCharsets.UTF_8.toString());
		return JspConstants.HTTPS + langCode + JspConstants.INCQP + JspConstants.REVIEWSEO + slug + "&key="
				+ SEOConstants.INDEXNOW;
	}

	private static int sendHttpRequest(String url) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse response = httpClient.execute(httpGet);
			return response.getStatusLine().getStatusCode();
		} catch (IOException e) {
			e.printStackTrace();
			return -1; // An error occurred
		}
	}
}