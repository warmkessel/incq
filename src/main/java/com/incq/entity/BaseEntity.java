package com.incq.entity;

import java.io.Serializable;
import java.util.Objects;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.incq.constants.BaseEntityConstants;
import com.google.cloud.Timestamp;

public  abstract class BaseEntity implements  Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7160882192926634429L;
//	private static final Logger logger = Logger.getLogger(BaseEntity.class.getName());


	private Key key = null;
	private boolean deleted = false;
	private Timestamp createdDate = Timestamp.now();
	private Timestamp updatedDate = Timestamp.now();

	public BaseEntity() {

	}

	public BaseEntity(Key key, boolean deleted, Timestamp createdDate, Timestamp updatedDate) {

		this.key = Objects.requireNonNull(key);
		this.deleted = deleted;
		this.createdDate = Objects.requireNonNull(createdDate);
		this.updatedDate = Objects.requireNonNull(updatedDate);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + key.hashCode();
		result = 31 * result + createdDate.hashCode();
		result = 31 * result + updatedDate.hashCode();
		return result;
	}
	public void loadFromEntity(Entity entity) {		  
		if(null != entity) {
			setKey(entity.getKey());
			setDeleted(entity.getBoolean(BaseEntityConstants.DELETED));
			setCreatedDate(entity.getTimestamp(BaseEntityConstants.CREATEDDATE));
			setUpdatedDate(entity.getTimestamp(BaseEntityConstants.UPDATEDDATE));
		}	
	}
	public abstract String getKind();
	
	public Key getKey() {
		 if(null == key) {
			 KeyFactory keyFactory = getDatastore().newKeyFactory().setKind(getKind());  
			 key = getDatastore().allocateId(keyFactory.newKey());
		 }
		return key;
	}
	public Long getKeyLong() {
		return getKey().getId();
	}
	public String getKeyString() {
		return new Long(getKeyLong()).toString();
	}

	public void setKey(Key key) {
		this.key = key;
	}

	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(String bookmarked) {
		setDeleted(new Boolean(bookmarked).booleanValue());
	}
	
	public void setDeleted(boolean theDeleted) {
		deleted = theDeleted;
	}
	
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate() {
		setUpdatedDate(Timestamp.now());
	}
	
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	protected Datastore getDatastore() {
		return DatastoreOptions.getDefaultInstance().getService();

	}
}
