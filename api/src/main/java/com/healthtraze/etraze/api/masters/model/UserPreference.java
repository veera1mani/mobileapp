package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

@Entity
@Table(name  = "tbl_user_preference") 
public class UserPreference extends BaseModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4168748353295034301L;

	@Id
	@Column(name = Constants.PREFERENCEID)
    private String preferenceId;

	@Column(name=Constants.USERID)
	private String userId;
	
	@Column(name=Constants.LANGUAGE)
	private String language;
	
	@Column(name=Constants.DATEFORMAT)
	private String dateFormat;
	
	@Column(name=Constants.CURRENCY)
	private String currency;
	
	@Column(name=Constants.TIMEZONE)
	private String timeZone;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getPreferenceId() {
		return preferenceId;
	}

	public void setPreferenceId(String preferenceId) {
		this.preferenceId = preferenceId;
	}

	
}
