package com.incq.instantiate;

import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.incq.ai.AIManager;
import com.incq.constants.*;
import com.incq.datastore.ReviewDetailsList;
import com.incq.enqueue.EnqueueReviewDetails;
import com.incq.entity.ReviewDetails;
import com.incq.exception.IncqServletException;
import com.incq.instantiate.helpers.FacebookHelper;
import com.incq.instantiate.helpers.IndexNow;

public class ReviewDetailsInstantiate {
	static Logger logger = Logger.getLogger(ReviewDetailsInstantiate.class.getName());

	public static void expandReviewDetails(long key, String[] langList) {

		ReviewDetails rdetail = new ReviewDetails();
		rdetail.loadFromEntity(ReviewDetailsList.fetchEventDetails(key, Language.ENGLISH, true));
		ReviewDetails rdetailSub = null;

		for (int x = 0; x < langList.length; x++) {
			Language lang = Language.findByCode(langList[x]);
			if (Language.ENGLISH != lang && null == ReviewDetailsList.fetchEventDetails(key, lang, false)) {
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
				rdetailSub.setTags(rdetail.getTags());
				rdetailSub.setMeta(rdetail.getMeta());
				rdetailSub.save();
				EnqueueReviewDetails.enqueueReviewDetailsTask(rdetailSub.getReviewId(), lang, ReviewDetailsStep.STEP1,
						true);

			}
		}
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
		ReviewDetails sourceDetails = new ReviewDetails();
		sourceDetails.loadEvent(reviewDetails.getReviewId(), Language.ENGLISH, true);

		switch (step) {
		case STEP1: // translate Introduction"
			reviewDetails.setIntroduction(AIManager.editText(sourceDetails.getIntroduction(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP2: // translate Desc"
			reviewDetails.setDesc(AIManager.editText(sourceDetails.getDesc(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP3: // translate Conclusion"
			reviewDetails.setConclusion(AIManager.editText(sourceDetails.getConclusion(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP4: // translate Summary"
			reviewDetails.setSummary(AIManager.editText(sourceDetails.getSummary(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP5: // translate Title"
			reviewDetails.setTitle(AIManager.editText(sourceDetails.getTitle(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP6: // translate Name"
			reviewDetails.setName(AIManager.editText(sourceDetails.getName(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP7: // translate Call"
			reviewDetails.setCall(AIManager.editText(sourceDetails.getCall(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));

			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP8: // translate Review"
//			reviewDetails.setReviewBody(translateString(sourceDetails.getReviewBody(), reviewDetails, lang, step,
//					position, continueExpand));
			reviewDetails.setReviewBody(translateString(reviewDetails.getReviewBody(), reviewDetails, lang, step,
					position, continueExpand));
			break;
		case STEP9:// translate Tags"
			List<String>tags = sourceDetails.getTagsList();
			List<String>translatedTags = new ArrayList<String>();
			for(String tag: tags) {
				translatedTags.add(AIManager.editText(tag,
						AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));
			}
			reviewDetails.setTags(translatedTags);
			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP10: // translate Meta"
			List<String>meta = sourceDetails.getMetaList();
			List<String>translatedMeta = new ArrayList<String>();
			for(String element: meta) {
				translatedMeta.add(AIManager.editText(element,
						AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):"));
			}
			reviewDetails.setMeta(translatedMeta);
			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP11:
			reviewDetails.setDeleted(false);
			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
		case STEP12:
			if(!reviewDetails.isDeleted()) {
				FacebookHelper.postToFacebookGraphAPI(reviewDetails.getReviewId(), lang);
			}
			if (continueExpand) {
				EnqueueReviewDetails.enqueueReviewDetailsTask(reviewDetails.getReviewId(), lang, step.next(),
						continueExpand);
			}
			break;
			
		case STEP13:
			if(!reviewDetails.isDeleted()) {
				IndexNow.postIndexNow(reviewDetails.getReviewId(), lang);
			}
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