package com.incq.entity;

//import java.util.logging.Logger;

import com.incq.constants.*;
import com.incq.datastore.AuthorList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;

public class Author extends BaseEntity implements Comparable<Author> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3130362409864472907L;
	/**
	 * 
	 */
	// private static final Logger log = Logger.getLogger(Event.class.getName());

	private boolean bookmarked = false;
	private String translatedName = "";
	private String name = "";
	private String language = "";
	private String style = "";
	private String shortDescription = "";
	private String longDescription = "";
	private List<? extends Value<?>> tags = null;

	public Author() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getTranslatedName() {
		if(null == translatedName || translatedName.length() == 0) {
			translatedName = getName();
		}
		return translatedName;
	}

	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Language getLanguage() {
		return Language.findByCode(getLanguageSring());
	}

	private String getLanguageSring() {
		return language;
	}

	public void setLanguage(Language language) {
		setLanguage(language.code);
	}

	private void setLanguage(String language) {
		this.language = language;
	}

	public String getTagsEncodedString() {
		if (getTags().size() == 0) {
			return "";
		} else {
			List<String> tagStrings = getTags().stream().map(Value::get).map(Object::toString)
					.collect(Collectors.toList());
			return String.join("&tags=", tagStrings);
		}
	}

	public List<String> getTagsList() {
		return getTags().stream().map(Value::get).map(Object::toString).collect(Collectors.toList());
	}

	public String getTagsString() {
		if (getTags().size() == 0) {
			return "";
		} else {
			return String.join(" ", getTagsList());
		}
	}

	public List<? extends Value<?>> getTags() {

		if (null == tags) {
			tags = new ArrayList<>();
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Author other = (Author) obj;
		return this.getKey().equals(other.getKey());
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (bookmarked ? 1 : 0);
		result = 31 * result + name.hashCode();
		result = 31 * result + translatedName.hashCode();
		result = 31 * result + language.hashCode();
		result = 31 * result + shortDescription.hashCode();
		result = 31 * result + longDescription.hashCode();
		result = 31 * result + getTags().hashCode();

		return result;
	}

	public void save() {
		Key key = getKey();
		Entity.Builder entity = Entity.newBuilder(key);
		entity.set(AuthorConstants.DELETED, isDeleted()).set(AuthorConstants.BOOKMARKED, isBookmarked())
		.set(ReviewConstants.CREATEDDATE, getCreatedDate())
		.set(AuthorConstants.UPDATEDDATE, Timestamp.now())
		.set(AuthorConstants.NAME, getName())
				.set(AuthorConstants.TRANSLATEDNAME, getTranslatedName())
				.set(AuthorConstants.LONGDESC,
						StringValue.newBuilder(getLongDescription()).setExcludeFromIndexes(true).build())
				.set(AuthorConstants.SHORTDESC,
						StringValue.newBuilder(getShortDescription()).setExcludeFromIndexes(true).build())
				.set(ReviewConstants.TAGS, getTags())
				.set(AuthorConstants.STYLE, StringValue.newBuilder(getStyle()).setExcludeFromIndexes(true).build())
				.set(AuthorConstants.LANGUAGE, getLanguageSring()).build();
		getDatastore().put(entity.build());
	}

	public void loadAuthor(String name, Language lang) {
		loadFromEntity(AuthorList.fetchAuthor(name, lang));
	}
	public void loadAuthorAdmin(String name, Language lang) {
		loadFromEntity(AuthorList.fetchAuthorAdmin(name, lang, true));
	}
	public void loadAuthor(long key) {
		loadAuthor(Key.newBuilder(Constants.INCQ, ReviewConstants.AUTHOR, key).build());
	}

	public void loadAuthor(Key key) {
		Entity event = getDatastore().get(key);
		loadFromEntity(event);
	}

	public void loadFromEntitySiteMap(Entity entity) {
		super.loadFromEntity(entity);
		setName(entity.getString(AuthorConstants.NAME));

	}
	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);
		if (null != entity) {
			setBookmarked(entity.getBoolean(AuthorConstants.BOOKMARKED));
			setName(entity.getString(AuthorConstants.NAME));
			setTranslatedName(entity.getString(AuthorConstants.TRANSLATEDNAME));
			setStyle(entity.getString(AuthorConstants.STYLE));
			setLanguage(entity.getString(AuthorConstants.LANGUAGE));
			setShortDescription(entity.getString(AuthorConstants.SHORTDESC));
			setLongDescription(entity.getString(AuthorConstants.LONGDESC));
			if (entity.contains(AuthorConstants.TAGS)) {
				setTags(entity.getList(AuthorConstants.TAGS));
			}

		}
	}

	public String toString() {
		return "Event{" + "" + Constants.KEY + "='" + getKeyString() + '\'' + ", " + AuthorConstants.DELETED + "="
				+ isDeleted() + ", \" + BOOKMARKED + \"=" + bookmarked + ", \" + CREATEDDATE + \"=" + getCreatedDate()
				+ '\'' + ", \" + TAGS + \"='" + getTags() + ", \" + UPDATEDDATE + \"=" + getUpdatedDate() + ", "
				+ AuthorConstants.NAME + "=" + name + ", "+ AuthorConstants.TRANSLATEDNAME + "=" + translatedName + ", " + AuthorConstants.LANGUAGE + "=" + language + ", "
				+ AuthorConstants.STYLE + "=" + style + ", " + AuthorConstants.SHORTDESC + "=" + shortDescription + ", "
				+ AuthorConstants.LONGDESC + "=" + longDescription + ", " + '}';
	}

	public int compareTo(Author other) {
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
		return AuthorConstants.AUTHOR;
	}

}
