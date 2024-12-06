package com.healthtraze.etraze.api.security.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

@Entity
@Table(name  ="tbl_user_session")
public class UserSession extends BaseModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = Constants.SESSIONID)
	private String sessionId;
	
	@Column(name = Constants.STATUS)
	private String status;
	
	@Column(name = Constants.IP)
	private String ip;
	
	@Column(name = Constants.PORT)
	private String port;
	
	@Column(name = Constants.COUNTRYNAME)
	private String countryName;
	
	@Column(name = Constants.CITYNAME)
	private String cityName;
	
	@Column(name = Constants.POSTAL)
	private String postal;
	
	@Column(name = Constants.STATE)
	private String state;
	
	@Column(name = Constants.USERAGENT)
	private String userAgent;
	
	@Column(name = Constants.LASTUPDATEDON)
	private LocalDateTime lastUpdatedOn;
	
	@Column(name = Constants.USERID)
	private String userId;
	
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getPostal() {
		return postal;
	}
	public void setPostal(String postal) {
		this.postal = postal;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public LocalDateTime getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	
	public void setLastUpdatedOn(LocalDateTime lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	
	
	
	
}
