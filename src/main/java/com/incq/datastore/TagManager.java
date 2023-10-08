package com.incq.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;
import com.incq.constants.AuthorConstants;
import com.incq.constants.Language;
import com.incq.constants.ReviewConstants;

public class TagManager {
	private Map<String, Map<String, Integer>> authorsMap = null;

	private Map<String, Integer> uniqueTags = null;

	public TagManager() {
	}

	public Map<String, Integer> fetchUniqueTags() {
		if (uniqueTags == null) {
			uniqueTags = buildTagData();
		}
		return uniqueTags;
	}

	public Map<String, Map<String, Integer>> fetchAuthorsTags() {
		if (authorsMap == null) {
			authorsMap = buildAuthorTagData();
		}
		return authorsMap;
	}

	public String fetchUniqueTagsString() {
		return fetchUniqueTagsString(fetchUniqueTags().keySet());
	}

	public String fetchUniqueTagsString(Set<String> tags) {
		return String.join(",", tags);
	}

	public String fetchUniqueTagsStringCR() {

		StringBuilder sb = new StringBuilder();
		for (String tag : fetchUniqueTags().keySet()) {
			sb.append(tag);
			sb.append("-");
			sb.append(fetchUniqueTags().get(tag));
			sb.append("<br>");
		}

		return sb.toString().trim(); // trim() to remove the trailing "\r"
	}

	public String fetchUniqueTagsByAuthorString() {
		StringBuffer theReturn = new StringBuffer();
		for (String authorName : fetchAuthorsTags().keySet()) {
			theReturn.append(fetchUniqueTagsByAuthorString(authorName));
		}
		return theReturn.toString();
	}

	public String fetchUniqueTagsByAuthorString(String authorName) {

		StringBuilder sb = new StringBuilder();
		sb.append("Author:").append(authorName);
		sb.append(" Tags:");
		sb.append(fetchUniqueTagsString(fetchAuthorsTags().get(authorName).keySet()));
		sb.append(". ");
		return sb.toString();
	}

	private Map<String, Map<String, Integer>> buildAuthorTagData() {
		Map<String, Map<String, Integer>> theReturn = new HashMap<>();
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

		Query<Entity> authorQuery = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.LANGUAGE, Language.ENGLISH.code),
						PropertyFilter.eq(AuthorConstants.DELETED, false)))
				.build();

		QueryResults<Entity> results = datastore.run(authorQuery);
		List<Entity> entities = Lists.newArrayList(results);

		for (Entity entity : entities) {
			Map<String, Integer> theAuthor = new HashMap<>();
			String name = entity.getString(ReviewConstants.NAME);
			theAuthor = extractTagDataFromResults(theAuthor, entity);
			theReturn.put(name, theAuthor);
		}
		return theReturn;
	}

	private Map<String, Integer> buildTagData() {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

		Query<Entity> reviewQuery = Query.newEntityQueryBuilder().setKind(ReviewConstants.REVIEW)
				.setFilter(PropertyFilter.eq(AuthorConstants.DELETED, false)).build();
		QueryResults<Entity> reviewResults = datastore.run(reviewQuery);

		Query<Entity> authorQuery = Query.newEntityQueryBuilder().setKind(AuthorConstants.AUTHOR)
				.setFilter(CompositeFilter.and(PropertyFilter.eq(ReviewConstants.LANGUAGE, Language.ENGLISH.code),
						PropertyFilter.eq(AuthorConstants.DELETED, false)))
				.build();

		QueryResults<Entity> authorResults = datastore.run(authorQuery);

		Map<String, Integer> theReturn = new HashMap<>();

		theReturn = extractTagDataFromResults(theReturn, reviewResults);
		theReturn = extractTagDataFromResults(theReturn, authorResults);
		return theReturn;
	}

	private Map<String, Integer> extractTagDataFromResults(Map<String, Integer> theReturn,
			QueryResults<Entity> results) {
		while (results.hasNext()) {
			extractTagDataFromResults(theReturn, results.next());
		}
		return theReturn;
	}

	private Map<String, Integer> extractTagDataFromResults(Map<String, Integer> theReturn, Entity entity) {
		List<Value<String>> tagList = entity.getList(ReviewConstants.TAGS);
		for (Value<String> tagValue : tagList) {
			String tag = tagValue.get();
			theReturn.put(tag, theReturn.getOrDefault(tag, 0) + 1);
		}
		return theReturn;
	}
}