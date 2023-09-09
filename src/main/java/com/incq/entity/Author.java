package com.incq.entity;

//import java.util.logging.Logger;

import com.incq.constants.*;
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
	private String name = "";
	private String language = "";
	private String style = "";
	private String shortDescription = "";
	private String longDescription = "";

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
		result = 31 * result + language.hashCode();
		result = 31 * result + shortDescription.hashCode();
		result = 31 * result + longDescription.hashCode();

		return result;
	}

	public void save() {
		Key key = getKey();
		Entity.Builder entity = Entity.newBuilder(key);
		entity.set(AuthorConstants.DELETED, isDeleted()).set(AuthorConstants.BOOKMARKED, isBookmarked())
				.set(AuthorConstants.CREATEDDATE, getCreatedDate()).set(AuthorConstants.UPDATEDDATE, getUpdatedDate())
				.set(AuthorConstants.NAME, getName())
				.set(AuthorConstants.LONGDESC, StringValue.newBuilder(getLongDescription()).setExcludeFromIndexes(true).build())
				.set(AuthorConstants.SHORTDESC, StringValue.newBuilder(getShortDescription()).setExcludeFromIndexes(true).build())
				.set(AuthorConstants.STYLE, StringValue.newBuilder(getStyle()).setExcludeFromIndexes(true).build())
				.set(AuthorConstants.LANGUAGE, getLanguageSring())
				.build();
		getDatastore().put(entity.build());
	}
	
	public void loadAuthor(long key) {
		loadAuthor(Key.newBuilder(Constants.INCQ, ReviewConstants.AUTHOR, key).build());
	}
	public void loadAuthor(Key key) {
		Entity event = getDatastore().get(key);
		loadFromEntity(event);
	}

	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);
		if (null != entity) {
			setBookmarked(entity.getBoolean(AuthorConstants.BOOKMARKED));
			setName(entity.getString(AuthorConstants.NAME));
			setStyle(entity.getString(AuthorConstants.STYLE));
			setLanguage(entity.getString(AuthorConstants.LANGUAGE));
			setShortDescription(entity.getString(AuthorConstants.SHORTDESC));
			setLongDescription(entity.getString(AuthorConstants.LONGDESC));
		}
	}

	public String toString() {
		return "Event{" + "" + Constants.KEY + "='" + getKeyString() + '\'' + ", " + AuthorConstants.DELETED + "="
				+ isDeleted() + ", \" + BOOKMARKED + \"=" + bookmarked + ", \" + CREATEDDATE + \"=" + getCreatedDate()
				+ ", \" + UPDATEDDATE + \"=" + getUpdatedDate() + ", " + AuthorConstants.NAME + "=" + name + ", "
				+ AuthorConstants.LANGUAGE + "=" + language + ", " + AuthorConstants.STYLE + "=" + style
				+ ", " + AuthorConstants.SHORTDESC + "=" + shortDescription
				+ ", " + AuthorConstants.LONGDESC + "=" + longDescription + ", " + '}';
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
