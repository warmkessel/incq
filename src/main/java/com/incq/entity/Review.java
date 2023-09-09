package com.incq.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
//import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.incq.constants.*;
import com.incq.datastore.*;
import com.google.cloud.*;
import com.google.cloud.datastore.*;
public class Review extends BaseEntity implements Comparable<Review> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -361472214131790072L;
	 //private static final Logger log = Logger.getLogger(Event.class.getName());

	private boolean bookmarked = false;

	private long userId = 0;
	private List<? extends Value<?>> tags = null;
	private List<? extends Value<?>> meta = null;
	private List<? extends Value<?>> media = null;
	private ReviewDetails reviewDetails = null;
	private String link = "";
	private String author = "";

	
	public Review() {
		setReviewDetails(new ReviewDetails());
	}
	public Review(Key key, boolean deleted, boolean bookmarked, Timestamp createdDate, Timestamp updatedDate, int userId, List<? extends Value<?>> tags, List<? extends Value<?>> meta, String author, String title, String compactDesc,
			String longDesc, String link, List<? extends Value<?>> media) {
		super(key, deleted, createdDate, updatedDate);
		this.bookmarked = bookmarked;
		this.userId = userId;
		this.tags = Objects.requireNonNull(tags);
		this.meta = Objects.requireNonNull(tags);
		this.author = Objects.requireNonNull(author);
		this.media = Objects.requireNonNull(media);
		this.link = Objects.requireNonNull(link);

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

	public String getTagsEncodedString() {
		if(getTags().size() == 0) {
			return "";
		}
		else {
			List<String> tagStrings = getTags().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
			return String.join("&tags=", tagStrings);
		}
	}
	
	public List<String> getTagsList() {
		return getTags().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
	}

	public String getTagsString() {
		if(getTags().size() == 0) {
			return "";
		}
		else {
			return String.join(" ", getTagsList());
		}
	}

	public List<? extends Value<?>> getTags() {

		if(null == tags) {
			tags =  new ArrayList<>();
		}
		return tags;
	}

	public void setTags(String tags) {
		String[] tagsArray = tags.toLowerCase().split(" ");
		setTags(Arrays.stream(tagsArray).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setTags(Set<String> tags) {
	    String[] tagsArray = tags.toArray(new String[tags.size()]);
		setTags(Arrays.stream(tagsArray).map(StringValue::of).collect(Collectors.toList()));
	}
	
	public void setTags(List<? extends Value<?>> tags) {
		this.tags = tags;
	}
	
	
	public String getMetaEncodedString() {
		if(getMeta().size() == 0) {
			return "";
		}
		else {
			return String.join("&tags=", getMetaList());
		}
	}
	
	public List<String> getMetaList() {
		return getMeta().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
	}

	public String getMetaString() {
		if(getMeta().size() == 0) {
			return "";
		}
		else {
			return String.join(" ", getMetaList());
		}
	}

	public List<? extends Value<?>> getMeta() {

		if(null ==
				meta) {
			meta =  new ArrayList<>();
		}
		return meta;
	}

	public void setMeta(String meta) {
		String[] metaArray = meta.toLowerCase().split(" ");
		setMeta(Arrays.stream(metaArray).map(StringValue::of).collect(Collectors.toList()));
	}

	public void setMeta(Set<String> meta) {
	    String[] metaArray = meta.toArray(new String[meta.size()]);
		setMeta(Arrays.stream(metaArray).map(StringValue::of).collect(Collectors.toList()));
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
		if(getMedia().size() == 0) {
			return "";
		}
		else {
			return String.join("&media=", getMediaList());
		}
	}
	

	public String getMediaString() {
		if(getMedia().size() == 0) {
			return "";
		}
		else {
			List<String> mediaStrings = getMedia().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
			return String.join(" ", mediaStrings);
		}
	}

	public List<? extends Value<?>> getMedia() {

		if(null == media) {
			media =  new ArrayList<>();
		}
		return media;
	}

	public void setMedia(String media) {
		String[] mediaArray = media.toLowerCase().split(" ");
		setMedia(Arrays.stream(mediaArray).map(StringValue::of).collect(Collectors.toList()));
	}
	

	
	public void setMedia(Set<String> media) {
	    String[] mediaArray = media.toArray(new String[media.size()]);
	    setMedia(Arrays.stream(mediaArray).map(StringValue::of).collect(Collectors.toList()));
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
		result = 31 * result + author.hashCode();
		result = 31 * result + media.hashCode();
		result = 31 * result + link.hashCode();

		return result;
	}

	public void save() {
		Key key = getKey();
		if(null != getReviewDetails()) {
			getReviewDetails().setReviewId(key.getId());
			getReviewDetails().save();
		}
		Entity.Builder entity = Entity.newBuilder(key);
		entity.set(ReviewConstants.DELETED, isDeleted()).set(ReviewConstants.BOOKMARKED, isBookmarked()).set(ReviewConstants.CREATEDDATE, getCreatedDate())
				.set(ReviewConstants.UPDATEDDATE, getUpdatedDate()).set(ReviewConstants.USERID, getUserId())
				.set(ReviewConstants.TAGS, getTags()).set(ReviewConstants.META, getMeta()).set(ReviewConstants.AUTHOR, getAuthor()).set(ReviewConstants.LINK, getLink())
				.set(ReviewConstants.MEDIA, getMedia()).build();
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
		Entity event = ReviewDetailsList.fetchEventDetails(key, lang);
		if(null != event) {
			ReviewDetails rd = new ReviewDetails();
			rd.loadFromEntity(event);
			setReviewDetails(rd);
		}
		loadEvent(Key.newBuilder(Constants.INCQ, ReviewConstants.REVIEW, key).build(), lang);
	}
	

	public void loadEvent(Key key, Language lang) {
		// log.info("key " + key.toString());
		Entity event = getDatastore().get(key);
		loadFromEntity(event, lang);

	}

	public void loadFromEntity(Entity entity, Language lang) {
		super.loadFromEntity(entity);
		if (null != entity) {
			setBookmarked(entity.getBoolean(ReviewConstants.BOOKMARKED));
			setUserId(entity.getLong(ReviewConstants.USERID));
			setTags(entity.getList(ReviewConstants.TAGS));
			setMeta(entity.getList(ReviewConstants.META));
			setAuthor(ReviewConstants.AUTHOR);
			setMedia(entity.getList(ReviewConstants.MEDIA));
			setLink(entity.getString(ReviewConstants.LINK));

		}
		Entity event = ReviewDetailsList.fetchEventDetails(entity.getKey().getId(), lang);
		if(null != event) {
			ReviewDetails rd = new ReviewDetails();
			rd.loadFromEntity(event);
			setReviewDetails(rd);
		}
	}

	public String toString() {
		return "Event{" + "" + Constants.KEY + "='" + getKeyString() + '\'' + ", " + ReviewConstants.DELETED + "=" + isDeleted()
				+ ", \" + BOOKMARKED + \"=" + bookmarked + ", \" + CREATEDDATE + \"=" + getCreatedDate()
				+ ", \" + UPDATEDDATE + \"=" + getUpdatedDate() + ", "+ ReviewConstants.USERID +"=" + userId + ", "
				+ '\'' + ", \" + TAGS + \"='" + getTags()+ ", \" + META + \"='" + getMeta()
				+ '\'' + ", \" + AUTHOR + \"='" + author + '\''
				+ ", \" + KINGDOM + \"='" + ReviewConstants.COMPACTDESC + '\'' + ", \" + LINK + \"='" + link
				+ '\'' + ", \" + MEDIA + \"'" + media + '\'' + '}';
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
