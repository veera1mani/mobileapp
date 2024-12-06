package com.healthtraze.etraze.api.base.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;

import javax.persistence.Id;

@Entity
@Table(name = "tbl_notification")
public class Notification  {

	@Id
	@Column(name = Constants.ID)
	private Long id;

	@Column(name =Constants.USERID)
	private String userId;

	@Column(name = Constants.TITLE)
	private String title;

	@Column(name = Constants.DESCRIPTION)
	private String description;

	@Column(name = StringIteration.CREATEDON)
	private LocalDateTime createdOn;

	@Column(name = Constants.ISREAD)
	private boolean isRead;
	
	@Column(name = Constants.SUBJECT)
	private String  subject;
	
	@Column(name = Constants.URL)
	private String  url;
	
	@Column(name = Constants.ASSETID)
	private String assetId;
	
	@Column(name = Constants.TENANTID)
	private String tenantId;
	
	
	
	

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime localDate) {
		this.createdOn = localDate;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	
	
	
}
