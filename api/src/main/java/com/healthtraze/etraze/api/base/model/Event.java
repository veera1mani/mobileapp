package com.healthtraze.etraze.api.base.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;

import javax.persistence.Id;

@Entity
@Table(name = "tbl_event")
public class Event {

	@Id
	@Column(name = Constants.ID)
	private Long id;

	@Column(name = Constants.VETERANID)
	private String veteranId;

	@Column(name = Constants.DEVICEID)
	private String deviceId;

	@Column(name = Constants.EVENTTYPE)
	private String eventType;

	@Column(name = StringIteration.CREATEDON)
	private LocalDateTime createdOn;

	@Column(name = StringIteration.RULEID)
	private String ruleId;

	@Column(name = StringIteration.STATUS)
	private String status;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVeteranId() {
		return veteranId;
	}

	public void setVeteranId(String veteranId) {
		this.veteranId = veteranId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
