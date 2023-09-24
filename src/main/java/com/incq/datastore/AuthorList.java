package com.incq.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;
import com.incq.ai.AIManager;
import com.incq.constants.*;
import com.incq.enqueue.EnqueueAuthor;
import com.incq.entity.Author;
import com.incq.exception.IncqServletException;

public class AuthorList {

	static Logger logger = Logger.getLogger(AuthorList.class.getName());

	private static List<Entity> fetchAuthorsList(Language lang, boolean bookmarked) {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = null;
		if (bookmarked) {
			query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
					.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.BOOKMARKED, bookmarked),
							PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
							PropertyFilter.eq(AuthorConstants.DELETED, false)))
					.build();
		} else {
			query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
					.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
							PropertyFilter.eq(AuthorConstants.DELETED, false)))
					.build();
		}
		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		return entities;
	}

	public static ArrayList<Author> fetchAuthors(Language lang) {
		ArrayList<Author> theReturn = new ArrayList<Author>();
		List<Entity> entitys = fetchAuthorsList(lang, true);
		for (int x = 0; x < entitys.size(); x++) {
			Author author = new Author();
			author.loadFromEntity(entitys.get(x));
			theReturn.add(author);
		}
		return theReturn;
	}

	public static ArrayList<Author> fetchAuthorsSiteMap(Language lang) {
		ArrayList<Author> theReturn = new ArrayList<Author>();
		List<Entity> entitys = fetchAuthorsList(lang, false);
		for (int x = 0; x < entitys.size(); x++) {
			Author author = new Author();
			author.loadFromEntitySiteMap(entitys.get(x));
			theReturn.add(author);
		}
		return theReturn;
	}

	public static Entity fetchAuthor(String name, Language lang) {
		return fetchAuthor(name, lang, true, false);
	}

	public static Entity fetchAuthor(String name, Language lang, boolean guarantee) {
		return fetchAuthor(name, lang, guarantee, false);
	}

	public static Entity fetchAuthorAdmin(String name, Language lang, boolean guarantee) {
		return fetchAuthor(name, lang, guarantee, true);
	}

	public static Entity fetchAuthor(String name, Language lang, boolean guarantee, boolean admin) {
		Entity theReturn = null;
		Query<Entity> query = null;
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		if (admin) {
			query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
					.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.NAME, name),
							PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code)))
					.build();
		} else {
			query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
					.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.NAME, name),
							PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
							PropertyFilter.eq(AuthorConstants.DELETED, false)))
					.build();
		}

		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		if (entities.size() > 0) {
			theReturn = entities.get(0);
		} else if (guarantee) {
			theReturn = datastore
					.get(Key.newBuilder(Constants.INCQ, AuthorConstants.AUTHOR, AuthorConstants.DEFAULTID).build());

		}
		return theReturn;
	}

	public static Map<Language, Boolean> checkAuthorReady(String name) {
		HashMap<Language, Boolean> theReturn = new HashMap<Language, Boolean>();
		for (Language langEnum : Language.values()) {
			if (null == fetchAuthor(name, langEnum, false)) {
				theReturn.put(langEnum, false);
			} else {
				theReturn.put(langEnum, true);

			}
		}
		return theReturn;
	}

	public static Map<Language, Boolean> checkAuthorLanguages(String name) {
		HashMap<Language, Boolean> theReturn = new HashMap<Language, Boolean>();
		for (Language langEnum : Language.values()) {
			if (null == fetchAuthorAdmin(name, langEnum, false)) {
				theReturn.put(langEnum, false);
			} else {
				theReturn.put(langEnum, true);

			}
		}
		return theReturn;
	}

	public static void expandLanguage(String name, String[] langList) {
		if (null != name && name.length() > 0 && null != langList && langList.length > 0) {
			for (int x = 0; x < langList.length; x++) {
				Author auth = new Author();
				auth.loadFromEntity(fetchAuthorAdmin(name, Language.ENGLISH, true));
				Author authSub = null;

				Language lang = Language.findByCode(langList[x]);
				if (Language.ENGLISH != lang && null == fetchAuthorAdmin(name, lang, false)) {
					authSub = new Author();
					authSub.setName(auth.getName());
					authSub.setTranslatedName(auth.getTranslatedName());
					authSub.setTags(auth.getTags());
					authSub.setLanguage(lang);
					authSub.setStyle(auth.getStyle());
					authSub.setBookmarked(auth.isBookmarked());
					authSub.setShortDescription(auth.getShortDescription());
					authSub.setLongDescription(auth.getLongDescription());

					authSub.setDeleted(true);
					authSub.save();
					EnqueueAuthor.enqueueAuthorTask(authSub.getKeyLong(), lang, AuthorStep.STEP6, true);
				}
			}

		} else {
			logger.log(Level.SEVERE, "Failed to Find Name or Language ");

		}
	}

	public static void expandAuthorSteps(String key, String lang, String step, String position, String continueExpand)
			throws IncqServletException {
		expandAuthorSteps(Long.valueOf(key), Language.findByCode(lang), AuthorStep.findByName(step),Integer.valueOf(position),
				Boolean.valueOf(continueExpand));
	}
	public static void expandAuthorSteps(Long key, Language lang, AuthorStep step, int position, boolean continueExpand)
			throws IncqServletException {
		Author auth = new Author();
		auth.loadAuthor(key);
		expandAuthorSteps(auth, lang, step, position, continueExpand);
	}
	public static void expandAuthorSteps(Author auth, Language lang, AuthorStep step, int position, boolean continueExpand)
			throws IncqServletException {
		switch (step) {
		case STEP1:// Suggest an Authors Name
			auth.setName(AIManager.editText("", AIConstants.AIAUTHOR, auth.getName()));
			if (continueExpand) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step.next(), continueExpand);
			}
			break;
		case STEP2:// Suggest a Style
			auth.setStyle(AIManager.editText(auth.getStyle(), AIConstants.AIAUTHORSTYLE));
			if (continueExpand) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step.next(), continueExpand);
			}
			break;
		case STEP3:// Suggest some Tags
			auth.setTags(AIManager.editText(auth.getStyle() + auth.getTagsString(), AIConstants.AITAGS,
					auth.getTagsString()));
			if (continueExpand) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step.next(), continueExpand);
			}
			break;
		case STEP4:// Suggest Long Description"
			auth.setLongDescription(chunkString("My name is " + auth.getName() + auth.getLongDescription(), auth, lang, step,
					position, continueExpand, AIConstants.AIAUTHORLONG));
			
			
			if (continueExpand) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step.next(), continueExpand);
			}
			break;
		case STEP5:// Suggest Short Description"
			auth.setShortDescription(AIManager.editText("My name is " + auth.getName() + auth.getLongDescription(), AIConstants.AIAUTHORSHORT,
					auth.getStyle(), auth.getShortDescription()));
			break;
		case STEP6:// Translate Name

			auth.setTranslatedName(AIManager.editText(auth.getTranslatedName(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code +"):"));
			if (continueExpand) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step.next(), continueExpand);
			}
			break;
		case STEP7:// Translate Short Description

			auth.setShortDescription(AIManager.editText(auth.getShortDescription(),
					AIConstants.AILANG + lang.name + " BPC-47(" + lang.code +"):"));
			if (continueExpand) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step.next(), continueExpand);
			}
			break;
		case STEP8:// Translate Long Description"
			auth.setLongDescription(chunkString(auth.getLongDescription(), auth, lang, step,
					position, continueExpand));
			break;
		case STEP9:// Enable
			auth.setDeleted(false);
			break;
		case FAIL:
			logger.log(Level.SEVERE, "AuthorStep Fail key " + auth.getKeyLong() + " lang " + lang + " step " + step);
			break;
		default:
			logger.log(Level.SEVERE, "Default Fail key " + auth.getKeyLong() + " lang " + lang + " step " + step);

		}
		auth.save();

	}
	private static String chunkString(String input, Author auth, Language lang,
			AuthorStep step, int position, boolean continueExpand) throws IncqServletException {
		return chunkString(input, auth, lang,
				step, position, continueExpand, 
				AIConstants.AILANG + lang.name + " BPC-47(" + lang.code + "):");
	}
	private static String chunkString(String input, Author auth, Language lang,
			AuthorStep step, int position, boolean continueExpand, String instruction) throws IncqServletException {
		StringBuffer theReturn = new StringBuffer();
		String subString = "";
		String[] theSplit = input.split("\r\n");
		if(0 == position) {
			for (int x = 0; x < theSplit.length; x++) {
				if(0<theSplit[x].trim().length()) {
					theReturn.append(theSplit[x]).append("\r\n");
				}
			}
			theSplit = theReturn.toString().split("\r\n");
			theReturn = new StringBuffer();
		}
		if(position >= 0 && position < theSplit.length) {
			subString = theSplit[position];
		}
		int numOfTries = 10;
		while (numOfTries > 0 && subString.length() > 0) {
			try {
				theSplit[position] = AIManager.editText(subString,instruction, "", subString);
				numOfTries = 0;
			} catch (IncqServletException incq) {
				logger.log(Level.SEVERE, "Failed to execute editTextChunk OpenAI API request numOfTries " + numOfTries);
				numOfTries = numOfTries - 1;
			}

			position = position + 1;
			if (position < theSplit.length) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step, position,
						continueExpand);
			} else if (continueExpand) {
				EnqueueAuthor.enqueueAuthorTask(auth.getKeyLong(), lang, step.next(),
						continueExpand);
			}
		}
		for (int x = 0; x < theSplit.length; x++) {
			theReturn.append(theSplit[x]).append("\r\n");
		}
		return theReturn.toString();
	}
}