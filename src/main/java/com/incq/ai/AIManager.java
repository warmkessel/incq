package com.incq.ai;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

import com.google.appengine.api.urlfetch.*;
import com.incq.constants.*;

public class AIManager {
    static Logger logger = Logger.getLogger(AIManager.class.getName());

	public static String removeUnusal(String input) {
		return input.replaceAll("\r", ". ").replaceAll("\n", "").replaceAll("'", "\'").replaceAll("\"", "\\\\\"");
	}
	public static String editText(String input, String instruction, String errorMessage) {
		return extactText(edit(input, instruction, true), errorMessage);
	}
	public static String editText3(String input, String instruction, String errorMessage) {
		return extactText(edit(input, instruction, false), errorMessage);
	}

	public static String edit(String input, String instruction, boolean current) {
		String theReturn = "";
		input = removeUnusal(input);

		try {

			// Encode the API key in Base64 format and set it as Authorization header
			String apiKey = AIKey.API_KEY;
			String auth = "Bearer " + apiKey;

//            curl https://api.openai.com/v1/chat/completions \
//            	  -H "Content-Type: application/json" \
//            	  -H "Authorization: Bearer $OPENAI_API_KEY" \
//            	  -d '{
//            	    "model": "gpt-3.5-turbo",
//            	    "messages": [{"role": "user", "content": "Hello!"}]
//            	  }'
//        
//        
			String endpoint = "https://api.openai.com/v1/chat/completions";
			
			String requestBody = "{\"model\":\"gpt-4\",\"messages\": [{\"role\": \"user\", \"content\": \"" + input + ". "+ instruction + "\"}]}";
			if(!current) {
				requestBody = "{\"model\":\"gpt-3.5-turbo\",\"messages\": [{\"role\": \"user\", \"content\": \"" + input + ". "+ instruction + "\"}]}";
			}
			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPRequest httpRequest = new HTTPRequest(new java.net.URL(endpoint), HTTPMethod.POST, FetchOptions.Builder.withDeadline(600d));

			//httpRequest.getFetchOptions().setDeadline(240000d);
			httpRequest.addHeader(new HTTPHeader("Content-Type", "application/json"));
			httpRequest.addHeader(new HTTPHeader("Authorization", auth));
			httpRequest.setPayload(requestBody.getBytes());
			logger.log(Level.INFO, "requestBody " + requestBody);

//            String endpoint = "https://api.openai.com/v1/edits";

			// Set the request body
//            String requestBody = "{\"model\":\"text-davinci-edit-001\",\"input\":\"" + input + "\",\"instruction\":\"" + instruction + "\"}";            
//            URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
//            HTTPRequest httpRequest = new HTTPRequest(new java.net.URL(endpoint), HTTPMethod.POST);
//            httpRequest.getFetchOptions().setDeadline(60000d);
//            httpRequest.addHeader(new HTTPHeader("Content-Type", "application/json"));
//            httpRequest.addHeader(new HTTPHeader("Authorization", auth));
//            httpRequest.setPayload(requestBody.getBytes());
//        	log.info("requestBody:" + requestBody);

			// Get the response
			HTTPResponse httpResponse = urlFetchService.fetch(httpRequest);
			theReturn = new String(httpResponse.getContent()).trim();
			logger.log(Level.INFO, "httpResponse " + theReturn);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to execute OpenAI API request: " + e.getMessage());
		}
		return theReturn;
	}

	public static String extactText(String jsonStr, String errorMessage) {
		String theReturn = "";
		try {
			if (jsonStr.startsWith("{")) {
				JSONObject json = new JSONObject(jsonStr);
//				JSONObject messages = json.getJSONObject("messages");
				JSONArray choices = json.getJSONArray("choices");
				JSONObject choice1 = choices.getJSONObject(0);
				JSONObject model = choice1.getJSONObject("message");
				String text = model.getString("content");

				
//				String text = choices.getJSONObject(0).getString("content");
				// String text = choices.getJSONObject(0).getString("text");
				theReturn = text;
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSONException: " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			if( theReturn.length() == 0) {
				theReturn = errorMessage;
			}		
		}
		return theReturn;
	}
}