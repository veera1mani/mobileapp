package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_mobile_pin")
public class MobilePin extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4663783900404907825L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@Column(name = "userId")
	private String userId;
	
	@Column(name = "userAuthId")
	private String userAuthId;
	
	
	@Column(name = "pin")
	private String pin;
	
	@Column(name = "deviceId")
	private String deviceId;
	
	@Column(name = "pinExpiry")
	private String pinExpiry;
	
	@Transient
	private String uid;
	

	@Transient
	private String pass;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAuthId() {
		return userAuthId;
	}

	public void setUserAuthId(String userAuthId) {
		this.userAuthId = userAuthId;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPinExpiry() {
		return pinExpiry;
	}

	public void setPinExpiry(String pinExpiry) {
		this.pinExpiry = pinExpiry;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	
	
	
}
