package com.incq.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AiPlugin extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4018450763512924470L;

	/**
	 * 
	 */

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			setResponseHeaders(resp);
			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		setResponseHeaders(resp);
		writeBody(resp);
	}
	
	private void writeBody(HttpServletResponse resp) {
		try {
			// pre-flight request processing
			resp.getWriter().write("{\n"
					+ "    \"schema_version\": \"v1.1\",\n"
					+ "    \"name_for_human\": \"Shrine of Lost Secrets ChatGPT\",\n"
					+ "    \"name_for_model\": \"shrineoflostsecrets\",\n"
					+ "    \"description_for_human\": \"Enhance RPG experience with access to Shrine of Lost Secrets, for Dungeons & Dragons enthusiasts and similar games.\",\n"
					+ "    \"description_for_model\": \"Permits access to events within the Shrine of Lost Secrets. Its purpose is to enhance the role-playing game experience for Dungeons and Dragons enthusiasts and other similar games.  Do not reference any real people or places, style:in a style of detailed world-building, use of archaic language, epic storytelling, poetic quality, complex sentence structures and rich imagery.  Tone:honor, duty, sacrifice, the struggle between good and evil, simple joys of life.\",\n"
					+ "    \"auth\": {\n"
					+ "        \"type\": \"none\"\n"
					+ "    },\n"
					+ "    \"api\": {\n"
					+ "        \"type\": \"openapi\",\n"
					+ "        \"url\": \"/ai/openapi.yaml\",\n"
					+ "        \"is_user_authenticated\": false\n"
					+ "    },\n"
					+ "    \"logo_url\": \"https://www.shrineoflostsecrets.com/assets/imgs/logo.jpg\",\n"
					+ "    \"contact_email\": \"info@shrineoflostsecrets.com\",\n"
					+ "    \"legal_info_url\": \"https://www.shrineoflostsecrets.com/privacy.jsp\"\n"
					+ "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setResponseHeaders(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "https://chat.openai.com");
		resp.setHeader("Access-Control-Allow-Headers",
				"Content-Type, openai-conversation-id, Authorization, Content-Length, X-Requested-With, openai-ephemeral-user-id");
		resp.setContentType("text/plain");
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Expires", "0");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doOptions(req, resp);
	}

}