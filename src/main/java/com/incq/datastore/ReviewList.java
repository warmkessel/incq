package com.incq.datastore;

import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;
import com.incq.constants.ReviewConstants;
import com.incq.entity.*;

public class ReviewList {
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
	
	public static ArrayList<Review> fetchBookmaredReviews(int lang) {
		ArrayList<Review> theReturn = new ArrayList<Review>();
		List<Entity> entitys = fetchBookmaredEntities();
			for(int x=0; x < entitys.size(); x++) {
				Review review = new Review();
				review.loadFromEntity(entitys.get(x), lang);
				theReturn.add(review);
			}		
		return theReturn;
	}
	

	
}