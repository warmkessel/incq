package com.incq.datastore;

import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Logger;
import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;
import com.incq.constants.*;
import com.incq.entity.*;

public class ReviewList {
	static Logger logger = Logger.getLogger(ReviewList.class.getName());

	private static List<Entity> fetchBookmaredEntities(boolean bookmarked) {

		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

		Query<Entity> query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEW)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.BOOKMARKED, true),
						PropertyFilter.eq(ReviewConstants.DELETED, false)))
				.build();

		if (!bookmarked) {
			query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEW)
					.setFilter(PropertyFilter.eq(ReviewConstants.DELETED, false)).build();
		}
		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		return entities;
	}

	private static List<Entity> fetchCategoryEntities(String category) {
		ListValue.Builder listBuilder = ListValue.newBuilder().addValue(StringValue.newBuilder(category).build());

		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

		Query<Entity> query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEW)
				
				.setFilter(CompositeFilter.and(PropertyFilter.in(ReviewConstants.TAGS, listBuilder.build()),
						PropertyFilter.eq(ReviewConstants.DELETED, false)))
				.build();
		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		return entities;
	}

	
	public static ArrayList<Review> fetchBookmaredReviews(Language lang) {
		ArrayList<Review> theReturn = new ArrayList<Review>();
		List<Entity> entitys = fetchBookmaredEntities(true);
		for (int x = 0; x < entitys.size(); x++) {
			Review review = new Review();
			review.loadFromEntity(entitys.get(x), lang);
			theReturn.add(review);
		}
		return theReturn;
	}

	public static ArrayList<Review> fetchCategoryEntities(String category, Language lang) {
		ArrayList<Review> theReturn = new ArrayList<Review>();
		List<Entity> entitys = fetchCategoryEntities(category);
		for (int x = 0; x < entitys.size(); x++) {
			Review review = new Review();
			review.loadFromEntity(entitys.get(x), lang);
			theReturn.add(review);
		}
		return theReturn;
	}
	
	public static ArrayList<Review> fetchReviewSiteMap(Language lang) {
		ArrayList<Review> theReturn = new ArrayList<Review>();
		List<Entity> entitys = fetchBookmaredEntities(false);
		for (int x = 0; x < entitys.size(); x++) {
			Review review = new Review();
			review.loadFromEntity(entitys.get(x), lang);
			theReturn.add(review);
		}
		return theReturn;
	}

	public static Entity fetchReview(String slug) {
		return fetchReview(slug, true);
	}

	public static Entity fetchReview(String slug, boolean guarantee) {
		Entity theReturn = null;
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEW).setFilter(CompositeFilter
				.and(PropertyFilter.eq(ReviewConstants.SLUG, slug), PropertyFilter.eq(AuthorConstants.DELETED, false)))
				.build();
		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		if (entities.size() > 0) {
			theReturn = entities.get(0);
		} else if (guarantee) {
			theReturn = datastore
					.get(Key.newBuilder(Constants.INCQ, ReviewConstants.REVIEW, ReviewConstants.DEFAULTID).build());

		}
		return theReturn;
	}
}