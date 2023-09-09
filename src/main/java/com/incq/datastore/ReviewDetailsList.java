package com.incq.datastore;

import java.util.List;
//import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.*;
import com.google.common.collect.Lists;
import com.incq.constants.*;
import com.incq.constants.ReviewConstants;

public class ReviewDetailsList {
	public static Entity fetchEventDetails(long key, Language lang) {
		Entity theReturn = null;
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEWDETAILS)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.REVIEW, key),
						PropertyFilter.eq(ReviewConstants.LANGUAGE, lang.code),
						PropertyFilter.eq(ReviewConstants.DELETED, false)))
				.build();
		// Run the query and retrieve a list of matching entities
		QueryResults<Entity> results = datastore.run(query);
		List<Entity> entities = Lists.newArrayList(results);
		if (entities.size() > 0) {
			theReturn = entities.get(0);
		}
		else {
			theReturn = datastore.get(Key.newBuilder(Constants.INCQ, ReviewConstants.REVIEWDETAILS, ReviewConstants.DEFAULTID).build());

		}
		return theReturn;
	}
}