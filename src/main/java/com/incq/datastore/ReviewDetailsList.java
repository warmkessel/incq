package com.incq.datastore;

import java.util.*;
//import java.util.logging.Logger;
import java.util.logging.Logger;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.*;
import com.google.common.collect.Lists;
import com.incq.constants.*;

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
}