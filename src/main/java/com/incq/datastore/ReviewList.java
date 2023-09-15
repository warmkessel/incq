package com.incq.datastore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;
import com.incq.ai.AIManager;
import com.incq.constants.*;
import com.incq.datastore.helper.FetchSourceHelper;
import com.incq.datastore.helper.SourceHelper;
import com.incq.enqueue.EnqueueReview;
import com.incq.entity.*;

public class ReviewList {
	static Logger logger = Logger.getLogger(ReviewList.class.getName());

	public static List<Entity> fetchBookmaredEntities() {

		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

		Query<Entity> query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEW)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.BOOKMARKED, true),
						PropertyFilter.eq(ReviewConstants.DELETED, false)))
				.build();

		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		return entities;
	}

	public static ArrayList<Review> fetchBookmaredReviews(Language lang) {
		ArrayList<Review> theReturn = new ArrayList<Review>();
		List<Entity> entitys = fetchBookmaredEntities();
		for (int x = 0; x < entitys.size(); x++) {
			Review review = new Review();
			review.loadFromEntity(entitys.get(x), lang);
			theReturn.add(review);
		}
		return theReturn;
	}

	public static void expandReviewSteps(String key, String lang, String step) {
		expandReviewSteps(Long.valueOf(key), Language.findByCode(lang), ReviewStep.findByName(step));

	}

	public static void expandReviewSteps(Long key, Language lang, ReviewStep step) {
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
			break;
		case STEP2:// Determine the Author
			review.setAuthor(AIManager.editText(review.getSource(), AIConstants.AIAUTHOR + review.getSource(),
					review.getAuthor()));
			EnqueueReview.enqueueReviewTask(key, lang, step.next());
			break;
		case STEP3:// Extract Title and Media from source

			SourceHelper helper = new SourceHelper(review.getSource());
			review.getReviewDetails().setTitle(helper.fetchTitle());
			review.setMedia(helper.fetchImages());
			// EnqueueReview.enqueueReviewTask(key, step.next());

			break;

		case STEP4:// Determine the Meta Tags
			review.setMeta(AIManager.editText(review.getSource(), AIConstants.AIMETA + review.getSource(), ""));
			EnqueueReview.enqueueReviewTask(key, lang, step.next());
			break;
		case STEP5:// Determine the Tags
			review.setTags(AIManager.editText(review.getSource(), AIConstants.AITAGS + review.getSource(), ""));
			EnqueueReview.enqueueReviewTask(key, lang, step.next());
			break;
		case STEP6:// Write the introductions
			review.getReviewDetails().setReviewBody(AIManager.editText(review.getSource(), AIConstants.AIIREVIEW,
					author.getStyle(), review.getReviewDetails().getReviewBody()));
			// EnqueueReview.enqueueReviewTask(key, step.next());
			break;
		case STEP7:// Write the introductions
			review.getReviewDetails().setIntroduction(AIManager.editText(review.getReviewDetails().getReviewBody(), AIConstants.AIINTRODUCTION,
					author.getStyle(), review.getReviewDetails().getIntroduction()));
			// EnqueueReview.enqueueReviewTask(key, step.next());
			break;
		case STEP8:// Write the Review Body
			review.getReviewDetails().setConclusion(AIManager.editText(review.getReviewDetails().getReviewBody(), AIConstants.AICONCLUSION,
					author.getStyle(), review.getReviewDetails().getConclusion()));
			// EnqueueReview.enqueueReviewTask(key, step.next());
			break;
		case STEP9:// Write the Conclusion
			review.getReviewDetails()
					.setSummary(AIManager.editText(
							review.getReviewDetails().getIntroduction() + review.getReviewDetails().getReviewBody()
									+ review.getReviewDetails().getConclusion(),
							AIConstants.AISUMMARY, author.getStyle(), review.getReviewDetails().getSummary()));
			// EnqueueReview.enqueueReviewTask(key, step.next());
			break;
		case STEP10:// Mark the Review Active
			review.setDeleted(false);
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