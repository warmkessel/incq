package com.incq.enqueue;

import java.io.IOException;
import java.util.logging.*;

import com.google.apphosting.api.DeadlineExceededException;
import com.google.cloud.tasks.v2.*;
import com.google.protobuf.ByteString;
import com.incq.constants.AuthorStep;
import com.incq.constants.JspConstants;
import com.incq.constants.Language;

public class EnqueueAuthor {

	static Logger logger = Logger.getLogger(EnqueueAuthor.class.getName());

	static public void enqueueAuthorTask(String name, Language lang, AuthorStep step, boolean continueExpand) {
		enqueueAuthorTask(name, lang, step, 0, continueExpand);
	}
	static public void enqueueAuthorTask(String name, Language lang, AuthorStep step, int position, boolean continueExpand) {
		try (CloudTasksClient client = CloudTasksClient.create()) {

			String parent = "projects/incq-397620/locations/us-west1/queues/Incq";

			// Prepare POST parameters
			String postData = JspConstants.NAME + "=" + name + "&" + JspConstants.LANGUAGE + "=" + lang.code + "&" + JspConstants.STEP + "=" + step.name + "&" + JspConstants.POSITION + "=" + position+ "&" + JspConstants.CONTINUE + "=" + continueExpand;
			HttpRequest httpRequest = HttpRequest.newBuilder().setBody(ByteString.copyFromUtf8(postData))
					.setHttpMethod(HttpMethod.POST).setUrl("https://incq-397620.appspot.com" + JspConstants.EXPANDAUTHOR)
					.putHeaders("Content-Type", "application/x-www-form-urlencoded") // Important for POST parameters
					.build();

			Task task = Task.newBuilder().setHttpRequest(httpRequest).build();
			client.createTask(parent, task);
		} catch (DeadlineExceededException dee) {
			// Handle exceptions
			logger.log(Level.SEVERE, "DeadlineExceededException: " + dee.getMessage());
		} catch (Exception e) {
			// Handle exceptions
			logger.log(Level.SEVERE, "Exception: " + e.getMessage());
		} finally {

		}
	}

	public static void main(String[] args) throws IOException {
	}
}
