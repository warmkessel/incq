package com.incq.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.*;
import com.google.common.collect.Lists;
import com.incq.ai.AIManager;
import com.incq.constants.*;
import com.incq.enqueue.EnqueueReviewDetails;
import com.incq.entity.ReviewDetails;

public class ReviewDetailsList {
	static Logger logger = Logger.getLogger(ReviewDetailsList.class.getName());

	public static Entity fetchEventDetails(long key, Language lang) {
		return fetchEventDetails(key, lang, true);
	}

	public static Entity fetchEventDetails(long key, Language lang, boolean guarantee) {
		return fetchEventDetails(key, lang, guarantee, false);
	}
	public static Entity fetchEventDetailsAdmin(long key, Language lang, boolean guarantee) {
		return fetchEventDetails(key, lang, guarantee, true);
	}
	public static Entity fetchEventDetails(long key, Language lang, boolean guarantee, boolean admin) {
		Entity theReturn = null;
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = null;
		if (admin) {
			 query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEWDETAILS)
						.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.REVIEW, key),
								PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code)))
						.build();
		}
		else {
			 query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEWDETAILS)
						.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.REVIEW, key),
								PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
								PropertyFilter.eq(ReviewConstants.DELETED, false)))
						.build();
			// Run the query and retrieve a list of matching entities
		}
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		if (entities.size() > 0) {
			theReturn = entities.get(0);
		} else if (guarantee) {
			theReturn = fetchEventDetails(key, Language.ENGLISH, true, false);
		}
		return theReturn;
	}

	public static void expandLanugage(long key, String[] langList) {

		ReviewDetails rdetail = new ReviewDetails();
		rdetail.loadFromEntity(fetchEventDetails(key, Language.ENGLISH, true));
		ReviewDetails rdetailSub = null;

		for (int x = 0; x < langList.length; x++) {
			Language lang = Language.findByCode(langList[x]);
			if (Language.ENGLISH != lang && null == fetchEventDetails(key, lang, false)) {
				rdetailSub = new ReviewDetails();
				rdetailSub.setLanguage(lang);
				rdetailSub.setReviewId(rdetail.getReviewId());
				rdetailSub.setDeleted(true);
				rdetailSub.setTitle(rdetail.getTitle());
				rdetailSub.setSummary(rdetail.getSummary());
				rdetailSub.setIntroduction(rdetail.getIntroduction());
				rdetailSub.setReviewBody(rdetail.getReviewBody());
				rdetailSub.setConclusion(rdetail.getConclusion());
				rdetailSub.save();
				EnqueueReviewDetails.enqueueReviewDetailsTask(rdetailSub.getKeyLong(), lang,
				 ReviewDetailsStep.STEP1, true);

			}
		}
	}

	public static Map<Language, Boolean> checkReviewDetailsReady(long key) {
		HashMap<Language, Boolean> theReturn = new HashMap<Language, Boolean>();
		for (Language langEnum : Language.values()) {
			if (null == fetchEventDetails(key, langEnum, false)) {
				theReturn.put(langEnum, false);
			} else {
				theReturn.put(langEnum, true);

			}
		}
		return theReturn;
	}
	public static Map<Language, Boolean> checkReviewDetailsLanguages(long key) {
		HashMap<Language, Boolean> theReturn = new HashMap<Language, Boolean>();
		for (Language langEnum : Language.values()) {
			if (null == fetchEventDetailsAdmin(key, langEnum, false)) {
				theReturn.put(langEnum, false);
			} else {
				theReturn.put(langEnum, true);

			}
		}
		return theReturn;
	}

	public static void expandReviewDetailSteps(String key, String lang, String step, String continueExpand) {
		expandReviewDetailSteps(Long.valueOf(key), Language.findByCode(lang), ReviewDetailsStep.findByName(step),
				Boolean.valueOf(continueExpand));

	}

	public static void expandReviewDetailSteps(Long key, Language lang, ReviewDetailsStep step,
			boolean continueExpand) {
		ReviewDetails reviewDetails = new ReviewDetails();
		reviewDetails.loadEvent(key, lang, true);
		switch (step) {
		case STEP1: // translate Introduction"
			reviewDetails.setIntroduction(AIManager.editText(reviewDetails.getIntroduction(),
					AIConstants.AILANG + lang.name, "", reviewDetails.getIntroduction()));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP2: // translate Review"
			reviewDetails.setReviewBody(AIManager.editTextChunk(reviewDetails.getReviewBody(),
					AIConstants.AILANG + lang.name, "", reviewDetails.getReviewBody(), AIConstants.AIDELIM));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP3: // translate Conclusion"
			reviewDetails.setConclusion(AIManager.editText(reviewDetails.getConclusion(),
					AIConstants.AILANG + lang.name, "", reviewDetails.getConclusion()));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP4: // translate Summary"
			reviewDetails.setSummary(AIManager.editText(reviewDetails.getSummary(), AIConstants.AILANG + lang.name, "",
					reviewDetails.getSummary()));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP5: // translate Title"
			reviewDetails.setTitle(AIManager.editText(reviewDetails.getTitle(), AIConstants.AILANG + lang.name, "",
					reviewDetails.getTitle()));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(key, lang, step.next(), continueExpand);
			}
			break;
		case STEP6:
			reviewDetails.setDeleted(false);
			break;
		case FAIL:
			logger.log(Level.SEVERE, "AuthorStep Fail key " + key + " lang " + lang + " step " + step);
			break;
		default:
			logger.log(Level.SEVERE, "Default Fail key " + key + " lang " + lang + " step " + step);

		}
		reviewDetails.save();

	}
}