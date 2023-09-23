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
import com.incq.exception.IncqServletException;

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
		} else {
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

	public static void expandReviewDetails(long key, String[] langList) {

		ReviewDetails rdetail = new ReviewDetails();
		rdetail.loadFromEntity(fetchEventDetails(key, Language.ENGLISH, true));
		ReviewDetails rdetailSub = null;

		for (int x = 0; x < langList.length; x++) {
			Language lang = Language.findByCode(langList[x]);
			if (Language.ENGLISH != lang && null == fetchEventDetails(key, lang, false)) {
				rdetailSub = new ReviewDetails();
				rdetailSub.setName(rdetail.getName());
				rdetailSub.setDesc(rdetail.getDesc());
				rdetailSub.setLanguage(lang);
				rdetailSub.setReviewId(rdetail.getReviewId());
				rdetailSub.setDeleted(true);
				rdetailSub.setTitle(rdetail.getTitle());
				rdetailSub.setSummary(rdetail.getSummary());
				rdetailSub.setIntroduction(rdetail.getIntroduction());
				rdetailSub.setReviewBody(rdetail.getReviewBody());
				rdetailSub.setConclusion(rdetail.getConclusion());
				rdetailSub.setCall(rdetail.getCall());
				rdetailSub.save();
				EnqueueReviewDetails.enqueueReviewDetailsTask(rdetailSub.getReviewId(), lang, ReviewDetailsStep.STEP1,
						true);

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

	public static void expandReviewDetailSteps(String key, String lang, String step, String position,
			String continueExpand) throws IncqServletException {
		expandReviewDetailSteps(Long.valueOf(key), Language.findByCode(lang), ReviewDetailsStep.findByName(step),
				Integer.valueOf(position), Boolean.valueOf(continueExpand));

	}

	public static void expandReviewDetailSteps(Long key, Language lang, ReviewDetailsStep step, int position,
			boolean continueExpand) throws IncqServletException {
		ReviewDetails reviewDetails = new ReviewDetails();
		reviewDetails.loadEvent(key, lang, true);
		expandReviewDetailSteps(reviewDetails, lang, step, position, continueExpand);

	}

	public static void expandReviewDetailSteps(ReviewDetails reviewDetails, Language lang, ReviewDetailsStep step,
			int position, boolean continueExpand) throws IncqServletException {
		switch (step) {
		case STEP1: // translate Introduction"
			reviewDetails.setIntroduction(AIManager.editText(reviewDetails.getIntroduction(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP2: // translate Desc"
			reviewDetails.setDesc(AIManager.editText(reviewDetails.getDesc(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP3: // translate Conclusion"
			reviewDetails.setConclusion(AIManager.editText(reviewDetails.getConclusion(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP4: // translate Summary"
			reviewDetails.setSummary(AIManager.editText(reviewDetails.getSummary(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP5: // translate Title"
			reviewDetails.setTitle(AIManager.editText(reviewDetails.getTitle(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP6: // translate Name"
			reviewDetails.setName(AIManager.editText(reviewDetails.getName(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP7: // translate Call"
			reviewDetails.setCall(AIManager.editText(reviewDetails.getCall(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP8: // translate Review"
			reviewDetails.setReviewBody(translateString(reviewDetails.getReviewBody(), reviewDetails, lang, step,
					position, continueExpand));
			break;
		case STEP9:
			reviewDetails.setDeleted(false);
			break;
		case FAIL:
			logger.log(Level.SEVERE,
					"AuthorStep Fail key " + reviewDetails.getKeyLong() + " lang " + lang + " step " + step);
			break;
		default:
			logger.log(Level.SEVERE,
					"Default Fail key " + reviewDetails.getKeyLong() + " lang " + lang + " step " + step);

		}
		reviewDetails.save();

	}

	private static String translateString(String input, ReviewDetails reviewDetails, Language lang,
			ReviewDetailsStep step, int position, boolean continueExpand) throws IncqServletException {
		StringBuffer theReturn = new StringBuffer();
		String subString = "";
		String[] theSplit = input.split("\r\n");
		if (0 == position) {
			for (int x = 0; x < theSplit.length; x++) {
				if (0 < theSplit[x].trim().length()) {
					theReturn.append(theSplit[x]).append("\r\n");
				}
			}
			theSplit = theReturn.toString().split("\r\n");
			theReturn = new StringBuffer();
		}
		if (position >= 0 && position < theSplit.length) {
			subString = theSplit[position];
		}
		int numOfTries = 10;
		while (numOfTries > 0 && subString.length() > 0) {
			try {
				theSplit[position] = AIManager.editText(subString,
						AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):");
				numOfTries = 0;
			} catch (IncqServletException incq) {
				logger.log(Level.SEVERE, "Failed to execute editTextChunk OpenAI API request numOfTries " + numOfTries);
				numOfTries = numOfTries - 1;
			}

			position = position + 1;
			if (position < theSplit.length) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step, position,
						continueExpand);
			} else if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
		}
		for (int x = 0; x < theSplit.length; x++) {
			theReturn.append(theSplit[x]).append("\r\n");
		}
		return theReturn.toString();
	}
}