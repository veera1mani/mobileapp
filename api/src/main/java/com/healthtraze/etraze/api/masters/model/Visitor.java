package com.healthtraze.etraze.api.masters.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

@Entity
@Table(name = "tbl_visitors")
public class Visitor extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5834068667680971107L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "emailId")
	private String emailId;

	@Column(name = "photoURL")
	private String photoURL;

	@Column(name = "company")
	private String company;

	@Column(name = "location")
	private String location;
	
	@Column(name = "inTime")
	private Date inTime;
	
	@Column(name = "outTime")
	private Date outTime;
	
	@Column(name = "meetTo")
	private String meetTo;
	
	@Column(name = "purpose")
	private String purpose;
	
	@Column(name = "status")
	private String status;

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMeetTo() {
		return meetTo;
	}

	public void setMeetTo(String meetTo) {
		this.meetTo = meetTo;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
