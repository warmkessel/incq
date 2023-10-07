package com.incq.instantiate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.incq.ai.AIManager;
import com.incq.constants.*;
import com.incq.enqueue.EnqueueReview;
import com.incq.entity.*;
import com.incq.exception.IncqServletException;
import com.incq.instantiate.helpers.FacebookHelper;
import com.incq.instantiate.helpers.FetchSourceHelper;
import com.incq.instantiate.helpers.SourceHelper;

public class ReviewInstantiate {
	static Logger logger = Logger.getLogger(ReviewInstantiate.class.getName());

	public static void expandReviewSteps(String key, String lang, String step, String continueExpand)
			throws IncqServletException {
		expandReviewSteps(Long.valueOf(key), Language.findByCode(lang), ReviewStep.findByName(step),
				Boolean.valueOf(continueExpand));

	}

	public static void expandReviewSteps(Long key, Language lang, ReviewStep step, boolean continueExpand)
			throws IncqServletException {
		Review review = new Review();
		Author author = new Author();

		review.loadEvent(key, Language.ENGLISH);
		author.loadAuthor(review.getAuthor(), Language.ENGLISH);

		switch (step) {
		case STEP1: // Fetch the Source
			try {
				review.setSource(FetchSourceHelper.fetchContent(review.getLink()));
				// EnqueueReview.enqueueReviewTask(key, step.next());

			} catch (MalformedURLException e) {
				logger.log(Level.SEVERE, "The URL is not formatted correctly: " + e.getMessage());

			} catch (IOException e) {
				logger.log(Level.SEVERE, "We had an io exception " + e.getMessage());

			} catch (Exception e) {
				logger.log(Level.SEVERE, "We had an exception " + e.getMessage());

			}
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP2:// Determine the Author
			review.setAuthor(AIManager.editText(review.getSource(), AIConstants.AIAUTHOR + review.getSource(),
					review.getAuthor()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP3:// Extract Title and Media from source

			SourceHelper helper = new SourceHelper(review.getSource());
			review.getReviewDetails().setTitle(helper.fetchTitle());
			review.setMedia(helper.fetchImages());
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;

		case STEP4:// Determine the Meta Tags
			review.setMeta(AIManager.editText(review.getSource(), AIConstants.AIMETA + review.getSource(), ""));
			review.getReviewDetails().setMeta(review.getMetaList());

			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP5:// Determine the Tags
			review.setTags(AIManager.editText(review.getSource(), AIConstants.AITAGS + review.getSource(), ""));
			review.getReviewDetails().setTags(review.getTagsList());
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP6:// Write the Review Body
			review.getReviewDetails().setReviewBody(AIManager.editText(review.getSource(),
					AIConstants.AIREVIEW, author.getStyle(), review.getReviewDetails().getReviewBody()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP6A:// Write the Review Body Alternate
			review.getReviewDetails().setReviewBody(AIManager.editText(review.getSource(),
					AIConstants.AIREVIEWA, author.getStyle(), review.getReviewDetails().getReviewBody()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP7:// Write the introductions
			review.getReviewDetails().setIntroduction(AIManager.editText(review.getReviewDetails().getReviewBody(),
					AIConstants.AIINTRODUCTION, author.getStyle(), review.getReviewDetails().getIntroduction()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP8:// Write the Conclusion
			review.getReviewDetails().setConclusion(AIManager.editText(review.getReviewDetails().getReviewBody(),
					AIConstants.AICONCLUSION, author.getStyle(), review.getReviewDetails().getConclusion()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP9:// Write the Summary
			review.getReviewDetails()
					.setSummary(AIManager.editText(
							review.getSource(),
							AIConstants.AISUMMARY, author.getStyle(), review.getReviewDetails().getSummary()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP10:// Write Name
			review.getReviewDetails().setName(AIManager.editText(review.getReviewDetails().getTitle(),
					AIConstants.AINAME, "", review.getReviewDetails().getSummary()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP11:// Write Slug
			review.setSlug(
					AIManager.editText(review.getReviewDetails().getName(), AIConstants.AISLUG, "", review.getSlug()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP12:// Write Description
			review.getReviewDetails().setDesc(AIManager.editText(review.getReviewDetails().getSummary(),
					AIConstants.AIDESC, author.getStyle(), review.getReviewDetails().getDesc()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;

		case STEP13:// Set Score
			review.setScore(4.5 + Math.round((0.5 * new Random().nextDouble()) * 100.0) / 100.0);
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP14:// Write Call
			review.getReviewDetails().setCall(AIManager.editText(review.getReviewDetails().getSummary(),
					AIConstants.AICALL, "", review.getReviewDetails().getCall()));
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP15:// Mark the Review Active
			review.setDeleted(false);
			if (continueExpand) {
				EnqueueReview.enqueueReviewTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP16:
			FacebookHelper.postToFacebookGraphAPI(review);
			break;
		case FAIL:
			logger.log(Level.SEVERE, "AuthorStep Fail key " + key + " step " + step);
			break;
		default:
			logger.log(Level.SEVERE, "Default Fail key " + key + " step " + step);

		}
		review.save();

	}

}