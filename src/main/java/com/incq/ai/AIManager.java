package com.incq.ai;

//import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

import com.google.appengine.api.urlfetch.*;
import com.incq.constants.*;
import com.incq.exception.IncqServletException;
import com.incq.util.HtmlHelper;

public class AIManager {
	public static final String SORRY = "sorry";
	public static final String AIMODEL = "ai model";
	public static final String AIINVALIDRESPOSNE = "The AI response was invalid";

	static Logger logger = Logger.getLogger(AIManager.class.getName());

	public static String removeUnusal(String input) {
		return input.replaceAll("\r", ". ").replaceAll("\n", "").replaceAll("'", "\'").replaceAll("\"", "\\\\\"");
	}

	public static String editTextChunk(String input, String instruction, String style, String errorMessage)
			throws IncqServletException {
		int numOfTries = 10;
		StringBuffer theReturn = new StringBuffer();

		while (numOfTries > 0) {
			try {
				String[] inputArr = input.split("\r\n");
				for (int x = 0; x < inputArr.length; x++) {
					if (inputArr[x].length() > 0) {
						theReturn.append(extactText(edit(inputArr[x], instruction, style, true), errorMessage))
								.append(HtmlHelper.CRLF);
					}
				}
				numOfTries = 0;
			} catch (IncqServletException incq) {
				logger.log(Level.SEVERE, "Failed to execute editTextChunk OpenAI API request numOfTries " + numOfTries);
				numOfTries = numOfTries - 1;
			}
		}
		return theReturn.toString();
	}

	public static String editText(String input, String instruction, String style, String errorMessage)
			throws IncqServletException {
		return extactText(edit(input, instruction, style, true), errorMessage);
	}

	public static String editText3(String input, String instruction, String style, String errorMessage)
			throws IncqServletException {
		return extactText(edit(input, instruction, style, false), errorMessage);
	}

	public static String editText(String input, String instruction, String errorMessage) throws IncqServletException {
		return extactText(edit(input, instruction, "", true), errorMessage);
	}

	public static String editText3(String input, String instruction, String errorMessage) throws IncqServletException {
		return extactText(edit(input, instruction, "", false), errorMessage);
	}

	public static String edit(String input, String instruction, String style, boolean current)
			throws IncqServletException {
		String theReturn = "";
		// input = removeUnusual(input);

		if (style.length() > 0) {
			style = AIConstants.AIMANAGERSTYLE + style;
		}

		try {
			String apiKey = AIKey.API_KEY;
			String auth = "Bearer " + apiKey;
			String endpoint = "https://api.openai.com/v1/chat/completions";

			JSONArray messagesArray = new JSONArray();
			JSONObject messageObject = new JSONObject();
			messageObject.put("role", "user");
			messageObject.put("content", instruction + " " + style + " " + input);
			messagesArray.put(messageObject);

			JSONObject mainObject = new JSONObject();
			mainObject.put("model", current ? "gpt-4" : "gpt-3.5-turbo");
			mainObject.put("messages", messagesArray);

			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
			HTTPRequest httpRequest = new HTTPRequest(new java.net.URL(endpoint), HTTPMethod.POST,
					FetchOptions.Builder.withDeadline(600d));

			httpRequest.addHeader(new HTTPHeader("Content-Type", "application/json"));
			httpRequest.addHeader(new HTTPHeader("Authorization", auth));
			httpRequest.setPayload(mainObject.toString().getBytes());

			logger.log(Level.INFO, "requestBody " + mainObject.toString());

			HTTPResponse httpResponse = urlFetchService.fetch(httpRequest);
			theReturn = new String(httpResponse.getContent()).trim();

			logger.log(Level.INFO, "httpResponse " + theReturn);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to execute OpenAI API request: " + e.getMessage());
			throw new IncqServletException(e);
		}

		return theReturn;
	}

	public static String extactText(String jsonStr, String errorMessage) throws IncqServletException {
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
		} finally {
			if (theReturn.length() == 0) {
				logger.log(Level.SEVERE, "AIManager: " + jsonStr);

				theReturn = errorMessage;
			}
		}
		if (checkInvalidResponse(theReturn)) {
			throw new IncqServletException(AIINVALIDRESPOSNE + theReturn);
		}
		return theReturn;
	}

	private static boolean checkInvalidResponse(String response) {
		String responseLower = response.toLowerCase();
		return (responseLower.contains(SORRY) || responseLower.contains(AIMODEL));
	}
}