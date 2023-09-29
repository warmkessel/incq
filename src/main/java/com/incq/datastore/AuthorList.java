package com.incq.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.Map;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;
import com.incq.constants.*;
import com.incq.entity.Author;

public class AuthorList {

	static Logger logger = Logger.getLogger(AuthorList.class.getName());

	private static List<Entity> fetchAuthorsList(Language lang, boolean all) {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = null;
		if (all) {
			query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
					.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.BOOKMARKED, all),
							PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
							PropertyFilter.eq(AuthorConstants.DELETED, false)))
					.build();
		}
		else {
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

	public static ArrayList<Author> fetchAuthors(Language lang, boolean all) {
		ArrayList<Author> theReturn = new ArrayList<Author>();
		List<Entity> entitys = fetchAuthorsList(lang, all);
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
}