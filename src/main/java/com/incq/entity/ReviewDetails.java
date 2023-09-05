package com.incq.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
//import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.*;

import com.incq.constants.Constants;
import com.incq.constants.Language;
import com.incq.constants.ReviewConstants;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;
public class ReviewDetails extends BaseEntity implements Comparable<ReviewDetails> {

	private static final long serialVersionUID = -361472214131790072L;
	 //private static final Logger log = Logger.getLogger(Event.class.getName());

	private int language = 0;
	private String title = "";
	private String summary = "";
	private String longDesc = "";
	private long reviewId = 0l;

	public ReviewDetails() {
	}
	public ReviewDetails(Key key, boolean deleted, Timestamp createdDate, Timestamp updatedDate, String title, String compactDesc,
			String longDesc, long reviewId) {
		super(key, deleted, createdDate, updatedDate);
		this.title = Objects.requireNonNull(title);
		this.longDesc = Objects.requireNonNull(longDesc);
		this.reviewId = Objects.requireNonNull(reviewId);
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

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}
	
	public JSONObject getLongJSON() {
		JSONTokener tokener = new JSONTokener(getLongDesc());
		return new JSONObject(tokener);
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
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
		result = 31 * result + language;
		result = 31 * result + title.hashCode();
		result = 31 * result + summary.hashCode();
		result = 31 * result + longDesc.hashCode();
		return result;
	}

	public void save() {
		Entity.Builder entity = Entity.newBuilder(getKey());
		entity.set(ReviewConstants.DELETED, isDeleted()).set(ReviewConstants.CREATEDDATE, getCreatedDate())
				.set(ReviewConstants.UPDATEDDATE, getUpdatedDate()).set(ReviewConstants.SUMMARY, getSummary())
				.set(ReviewConstants.TITLE, getTitle()).set(ReviewConstants.LANGUAGE, getLanguage()).set(ReviewConstants.REVIEW, getReviewId())
				.set(ReviewConstants.LONGDESC, StringValue.newBuilder(getLongDesc()).setExcludeFromIndexes(true).build());
		getDatastore().put(entity.build());
	}

	public static List<? extends Value<?>> convertTags(String[] tags) {
		List<StringValue> stringValues = Arrays.stream(tags).map(StringValue::of).collect(Collectors.toList());
		return Arrays.asList(ListValue.of(stringValues));
	}

	public void loadEvent(String key) {
		loadEvent(new Long(key).longValue());
	}

	public void loadEvent(long key) {
		loadEvent(Key.newBuilder(Constants.INCQ, ReviewConstants.REVIEW, key).build());
	}

	public void loadEvent(Key key) {
		// log.info("key " + key.toString());
		Entity event = getDatastore().get(key);
		loadFromEntity(event);

	}

	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);
		if (null != entity) {
			setTitle(entity.getString(ReviewConstants.TITLE));
			setLongDesc(entity.getString(ReviewConstants.LONGDESC));
			setSummary(entity.getString(ReviewConstants.SUMMARY));
			try{
				setLanguage(new Long(entity.getLong(ReviewConstants.LANGUAGE)).intValue());
			}
			catch(NumberFormatException nfe) {
				setLanguage(Language.DEFAULTLNAG);
			}
		}
	}

	public String toString() {
		return "Event{" + "" + Constants.KEY + "='" + getKeyString() + '\'' + ", " + ReviewConstants.DELETED + "=" + isDeleted()
				+ ", \" + CREATEDDATE + \"=" + getCreatedDate()+ ", \" + SUMMARY + \"=" + getSummary()
				+ ", \" + UPDATEDDATE + \"=" + getUpdatedDate() + ", "
				+ '\'' + ", \" + TITLE + \"='" + title + '\''
				+ ", \" + KINGDOM + \"='" + ReviewConstants.COMPACTDESC + '\''  + '\''
				+ ", \" + LONGDESC + \"='" + longDesc + '\'' + ", \" + REVIEWID + \"='" + reviewId + '\'' + '}';
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
