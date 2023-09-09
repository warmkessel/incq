package com.incq.datastore;

import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;
import com.incq.constants.*;
import com.incq.entity.Author;

public class AuthorList {
	private static List<Entity> fetchAuthorsList(Language lang) {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.BOOKMARKED, true),
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
		Entity theReturn = null;
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(AuthorConstants.NAME, name),
						PropertyFilter.eq(AuthorConstants.DELETED, false)))
				.build();

		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		if(entities.size() > 0) {
			theReturn = entities.get(0);
		}
		else {
			theReturn = datastore.get(Key.newBuilder(Constants.INCQ, AuthorConstants.AUTHOR, AuthorConstants.DEFAULTID).build());

		}
		return theReturn;
	}	
	
}