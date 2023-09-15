package com.incq.entity;

import java.util.Arrays;
import java.util.List;
//import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.incq.constants.Constants;
import com.incq.constants.Language;
import com.incq.constants.ReviewConstants;
import com.incq.datastore.ReviewDetailsList;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;

public class ReviewDetails extends BaseEntity implements Comparable<ReviewDetails> {

	private static final long serialVersionUID = -361472214131790072L;
	// private static final Logger log = Logger.getLogger(Event.class.getName());

	private Language language = Language.ENGLISH;
	private String title = "";
	private String summary = "";
	private String introduction = "";
	private String reviewBody = "";
	private String conclusion = "";
	private long reviewId = 0l;

	public ReviewDetails() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getReviewBody() {
		return reviewBody;
	}

	public void setReviewBody(String review) {
		this.reviewBody = review;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		setLanguage(Language.findByCode(language));
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public long getReviewId() {
		return reviewId;
	}

	public void setReviewId(long reviewId) {
		this.reviewId = reviewId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ReviewDetails other = (ReviewDetails) obj;
		return this.getKey().equals(other.getKey());
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + language.hashCode();
		result = 31 * result + title.hashCode();
		result = 31 * result + summary.hashCode();
		result = 31 * result + introduction.hashCode();
		result = 31 * result + reviewBody.hashCode();
		result = 31 * result + conclusion.hashCode();
		return result;
	}

	public void save() {
		Entity.Builder entity = Entity.newBuilder(getKey());
		entity.set(ReviewConstants.DELETED, isDeleted()).set(ReviewConstants.CREATEDDATE, getCreatedDate())
				.set(ReviewConstants.UPDATEDDATE, getUpdatedDate()).set(ReviewConstants.SUMMARY, getSummary())
				.set(ReviewConstants.TITLE, getTitle()).set(ReviewConstants.LANGUAGE, getLanguage().code)
				.set(ReviewConstants.REVIEW, getReviewId())
				.set(ReviewConstants.INTRODUCTION,
						StringValue.newBuilder(getIntroduction()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.REVIEWBODY,
						StringValue.newBuilder(getReviewBody()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.CONCLUSION,
						StringValue.newBuilder(getConclusion()).setExcludeFromIndexes(true).build());
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
		loadEvent(Key.newBuilder(Constants.INCQ, ReviewConstants.REVIEWDETAILS, key).build(), lang);
	}

	public void loadEvent(Key key, Language lang) {
		// log.info("key " + key.toString());
		Entity event = ReviewDetailsList.fetchEventDetails(key.getId(), lang);
		loadFromEntity(event);

	}

	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);
		if (null != entity) {
			setTitle(entity.getString(ReviewConstants.TITLE));
			setIntroduction(entity.getString(ReviewConstants.INTRODUCTION));
			setReviewBody(entity.getString(ReviewConstants.REVIEWBODY));
			setConclusion(entity.getString(ReviewConstants.CONCLUSION));
			setSummary(entity.getString(ReviewConstants.SUMMARY));
			setReviewId(entity.getLong(ReviewConstants.REVIEW));

			try {
				setLanguage(entity.getString(ReviewConstants.LANGUAGE));
			} catch (NumberFormatException nfe) {
				setLanguage(Language.ENGLISH.code);
			}
		}
	}

	public String toString() {
		return "Event{" + "" + Constants.KEY + "='" + getKeyString() + '\'' + ", " + ReviewConstants.DELETED + "="
				+ isDeleted() + ", \" + CREATEDDATE + \"=" + getCreatedDate() + ", \" + SUMMARY + \"=" + getSummary()
				+ ", \" + UPDATEDDATE + \"=" + getUpdatedDate() + ", " + '\'' + ", \" + TITLE + \"='" + title + '\''
				+ ", \" + KINGDOM + \"='" + ReviewConstants.COMPACTDESC + '\'' + '\'' + ", \" + INTRODUCTION + \"='"
				+ introduction + '\'' + ", \" + REVIEWBODY + \"='" + reviewBody + '\'' + ", \" + CONCLUSION + \"='"
				+ conclusion + '\'' + ", \" + REVIEWID + \"='" + reviewId + '\'' + '}';
	}

	public int compareTo(ReviewDetails other) {
		if (this.title == null && other.title == null) {
			return 0;
		} else if (this.title == null) {
			return -1;
		} else if (other.title == null) {
			return 1;
		} else {
			return this.title.compareTo(other.title);
		}
	}

	public String getKind() {
		return ReviewConstants.REVIEWDETAILS;
	}

}
