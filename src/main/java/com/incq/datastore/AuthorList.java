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
import com.incq.constants.*;
import com.incq.entity.Author;

import ai.AIManager;

public class AuthorList {
	
    static Logger logger = Logger.getLogger(AuthorList.class.getName());

	private static List<Entity> fetchAuthorsList(Language lang) {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.BOOKMARKED, true),PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
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
			for(int x=0; x < entitys.size(); x++) {
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
				.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.NAME, name),PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
						PropertyFilter.eq(AuthorConstants.DELETED, false)))
				.build();

		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		if(entities.size() > 0) {
			theReturn = entities.get(0);
		}
		else if(guarantee){
			theReturn = datastore.get(Key.newBuilder(Constants.INCQ, AuthorConstants.AUTHOR, AuthorConstants.DEFAULTID).build());

		}
		return theReturn;
	}	
	public static Map<Language, Boolean> checkAuthorLanguages(String name) {
		HashMap<Language, Boolean> theReturn = new HashMap<Language, Boolean>();
		for (Language langEnum : Language.values()) {
			if(null == fetchAuthor(name, langEnum, false)) {
				theReturn.put(langEnum, false);
			}
			else {
				theReturn.put(langEnum, true);

			}
		}
		return theReturn;
	}
	public static void  expandLanguage(String name, String[] langList, boolean force) {
		
		Author auth = new Author();
		auth.loadFromEntity(fetchAuthor(name, Language.ENGLISH, true));
		Author authSub = null;
		String tempLongDesc = null;
		String tempShortDesc = null;
		
		for (int x=0; x < langList.length; x++) {
			Language lang = Language.findByCode(langList[x]);
			if(Language.ENGLISH != lang && (force || null == fetchAuthor(name, lang, false))) {
				authSub = new Author();
				authSub.setName(auth.getName());
				authSub.setLanguage(lang);
				authSub.setStyle(auth.getStyle());
				authSub.setBookmarked(auth.isBookmarked());
				authSub.setDeleted(auth.isDeleted());

				tempShortDesc = AIManager.editText(auth.getShortDescription(), AIConstants.AILANG + lang.name, auth.getShortDescription());
				tempLongDesc = AIManager.editText(auth.getLongDescription(), AIConstants.AILANG + lang.name, auth.getLongDescription());
				//tempShortDesc
				//tempLongDesc	
                logger.log(Level.INFO, "ShortDescription " + tempShortDesc);
                logger.log(Level.INFO, "LongDescription " + tempLongDesc);
                
                authSub.setShortDescription(tempShortDesc);
				authSub.setLongDescription(tempLongDesc);
				authSub.save();
			}
		}
	}
	
}