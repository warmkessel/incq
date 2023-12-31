package com.incq.enqueue;

import java.io.IOException;
import java.util.logging.*;

import com.google.apphosting.api.DeadlineExceededException;
import com.google.cloud.tasks.v2.*;
import com.google.protobuf.ByteString;
import com.incq.constants.JspConstants;
import com.incq.constants.Language;
import com.incq.constants.ReviewDetailsStep;

public class EnqueueReviewDetails {

	static Logger logger = Logger.getLogger(EnqueueReviewDetails.class.getName());

	static public void enqueueReviewDetailsTask(Long key, Language lang, ReviewDetailsStep step, boolean continueExpand) {
		enqueueReviewDetailsTask(key, lang, step, 0, continueExpand);
	}
	static public void enqueueReviewDetailsTask(Long key, Language lang, ReviewDetailsStep step, int position, boolean continueExpand) {
		try (CloudTasksClient client = CloudTasksClient.create()) {

			String parent = "projects/incq-397620/locations/us-west1/queues/Incq";

			// Prepare POST parameters
			String postData = JspConstants.ID + "=" + key + "&" + JspConstants.LANGUAGE + "=" + lang.code + "&" + JspConstants.STEP + "=" + step.name  + "&" + JspConstants.POSITION + "=" + position+ "&" + JspConstants.CONTINUE + "=" + continueExpand;
			HttpRequest httpRequest = HttpRequest.newBuilder().setBody(ByteString.copyFromUtf8(postData))
					.setHttpMethod(HttpMethod.POST).setUrl("https://incq-397620.appspot.com" + JspConstants.EXPANDREVIEWDETAILS)
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
