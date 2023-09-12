package com.incq.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Openapi extends HttpServlet {
	/**
	 * 
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -3942463413686495382L;

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
			resp.getWriter().write("openapi: 3.1.0\n"
					+ "info:\n"
					+ "  title: Shrine of Lost Secrets\n"
					+ "  description: This plugin enables ChatGPT to access the events within the Shrine of Lost Secrets, aiming to enhance the role-playing game experience for enthusiasts of games like Dungeons and Dragons.\n"
					+ "  version: v1.0.0\n"
					+ "servers:\n"
					+ "  - url: https://www.shrineoflostsecrets.com\n"
					+ "  - url: http://localhost:8080\n"
					+ "paths:\n"
					+ "  /ai/help:\n"
					+ "    get:\n"
					+ "      operationId: getHelp\n"
					+ "      summary: Provide help information on how get started using the pluin.\n"
					+ "      responses:\n"
					+ "        \"200\":\n"
					+ "          description: OK\n"
					+ "          content:\n"
					+ "            text/plain:\n"
					+ "              schema:\n"
					+ "                type: string\n"
					+ "                description: Suggestions on how to setup and use the plugin with the Shrine of Lost Secrets.\n"
					+ "  /ai/event:\n"
					+ "    get:\n"
					+ "      operationId: events\n"
					+ "      summary: Get the summary of events\n"
					+ "      parameters:\n"
					+ "      - in: query\n"
					+ "        name: tags\n"
					+ "        schema:\n"
					+ "          type: string\n"
					+ "        required: true\n"
					+ "        description: Space separated tags used to filter the events.\n"
					+ "      - in: query\n"
					+ "        name: list\n"
					+ "        schema:\n"
					+ "          type: boolean\n"
					+ "        required: false\n"
					+ "        default: false\n"
					+ "        description: Flag used to only list events titles.\n"
					+ "      - in: query\n"
					+ "        name: count\n"
					+ "        schema:\n"
					+ "          type: integer\n"
					+ "        required: false\n"
					+ "        default: 10\n"
					+ "        description: The number of items to return.\n"
					+ "      responses:\n"
					+ "        \"200\":\n"
					+ "          description: OK\n"
					+ "          content:\n"
					+ "            application/json:\n"
					+ "              schema:\n"
					+ "                $ref: '#/components/schemas/GetEventResponse'\n"
					+ "  /ai/date:\n"
					+ "    get:\n"
					+ "      operationId: date\n"
					+ "      summary: To restrict the range of events to be returned, you can manipulate the starting and ending dates accordingly.\n"
					+ "      parameters:\n"
					+ "        - in: query\n"
					+ "          name: start\n"
					+ "          schema:\n"
					+ "            type: integer\n"
					+ "          required: false\n"
					+ "          description: The number of days to move forward (or backward use the negative number) the start date\n"
					+ "        - in: query\n"
					+ "          name: end\n"
					+ "          schema:\n"
					+ "            type: integer\n"
					+ "          required: false\n"
					+ "          description: The number of days to move forward (or backward use the negative number) the end date\n"
					+ "        - in: query\n"
					+ "          name: reset\n"
					+ "          schema:\n"
					+ "            type: string\n"
					+ "          required: false\n"
					+ "          description: Set to true to reset\n"
					+ "        - in: query\n"
					+ "          name: email\n"
					+ "          schema:\n"
					+ "            type: string\n"
					+ "          required: false\n"
					+ "          description: My email address\n"
					+ "      responses:\n"
					+ "        \"200\":\n"
					+ "          description: OK\n"
					+ "          content:\n"
					+ "            application/json:\n"
					+ "              schema:\n"
					+ "                $ref: '#/components/schemas/GetDateResponse'              \n"
					+ "components:\n"
					+ "  schemas:\n"
					+ "    Event:\n"
					+ "      type: object\n"
					+ "      properties:\n"
					+ "        eventTitle:\n"
					+ "          type: string\n"
					+ "          description: The title of the event.\n"
					+ "        eventDescription:\n"
					+ "          type: string\n"
					+ "          description: The description of the event.\n"
					+ "        additionalDetails:\n"
					+ "          type: string\n"
					+ "          description: A link to the entire event.\n"
					+ "        additionalTags:\n"
					+ "          type: string\n"
					+ "          description: Additional tags for this event.\n"
					+ "      required:\n"
					+ "        - eventTitle\n"
					+ "        - eventDescription\n"
					+ "        - additionalDetails\n"
					+ "        - additionalTags\n"
					+ "\n"
					+ "    GetEventResponse:\n"
					+ "      type: object\n"
					+ "      properties:\n"
					+ "        userName:\n"
					+ "          type: string\n"
					+ "          description: The username of the user. (Optional)\n"
					+ "        eventDateRange:\n"
					+ "          type: string\n"
					+ "          description: The range of dates for the events.\n"
					+ "        shrineurl:\n"
					+ "          type: string\n"
					+ "          description: The URL associated with the events.\n"
					+ "        moreEventsAvailable:\n"
					+ "          type: boolean\n"
					+ "          description: There are more events svailable, make a more details request or narrow the date range to view them.\n"
					+ "        events:\n"
					+ "          type: array\n"
					+ "          items:\n"
					+ "            $ref: '#/components/schemas/Event'\n"
					+ "          description: The array of event objects.\n"
					+ "      required:\n"
					+ "        - eventDateRange\n"
					+ "        - shrineurl\n"
					+ "        - events\n"
					+ "\n"
					+ "    GetDateResponse:\n"
					+ "      type: object\n"
					+ "      properties:\n"
					+ "        userName:\n"
					+ "          type: string\n"
					+ "          description: The username of the user. (Optional)\n"
					+ "        eventDateRange:\n"
					+ "          type: string\n"
					+ "          description: The range of dates for the events.\n"
					+ "        shrineurl:\n"
					+ "          type: string\n"
					+ "          description: The URL associated with the events.\n"
					+ "      required:\n"
					+ "        - eventDateRange\n"
					+ "        - shrineurl");
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