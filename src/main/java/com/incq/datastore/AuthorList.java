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

public class AuthorList {

	static Logger logger = Logger.getLogger(AuthorList.class.getName());

	private static List<Entity> fetchAuthorsList(Language lang) {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.BOOKMARKED, true),
						PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
						PropertyFilter.eq(AuthorConstants.DELETED, false)))
				.build();

		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		return entities;
	}

	public static ArrayList<Author> fetchAuthors(Language lang) {
		ArrayList<Author> theReturn = new ArrayList<Author>();
		List<Entity> entitys = fetchAuthorsList(lang);
		for (int x = 0; x < entitys.size(); x++) {
			Author author = new Author();
			author.loadFromEntity(entitys.get(x));
			theReturn.add(author);
		}
		return theReturn;
	}

	public static Entity fetchAuthor(String name, Language lang) {
		return fetchAuthor(name, lang, true);
	}

	public static Entity fetchAuthor(String name, Language lang, boolean guarantee) {
		Entity theReturn = null;
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.NAME, name),
						PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
						PropertyFilter.eq(AuthorConstants.DELETED, false)))
				.build();

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

	public static Map<Language, Boolean> checkAuthorLanguages(String name) {
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

	public static void expandLanguage(String name, String[] langList) {
		if (null != name && name.length() > 0 && null != langList && langList.length > 0) {
			for (int x = 0; x < langList.length; x++) {
				Author auth = new Author();
				auth.loadFromEntity(fetchAuthor(name, Language.ENGLISH, true));
				Author authSub = null;

				Language lang = Language.findByCode(langList[x]);
				if (Language.ENGLISH != lang && null == fetchAuthor(name, lang, false)) {
					authSub = new Author();
					authSub.setName(auth.getName());
					authSub.setTags(auth.getTags());
					authSub.setLanguage(lang);
					authSub.setStyle(auth.getStyle());
					authSub.setBookmarked(auth.isBookmarked());
//				tempLongDesc = AIManager.editText(auth.getLongDescription(), AIConstants.AILANG + lang.name,
//						auth.getLongDescription());
//				tempShortDesc = AIManager.editText(auth.getShortDescription(), AIConstants.AILANG + lang.name,
//						auth.getShortDescription());

					authSub.setShortDescription(auth.getShortDescription());
					authSub.setLongDescription(auth.getLongDescription());

					authSub.setDeleted(true);
					authSub.save();
					EnqueueAuthor.enqueueAuthorTask(authSub.getKeyLong(), lang, AuthorStep.STEP1);
				}
			}

		} else {
			logger.log(Level.SEVERE, "Failed to Find Name or Language ");

		}
	}

	public static void expandAuthorSteps(String key, String lang, String step) {
		expandAuthorSteps(Long.valueOf(key), Language.findByCode(lang), AuthorStep.findByName(step));

	}

	public static void expandAuthorSteps(Long key, Language lang, AuthorStep step) {
		Author auth = new Author();
		auth.loadAuthor(key);
		switch (step) {
		case STEP1:// Suggest an Authors Name
			auth.setName(AIManager.editText("", AIConstants.AIAUTHOR, auth.getName()));
			//EnqueueAuthor.enqueueAuthorTask(key, lang, step.next());
			break;
		case STEP2:// Suggest a Style
			auth.setStyle(AIManager.editText(auth.getStyle(), AIConstants.AIAUTHORSTYLE, auth.getStyle()));
			//EnqueueAuthor.enqueueAuthorTask(key, lang, step.next());
			break;
		case STEP3:// Suggest some Tags
			auth.setTags(AIManager.editText(auth.getStyle() + auth.getTagsString(), AIConstants.AITAGS,
					auth.getTagsString()));
			//EnqueueAuthor.enqueueAuthorTask(key, lang, step.next());
			break;
		case STEP4:// Suggest Long Description"
			auth.setLongDescription(AIManager.editText3(auth.getLongDescription(), AIConstants.AIAUTHORLONG, auth.getStyle(), auth.getLongDescription()));
			//EnqueueAuthor.enqueueAuthorTask(key, lang, step.next());
			break;
		case STEP5://Suggest Short Description"
			auth.setShortDescription(AIManager.editText(auth.getLongDescription(), AIConstants.AIAUTHORSHORT, auth.getStyle(), auth.getShortDescription()));
			//EnqueueAuthor.enqueueAuthorTask(key, lang, step.next());
			break;
		case STEP6:// Translate Short Description

			auth.setShortDescription(AIManager.editText(auth.getShortDescription(), AIConstants.AILANG + lang.name,
					auth.getShortDescription()));
			//EnqueueAuthor.enqueueAuthorTask(key, lang, step.next());
			break;
		case STEP7:// Translate Long Description"
			auth.setLongDescription(AIManager.editText3(auth.getLongDescription(), AIConstants.AILANG + lang.name,
					auth.getLongDescription()));
			//EnqueueAuthor.enqueueAuthorTask(key, lang, step.next());
			break;
		case STEP8:// Enable
			auth.setDeleted(false);
			break;
		case FAIL:
			logger.log(Level.SEVERE, "AuthorStep Fail key " + key + " lang " + lang + " step " + step);
			break;
		default:
			logger.log(Level.SEVERE, "Default Fail key " + key + " lang " + lang + " step " + step);

		}
		auth.save();

	}
}