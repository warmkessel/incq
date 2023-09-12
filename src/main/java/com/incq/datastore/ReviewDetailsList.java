package com.incq.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.*;
import com.google.common.collect.Lists;
import com.incq.constants.*;
import com.incq.entity.ReviewDetails;

import ai.AIManager;

public class ReviewDetailsList {
    static Logger logger = Logger.getLogger(ReviewDetailsList.class.getName());

    public static Entity fetchEventDetails(long key, Language lang) {
		return fetchEventDetails(key, lang, true);
		
	}
	public static Entity fetchEventDetails(long key, Language lang, boolean guarantee) {
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
		else if(guarantee){
			theReturn = fetchEventDetails(key, Language.ENGLISH);
		}
		return theReturn;
	}
public static void  expandLanugage(long key, String[] langList, boolean force) {
		
		ReviewDetails rdetail = new ReviewDetails();
		rdetail.loadFromEntity(fetchEventDetails(key, Language.ENGLISH, true));
		ReviewDetails rdetailSub = null;
		String tempLongDesc = null;
		String temptitle = null;
		String tempsummary = null;

		for (int x=0; x < langList.length; x++) {
			Language lang = Language.findByCode(langList[x]);
			if(Language.ENGLISH != lang && (force || null == fetchEventDetails(key, lang, false))) {
				rdetailSub = new ReviewDetails();
				rdetailSub.setLanguage(lang);
				rdetailSub.setDeleted(rdetail.isDeleted());

				temptitle = AIManager.editText(rdetail.getTitle(), AIConstants.AILANG + lang.name, rdetail.getTitle());
				tempsummary = AIManager.editText(rdetail.getSummary(), AIConstants.AILANG + lang.name, rdetail.getSummary());
				tempLongDesc = AIManager.editText(rdetail.getLongDesc(), AIConstants.AILANG + lang.name, rdetail.getLongDesc());
				//tempShortDesc
				//tempLongDesc	
                logger.log(Level.INFO, "Title " + temptitle);
                logger.log(Level.INFO, "Summary " + tempsummary);
                logger.log(Level.INFO, "Long Description " + tempLongDesc);

                rdetailSub.setTitle(temptitle);
                rdetailSub.setSummary(tempsummary);
                rdetailSub.setLongDesc(tempLongDesc);
                rdetailSub.save();
			}
		}
	}
	public static Map<Language, Boolean> checkReviewDetailsLanguages(long key) {
	HashMap<Language, Boolean> theReturn = new HashMap<Language, Boolean>();
	for (Language langEnum : Language.values()) {
		if(null == fetchEventDetails(key, langEnum, false)) {
			theReturn.put(langEnum, false);
		}
		else {
			theReturn.put(langEnum, true);

		}
	}
	return theReturn;
}
}