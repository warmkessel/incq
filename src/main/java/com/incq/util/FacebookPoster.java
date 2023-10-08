package com.incq.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class FacebookPoster {

	private static final String GRAPH_API_URL = "https://graph.facebook.com/v13.0/me/feed"; // 'v13.0' is just an
																							// example. Use the latest
																							// version.

	public static void main(String[] args) {
		String accessToken = "YOUR_ACCESS_TOKEN"; // obtained from Facebook after user authentication
		String message = "Hello, this is a test post!";

		postToFacebook(GRAPH_API_URL, message, accessToken);
	}

	public static void postToFacebook(String endpoint, String message, String token) {
		try {
			URL url = new URL(endpoint + "?message=" + message + "&access_token=" + token);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("Post was successful!");
			} else {
				System.out.println("Failed to post. Response code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//GraphRequest request = GraphRequest.newPostRequest(
//		  accessToken,
//		  "/InternetConsumerQuality/feed/",
//		  new JSONObject("{\"message\":\"Lets Do It!\"}"),
//		  new GraphRequest.Callback() {
//		    @Override
//		    public void onCompleted(GraphResponse response) {
//		      // Insert your code here
//		    }
//		});
//		request.executeAsync();