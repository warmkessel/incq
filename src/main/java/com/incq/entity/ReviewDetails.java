package com.incq.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
//import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.incq.constants.Constants;
import com.incq.constants.Language;
import com.incq.constants.ReviewConstants;
import com.incq.datastore.ReviewDetailsList;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;

public class ReviewDetails extends BaseEntity implements Comparable<ReviewDetails> {

	private static final long serialVersionUID = -361472214131790072L;
	// private static final Logger log = Logger.getLogger(Event.class.getName());

	private Language language = Language.ENGLISH;
	private String desc = "";
	private String title = "";
	private String summary = "";
	private String introduction = "";
	private String reviewBody = "";
	private String conclusion = "";
	private long reviewId = 0l;
	private String name = "";
	private String call = "Get Mine Today!";
	private List<? extends Value<?>> tags = null;
	private List<? extends Value<?>> meta = null;

	public ReviewDetails() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getCall() {
		return call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
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

	public void setReviewBody(String reviewBody) {
		this.reviewBody = reviewBody;
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
		setTags(Arrays.stream(tagsArray).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setTags(Collection<String> tags) {
		String[] tagsArray = tags.toArray(new String[tags.size()]);
		setTags(Arrays.stream(tagsArray).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setTags(List<? extends Value<?>> tags) {
		this.tags = tags;
	}
	public List<String> getTagsList() {
		return getTags().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
	}

	public String getTagsString() {
		if (getTags().size() == 0) {
			return "";
		} else {
			return String.join(",", getTagsList());
		}
	}
	public String getMetaEncodedString() {
		if (getMeta().size() == 0) {
			return "";
		} else {
			return String.join("&tags=", getMetaList());
		}
	}

	public List<String> getMetaList() {
		return getMeta().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
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
		setMeta(Arrays.stream(metaArray).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setMeta(Collection<String> meta) {
		String[] metaArray = meta.toArray(new String[meta.size()]);
		setMeta(Arrays.stream(metaArray).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setMeta(List<? extends Value<?>> meta) {
		this.meta = meta;
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
		result = 31 * result + desc.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + summary.hashCode();
		result = 31 * result + introduction.hashCode();
		result = 31 * result + reviewBody.hashCode();
		result = 31 * result + conclusion.hashCode();
		result = 31 * result + call.hashCode();
		result = 31 * result + getTags().hashCode();
		result = 31 * result + getMeta().hashCode();

		return result;
	}

	public void save() {
		Entity.Builder entity = Entity.newBuilder(getKey());
		entity.set(ReviewConstants.DELETED, isDeleted()).set(ReviewConstants.CREATEDDATE, getCreatedDate())
				.set(ReviewConstants.UPDATEDDATE, Timestamp.now())
				.set(ReviewConstants.SUMMARY,
						StringValue.newBuilder(getSummary()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.TITLE, getTitle()).set(ReviewConstants.LANGUAGE, getLanguage().code)
				.set(ReviewConstants.REVIEW, getReviewId()).set(ReviewConstants.NAME, getName())
				.set(ReviewConstants.DESC, StringValue.newBuilder(getDesc()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.CALL, getCall())
				.set(ReviewConstants.INTRODUCTION,
						StringValue.newBuilder(getIntroduction()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.REVIEWBODY,
						StringValue.newBuilder(getReviewBody()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.TAGS, getTags())
				.set(ReviewConstants.META, getMeta())
				.set(ReviewConstants.CONCLUSION,
						StringValue.newBuilder(getConclusion()).setExcludeFromIndexes(true).build());
		
		getDatastore().put(entity.build());
	}

	public static List<? extends Value<?>> convertTags(String[] tags) {
		List<StringValue> stringValues = Arrays.stream(tags).map(StringValue::of).collect(Collectors.toList());
		return Arrays.asList(ListValue.of(stringValues));
	}

	public void loadEvent(String key, Language lang) {
		loadEvent(new Long(key).longValue(), lang, false);
	}

	public void loadEvent(long key, Language lang, boolean admin) {
		loadEvent(Key.newBuilder(Constants.INCQ, ReviewConstants.REVIEWDETAILS, key).build(), lang, admin);
	}

	public void loadEvent(Key key, Language lang, boolean admin) {
		// log.info("key " + key.toString());
		Entity event = ReviewDetailsList.fetchEventDetails(key.getId(), lang, true, admin);
		loadFromEntity(event);

	}

	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);
		if (null != entity) {
			if (entity.contains(ReviewConstants.CALL)) {
				setCall(entity.getString(ReviewConstants.CALL));
			}
			setName(entity.getString(ReviewConstants.NAME));
			setDesc(entity.getString(ReviewConstants.DESC));
			setTitle(entity.getString(ReviewConstants.TITLE));
			setIntroduction(entity.getString(ReviewConstants.INTRODUCTION));
			setReviewBody(entity.getString(ReviewConstants.REVIEWBODY));
			setConclusion(entity.getString(ReviewConstants.CONCLUSION));
			setSummary(entity.getString(ReviewConstants.SUMMARY));
			setReviewId(entity.getLong(ReviewConstants.REVIEW));

			if (entity.contains(ReviewConstants.TAGS)) {
				setTags(entity.getList(ReviewConstants.TAGS));
			}
			if (entity.contains(ReviewConstants.META)) {
				setMeta(entity.getList(ReviewConstants.META));
			}
			try {
				setLanguage(entity.getString(ReviewConstants.LANGUAGE));
			} catch (NumberFormatException nfe) {
				setLanguage(Language.ENGLISH.code);
			}
		}
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
