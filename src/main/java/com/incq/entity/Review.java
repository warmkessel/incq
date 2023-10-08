package com.incq.entity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
//import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.incq.constants.*;
import com.incq.datastore.*;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;

public class Review extends BaseEntity implements Comparable<Review> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -361472214131790072L;
	// private static final Logger log = Logger.getLogger(Event.class.getName());

	private boolean bookmarked = false;

	private long userId = 0;
	private List<? extends Value<?>> tags = null;
	private List<? extends Value<?>> meta = null;
	private List<? extends Value<?>> media = null;
	private ReviewDetails reviewDetails = null;
	private String slug = "";
	private String link = "";
	private String author = "";
	private String source = "";
	private double score = 5d;

	public Review() {
		setReviewDetails(new ReviewDetails());
	}

	public boolean isBookmarked() {
		return bookmarked;
	}

	public void setBookmarked(String bookmarked) {
		setBookmarked(new Boolean(bookmarked).booleanValue());
	}

	public void setBookmarked(boolean bookmarked) {
		this.bookmarked = bookmarked;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(String score) {
		try {
			setScore(Double.valueOf(score));
		}
		catch(Exception e) {
			setScore(Double.valueOf(4.9d));
		}
	}
	public void setScore(double score) {
		this.score = score;
	}

	public String getTagsEncodedString() {
		if (getTags().size() == 0) {
			return "";
		} else {
			List<String> tagStrings = getTags().stream().map(Value::get).map(Object::toString).map(String::toLowerCase).map(String::trim)
					.collect(Collectors.toList());
			return String.join("&tags=", tagStrings);
		}
	}

	public List<String> getTagsList() {
		return getTags().stream().map(Value::get).map(Object::toString).map(String::trim).collect(Collectors.toList());
	}

	public String getTagsString() {
		if (getTags().size() == 0) {
			return "";
		} else {
			return String.join(",", getTagsList());
		}
	}

	public String getCategory() {
	    return getTags().stream()
	                    .map(Value::get)
	                    .map(Object::toString)
	                    .findFirst()
	                    .orElse(ReviewConstants.GENERAL);
	}
	public List<? extends Value<?>> getTags() {

		if (null == tags) {
			tags = new ArrayList<>();
		}
		return tags;
	}

	public void setTags(String tags) {
		String[] tagsArray = tags.toLowerCase().split(",");
		setTags(Arrays.stream(tagsArray).map(String::toLowerCase).map(String::trim).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setTags(Set<String> tags) {
		String[] tagsArray = tags.toArray(new String[tags.size()]);
		setTags(Arrays.stream(tagsArray).map(String::toLowerCase).map(String::trim).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setTags(List<? extends Value<?>> tags) {
		this.tags = tags;
	}

	public String getMetaEncodedString() {
		if (getMeta().size() == 0) {
			return "";
		} else {
			return String.join("&tags=", getMetaList());
		}
	}

	public List<String> getMetaList() {
		return getMeta().stream().map(Value::get).map(Object::toString).map(String::trim).collect(Collectors.toList());
	}

	public String getMetaString() {
		if (getMeta().size() == 0) {
			return "";
		} else {
			return String.join(",", getMetaList());
		}
	}

	public List<? extends Value<?>> getMeta() {

		if (null == meta) {
			meta = new ArrayList<>();
		}
		return meta;
	}

	public void setMeta(String meta) {
		String[] metaArray = meta.toLowerCase().split(",");
		setMeta(Arrays.stream(metaArray).map(String::toLowerCase).map(String::trim).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setMeta(Collection<String> meta) {
		String[] metaArray = meta.toArray(new String[meta.size()]);
		setMeta(Arrays.stream(metaArray).map(String::toLowerCase).map(String::trim).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setMeta(List<? extends Value<?>> meta) {
		this.meta = meta;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getSource() {
		byte[] b = source.getBytes(StandardCharsets.UTF_8);
		return new String(b, StandardCharsets.UTF_8);
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<String> getMediaList() {
		return getMedia().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
	}

	public String getMediaEncodedString() {
		if (getMedia().size() == 0) {
			return "";
		} else {
			return String.join("&media=", getMediaList());
		}
	}

	public String getMediaString() {
		if (getMedia().size() == 0) {
			return "";
		} else {
			List<String> mediaStrings = getMedia().stream().map(Value::get).map(Object::toString).map(String::trim)
					.collect(Collectors.toList());
			return String.join(" ", mediaStrings);
		}
	}

	public List<? extends Value<?>> getMedia() {

		if (null == media) {
			media = new ArrayList<>();
		}
		return media;
	}

	public void setMedia(String media) {
		String[] mediaArray = media.toLowerCase().split(" ");
		setMedia(mediaArray);
	}

	public void setMedia(String[] mediaArray) {
		setMedia(Arrays.stream(mediaArray).map(String::trim).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setMedia(Set<String> media) {
		String[] mediaArray = media.toArray(new String[media.size()]);
		setMedia(Arrays.stream(mediaArray).map(String::trim).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setMedia(List<? extends Value<?>> media) {
		this.media = media;
	}

	public ReviewDetails getReviewDetails() {
		return reviewDetails;
	}

	public void setReviewDetails(ReviewDetails reviewDetails) {
		this.reviewDetails = reviewDetails;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Review other = (Review) obj;
		return this.getKey().equals(other.getKey());
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (bookmarked ? 1 : 0);
		result = 31 * result + new Long(userId).intValue();
		result = 31 * result + getTags().hashCode();
		result = 31 * result + source.hashCode();
		result = 31 * result + author.hashCode();
		result = 31 * result + media.hashCode();
		result = 31 * result + link.hashCode();
		result = 31 * result + slug.hashCode();
		result = 31 * result + new Double(score).intValue();

		return result;
	}

	public void save() {
		Key key = getKey();
		if (null != getReviewDetails()) {
			getReviewDetails().setReviewId(key.getId());
			getReviewDetails().save();
		}
		Entity.Builder entity = Entity.newBuilder(key);
		entity.set(ReviewConstants.DELETED, isDeleted()).set(ReviewConstants.BOOKMARKED, isBookmarked())
				.set(ReviewConstants.CREATEDDATE, getCreatedDate()).set(ReviewConstants.UPDATEDDATE, Timestamp.now())
				.set(ReviewConstants.USERID, getUserId()).set(ReviewConstants.TAGS, getTags())
				.set(ReviewConstants.SCORE, getScore()).set(ReviewConstants.META, getMeta())
				.set(ReviewConstants.AUTHOR, getAuthor())
				.set(ReviewConstants.SOURCE, StringValue.newBuilder(getSource()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.LINK, getLink()).set(ReviewConstants.MEDIA, getMedia())
				.set(ReviewConstants.SLUG, getSlug()).build();
		getDatastore().put(entity.build());
	}

	public static List<? extends Value<?>> convertTags(String[] tags) {
		List<StringValue> stringValues = Arrays.stream(tags).map(StringValue::of).collect(Collectors.toList());
		return Arrays.asList(ListValue.of(stringValues));
	}

	public void loadEvent(String key, Language lang) {
		loadEvent(new Long(key).longValue(), lang);
	}

	public void loadEvent(long key, Language lang) {
		loadEvent(key, lang, false);
	}

	public void loadEvent(long key, Language lang, boolean admin) {
		loadEvent(Key.newBuilder(Constants.INCQ, ReviewConstants.REVIEW, key).build(), lang, admin);
	}

	public void loadEvent(Key key, Language lang, boolean admin) {
		// log.info("key " + key.toString());
		Entity event = getDatastore().get(key);
		loadFromEntity(event, lang, admin);

	}

	public void loadFromEntity(Entity entity, Language lang) {
		loadFromEntity(entity, lang, false);
	}

	public void loadFromEntitySiteMap(Entity entity) {
		super.loadFromEntity(entity);
		setSlug(entity.getString(ReviewConstants.SLUG));

	}

	public void loadFromEntity(Entity entity, Language lang, boolean admin) {
		super.loadFromEntity(entity);
		if (null != entity) {
			setBookmarked(entity.getBoolean(ReviewConstants.BOOKMARKED));
			setUserId(entity.getLong(ReviewConstants.USERID));
			setTags(entity.getList(ReviewConstants.TAGS));
			setMeta(entity.getList(ReviewConstants.META));
			setAuthor(entity.getString(ReviewConstants.AUTHOR));
			setMedia(entity.getList(ReviewConstants.MEDIA));
			setLink(entity.getString(ReviewConstants.LINK));
			setSlug(entity.getString(ReviewConstants.SLUG));
			setSource(entity.getString(ReviewConstants.SOURCE));
			setScore(entity.getDouble(ReviewConstants.SCORE));

			Entity event = ReviewDetailsList.fetchEventDetails(entity.getKey().getId(), lang, true, admin);
			if (null != event) {
				ReviewDetails rd = new ReviewDetails();
				rd.loadFromEntity(event);
				setReviewDetails(rd);
			}
		}
	}


	public int compareTo(Review other) {
		if (this.getKeyLong() == null && other.getKeyLong() == null) {
			return 0;
		} else if (this.getKeyLong() == null) {
			return -1;
		} else if (other.getKeyLong() == null) {
			return 1;
		} else {
			return this.getKeyLong().compareTo(other.getKeyLong());
		}
	}

	public String getKind() {
		return ReviewConstants.REVIEW;
	}

}
